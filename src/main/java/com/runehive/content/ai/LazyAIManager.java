package com.runehive.content.ai;

import com.runehive.content.dialogue.Expression;
import com.runehive.game.world.entity.mob.npc.Npc;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.position.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runehive.game.task.Task;
import com.runehive.game.world.World;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Set;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class LazyAIManager {
    
    private static final Logger logger = LoggerFactory.getLogger(LazyAIManager.class);
    private static final AtomicBoolean openAIInitialized = new AtomicBoolean(false);
    private static final int GANDALF_AI_ID = 2108;
    
    private static final Set<String> sessionConsents = ConcurrentHashMap.newKeySet();
    private static final Map<String, Task> inactivityTimers = new ConcurrentHashMap<>();
    
    private static volatile OpenAIService openAIService;
    
    public static boolean handleGandalfAIClick(Player player, Npc npc) {
        if (npc.id != GANDALF_AI_ID) {
            return false;
        }
        
        // Position player 1 tile west of NPC and setup camera
        Position npcPos = npc.getPosition();
        Position playerTarget = new Position(npcPos.getX() - 1, npcPos.getY(), npcPos.getHeight());
        player.move(playerTarget);
        player.face(npc);
        
        // Initialize dialogue camera - player speaks first
        player.dialogueCamNpc = npc;
        player.dialogueCamMode = com.runehive.game.world.entity.mob.player.camera.DialogueCameraDirector.Mode.BEHIND_PLAYER;
        
        if (sessionConsents.contains(player.getUsername())) {
            if (!openAIInitialized.get()) {
                initializeOpenAIServices();
            }
            
            // Always show instructions when NPC is clicked (connection is idle)
            showGptInstructions(player, npc);
        } else {
            showOpenAIConnectionOffer(player, npc);
        }
        return true;
    }
    
    private static void showOpenAIConnectionOffer(Player player, Npc npc) {
        player.dialogueFactory
            .sendPlayerChat("Hey, Wise Old Man!")
            .onAction(() -> player.dialogueCamMode = com.runehive.game.world.entity.mob.player.camera.DialogueCameraDirector.Mode.BEHIND_NPC)
            .sendNpcChat(npc.id, Expression.HAPPY,
                "Greetings, " + player.getUsername() + "!",
                "I am Gandalf GPT4o. I can connect to the OpenAI",
                "mystical networks to provide you with advanced",
                "AI assistance.")
            .sendNpcChat(npc.id, Expression.HAPPY,
                "Would you like me to establish this connection?")
            .sendOption(
                "Yes, connect to OpenAI", () -> {
                    sessionConsents.add(player.getUsername());
                    logger.info("Player {} consented to OpenAI connection for this session", player.getUsername());
                    
                    // Send blue chat message to player about service initialization
                    String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
                    player.message("<col=0000FF>[OpenAI Service] Initialized at " + timestamp + ". Conversation History = 10.</col>");
                    
                    initializeOpenAIAndProcess(player, npc);
                },
                "No, maybe later", () -> {
                    player.dialogueFactory
                        .sendNpcChat(npc.id, Expression.HAPPY,
                            "Very well. I shall remain here if you change",
                            "your mind. The mystical networks await when",
                            "you are ready.")
                        .execute();
                }
            )
            .execute();
    }
    
    private static void initializeOpenAIAndProcess(Player player, Npc npc) {
        if (!openAIInitialized.get()) {
            if (initializeOpenAIServices()) {
                showGptInstructionsWithGreeting(player, npc);
            } else {
                player.dialogueFactory
                    .sendNpcChat(npc.id, Expression.SAD,
                        "Alas! The mystical networks are unreachable.",
                        "Please ensure the OpenAI key scroll is present",
                        "and try again later.")
                    .execute();
                return;
            }
        } else {
            showGptInstructionsWithGreeting(player, npc);
        }
    }
    
    private static synchronized boolean initializeOpenAIServices() {
        if (openAIInitialized.compareAndSet(false, true)) {
            logger.info("Player accepted OpenAI connection - initializing OpenAI services...");
            
            try {
                openAIService = OpenAIService.getInstance();
                openAIService.initialize();
                logger.info("Official OpenAI Service initialized with GPT-4o-mini model");
                
                logger.info("OpenAI services fully initialized and ready for interactions!");
                return true;
                
            } catch (Exception e) {
                logger.error("Failed to initialize OpenAI services", e);
                openAIInitialized.set(false);
                return false;
            }
        }
        return true;
    }
    
    public static boolean isInitialized() {
        return openAIInitialized.get();
    }
    
    public static OpenAIService getOpenAIService() {
        return openAIService;
    }
    
    private static void showGptInstructionsWithGreeting(Player player, Npc npc) {
        npc.animate(new com.runehive.game.Animation(858));
        npc.speak("GPT 4o at your service!");
        player.dialogueFactory
            .sendPlayerChat("Hello again, Wise Old Man!")
            .onAction(() -> player.dialogueCamMode = com.runehive.game.world.entity.mob.player.camera.DialogueCameraDirector.Mode.BEHIND_NPC)
            .sendNpcChat(npc.id, Expression.HAPPY,
                "To send a message, type:",
                "<col=0000FF>::gpt <your message></col>")
            .onAction(() -> {
                player.interfaceManager.close();
                com.runehive.game.world.entity.mob.player.camera.DialogueCameraDirector.requestReset(player);
                startInactivityTimer(player, npc);
            })
            .execute();
    }
    
    private static void showGptInstructions(Player player, Npc npc) {
        player.dialogueFactory
            .sendPlayerChat("Hello again, Wise Old Man!")
            .onAction(() -> player.dialogueCamMode = com.runehive.game.world.entity.mob.player.camera.DialogueCameraDirector.Mode.BEHIND_NPC)
            .sendNpcChat(npc.id, Expression.HAPPY,
                "To send a message, type:",
                "<col=0000FF>::gpt <your message></col>")
            .onAction(() -> {
                player.interfaceManager.close();
                com.runehive.game.world.entity.mob.player.camera.DialogueCameraDirector.requestReset(player);
            })
            .execute();
    }
    
    public static void markInstructionsReceived(String username) {
        cancelInactivityTimer(username);
    }
    
    private static void startInactivityTimer(Player player, Npc npc) {
        cancelInactivityTimer(player.getUsername());
        
        Task timer = new Task(100) {
            int warningsSent = 0;
            
            @Override
            public void execute() {
                if (!player.isRegistered() || !sessionConsents.contains(player.getUsername())) {
                    cancel();
                    return;
                }
                
                if (warningsSent == 0) {
                    player.dialogueFactory
                        .sendNpcChat(npc.id, Expression.HAPPY,
                            "Hey " + player.getUsername() + ", I'm still here!",
                            "If I don't hear back from you within 1 minute,",
                            "I shall close the OpenAI realms. Your message",
                            "history would be reset in this event.")
                        .execute();
                    warningsSent++;
                } else {
                    player.dialogueFactory
                        .sendNpcChat(npc.id, Expression.HAPPY,
                            "Farewell, " + player.getUsername() + "!")
                        .execute();
                    clearPlayerConsent(player.getUsername());
                    cancel();
                }
            }
        };
        
        World.schedule(timer);
        inactivityTimers.put(player.getUsername(), timer);
    }
    
    private static void cancelInactivityTimer(String username) {
        Task timer = inactivityTimers.remove(username);
        if (timer != null) {
            timer.cancel();
        }
    }
    
    public static void clearPlayerConsent(String username) {
        if (sessionConsents.remove(username)) {
            logger.info("Cleared OpenAI consent for player: {}", username);
            
            // Send blue chat message to player about service termination
            World.getPlayerByName(username).ifPresent(player -> {
                String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
                player.message("<col=0000FF>[OpenAI Service] Terminated at " + timestamp + ". Conversation History flushed.</col>");
            });
        }
        
        cancelInactivityTimer(username);
        
        if (openAIService != null) {
            openAIService.clearConversationHistory(username);
        }
    }
    
    public static void shutdown() {
        if (openAIInitialized.get() && openAIService != null) {
            logger.info("Shutting down OpenAI services...");
            openAIService.shutdown();
        }
        sessionConsents.clear();
        inactivityTimers.values().forEach(Task::cancel);
        inactivityTimers.clear();
    }
}
