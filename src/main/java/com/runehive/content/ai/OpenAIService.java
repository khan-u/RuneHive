package com.runehive.content.ai;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.ChatCompletionMessage;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public final class OpenAIService {
    
    private static final Logger logger = LoggerFactory.getLogger(OpenAIService.class);
    private static final String API_KEY_ENV = "OPENAI_API_KEY";
    private static final String API_KEY_FILE = "openai-java-key.txt";
    private static final int MAX_TOKENS = 500;
    private static final double TEMPERATURE = 0.8;
    private static final int MAX_CONVERSATION_HISTORY = 10;
    
    private static final OpenAIService INSTANCE = new OpenAIService();
    
    private final ExecutorService executorService;
    private final ConcurrentHashMap<String, List<ChatMessage>> conversationHistory;
    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private OpenAIClient openAIClient;
    private String apiKey;
    
    private OpenAIService() {
        this.executorService = Executors.newFixedThreadPool(4);
        this.conversationHistory = new ConcurrentHashMap<>();
    }
    
    public static OpenAIService getInstance() {
        return INSTANCE;
    }
    
    public void initialize() {
        if (initialized.compareAndSet(false, true)) {
            logger.info("Initializing Official OpenAI Service with GPT-4o-mini model");
            
            try {
                loadApiKey();
                
                openAIClient = OpenAIOkHttpClient.builder()
                    .apiKey(apiKey)
                    .timeout(Duration.ofSeconds(30))
                    .maxRetries(3)
                    .build();
                
                logger.info("Official OpenAI Service initialized and ready for GPT-4o-mini requests");
            } catch (Exception e) {
                logger.error("Failed to initialize Official OpenAI Service", e);
                initialized.set(false);
                throw new RuntimeException("OpenAI Service initialization failed", e);
            }
        }
    }
    
    private void loadApiKey() throws IOException {
        apiKey = System.getenv(API_KEY_ENV);
        
        if (apiKey != null && !apiKey.trim().isEmpty()) {
            apiKey = apiKey.trim();
            if (apiKey.matches("^sk-[a-zA-Z0-9\\-_]{20,}$")) {
                logger.info("OpenAI API key loaded from environment variable");
                return;
            } else {
                logger.warn("Invalid API key format in environment variable - must match pattern: sk-[alphanumeric]{20+}");
                apiKey = null;
            }
        }
        
        try {
            Path keyFile = Paths.get(API_KEY_FILE);
            if (Files.exists(keyFile)) {
                byte[] bytes = Files.readAllBytes(keyFile);
                apiKey = new String(bytes, "UTF-8").trim();
                if (apiKey.isEmpty() || !apiKey.matches("^sk-[a-zA-Z0-9\\-_]{20,}$")) {
                    throw new IllegalArgumentException("Invalid API key format in file - must match pattern: sk-[alphanumeric]{20+}");
                }
                logger.warn("OpenAI API key loaded from file - consider using environment variable {} for better security", API_KEY_ENV);
                return;
            }
        } catch (IOException e) {
            logger.error("Could not read API key file: {}", API_KEY_FILE);
        }
        
        throw new IOException("OpenAI API key not found. Set environment variable " + API_KEY_ENV + 
                            " or create file " + API_KEY_FILE + " with a valid API key starting with 'sk-'");
    }
    
    public CompletableFuture<String> processPlayerMessage(String username, String message, int npcId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (!initialized.get()) {
                    logger.warn("OpenAI Service not initialized");
                    return "The mystical networks are not yet connected. Please try again.";
                }
                
                List<ChatMessage> history = conversationHistory.computeIfAbsent(username, 
                    k -> new ArrayList<>());
                
                history.add(new ChatMessage("user", message));
                
                if (history.size() > MAX_CONVERSATION_HISTORY) {
                    history = new ArrayList<>(history.subList(
                        history.size() - MAX_CONVERSATION_HISTORY, history.size()));
                    conversationHistory.put(username, history);
                }
                
                ChatCompletionCreateParams.Builder paramsBuilder = ChatCompletionCreateParams.builder()
                    .model(ChatModel.GPT_4O_MINI)
                    .maxCompletionTokens(MAX_TOKENS)
                    .temperature(TEMPERATURE)
                    .addDeveloperMessage(
                        "You are a helpful AI assistant. Provide accurate, concise, and direct answers to questions. " +
                        "Keep responses under 150 tokens while ensuring they fully answer the question."
                    );
                
                for (ChatMessage msg : history) {
                    if ("user".equals(msg.getRole())) {
                        paramsBuilder.addUserMessage(msg.getContent());
                    } else if ("assistant".equals(msg.getRole())) {
                        paramsBuilder.addAssistantMessage(msg.getContent());
                    }
                }
                
                ChatCompletionCreateParams params = paramsBuilder.build();
                
                logger.debug("Making OpenAI API request for user: {}", hashUsername(username));
                ChatCompletion completion = openAIClient.chat().completions().create(params);
                
                if (completion.choices().isEmpty()) {
                    logger.warn("No choices returned from OpenAI API");
                    return "The mystical spirits did not respond. Please try again.";
                }
                
                String response = completion.choices().get(0).message().content().orElse("");
                if (response.isEmpty()) {
                    logger.warn("Empty response from OpenAI API");
                    return "The AI spirits whispered too quietly. Please try again.";
                }
                
                history.add(new ChatMessage("assistant", response));
                
                completion.usage().ifPresent(usage -> {
                    logger.info("OpenAI API usage for {}: prompt_tokens={}, completion_tokens={}, total_tokens={}", 
                        hashUsername(username), usage.promptTokens(), usage.completionTokens(), usage.totalTokens());
                });
                
                logger.debug("Successfully processed OpenAI request for user: {}", hashUsername(username));
                return response;
                
            } catch (Exception e) {
                logger.error("Error processing OpenAI request for user: " + username, e);
                return "The mystical connection encountered turbulence. Please try again later.";
            }
        }, executorService);
    }
    
    public void clearConversationHistory(String username) {
        if (conversationHistory.remove(username) != null) {
            logger.debug("Cleared conversation history for user: {}", hashUsername(username));
        }
    }
    
    private String hashUsername(String username) {
        if (username == null) return "null";
        int hash = username.hashCode();
        return "user_" + Integer.toHexString(hash);
    }
    
    public boolean isInitialized() {
        return initialized.get();
    }
    
    public int getMinWordCount() {
        return 0;
    }
    
    public int getActiveSessionCount() {
        return conversationHistory.size();
    }
    
    public void shutdown() {
        logger.info("Shutting down Official OpenAI Service...");
        
        conversationHistory.clear();
        
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
        
        openAIClient = null;
        
        initialized.set(false);
        logger.info("Official OpenAI Service shutdown complete");
    }
    
    private static class ChatMessage {
        private final String role;
        private final String content;
        
        public ChatMessage(String role, String content) {
            this.role = role;
            this.content = content;
        }
        
        public String getRole() {
            return role;
        }
        
        public String getContent() {
            return content;
        }
    }
}
