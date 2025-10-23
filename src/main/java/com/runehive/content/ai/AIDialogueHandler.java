package com.runehive.content.ai;

import com.runehive.content.dialogue.Dialogue;
import com.runehive.content.dialogue.DialogueFactory;
import com.runehive.content.dialogue.Expression;
import com.runehive.game.task.Task;
import com.runehive.game.world.World;
import com.runehive.game.world.entity.mob.npc.Npc;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.object.CustomGameObject;
import com.runehive.game.world.object.ObjectDirection;
import com.runehive.game.world.position.Position;
import com.runehive.net.packet.out.SendInputMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AIDialogueHandler extends Dialogue {
    
    private static final Logger logger = LoggerFactory.getLogger(AIDialogueHandler.class);
    private static final int GANDALF_AI_ID = 2108;
    
    private final Npc npc;
    
    public AIDialogueHandler(Npc npc) {
        this.npc = npc;
    }
    
    @Override
    public void sendDialogues(DialogueFactory factory) {
        Player player = factory.getPlayer();
        player.locking.lock();
        
        // Ensure director knows the NPC and start with delay to prevent coordinate misalignment
        player.dialogueCamNpc = npc; // 2108
        player.dialogueCamMode = com.runehive.game.world.entity.mob.player.camera.DialogueCameraDirector.Mode.OFF;
        player.dialogueCamDelayTicks = 2; // delay 2 ticks before camera activates
        com.runehive.game.world.entity.mob.player.camera.DialogueCameraDirector.boostSwap(player, 8);
        
        factory.sendNpcChat(npc.id, Expression.HAPPY, 
                "What would you like to ask?",
                "Type in chat: ::gpt <your message>")
               .onAction(() -> {
                   player.locking.unlock();
                   player.interfaceManager.close();
                   com.runehive.game.world.entity.mob.player.camera.DialogueCameraDirector.requestReset(player);
               })
               .execute();
    }
    
    /**
     * Process AI command input (called from command handler)
     */
    public static void handleAICommand(Player player, String message) {
        int npcSpawnX = 1684;
        int npcSpawnY = 3746;
        int maxDistance = 1;
        
        if (Math.abs(player.getPosition().getX() - npcSpawnX) > maxDistance || 
            Math.abs(player.getPosition().getY() - npcSpawnY) > maxDistance) {
            player.message("You must be near the Wise Old Man to use AI chat.");
            return;
        }
        
        if (!LazyAIManager.isInitialized()) {
            player.message("AI services are not initialized. Click the Wise Old Man first.");
            return;
        }
        
        // Find the actual spawned Wise Old Man NPC near the spawn coordinates
        Npc gandalf = findNearbyNpc(player, GANDALF_AI_ID, npcSpawnX, npcSpawnY, maxDistance);
        
        if (gandalf == null) {
            player.message("Could not find the Wise Old Man NPC. Please stand closer.");
            return;
        }
        
        // ::gpt must use the exact same choreography as Talk-to on NPC 2108
        // Teleport P to exact position facing the NPC; LOCK P.
        com.runehive.game.world.position.Position npcPos = gandalf.getPosition();
        com.runehive.game.world.position.Position playerTarget = new com.runehive.game.world.position.Position(npcPos.getX() - 1, npcPos.getY(), npcPos.getHeight());
        player.move(playerTarget);
        player.face(gandalf);
        player.locking.lock();
        
        // Initialize dialogue camera - player speaks first (with delay to prevent misalignment)
        player.dialogueCamNpc = gandalf;
        player.dialogueCamMode = com.runehive.game.world.entity.mob.player.camera.DialogueCameraDirector.Mode.OFF;
        player.dialogueCamDelayTicks = 2; // delay 2 ticks before camera activates
        com.runehive.game.world.entity.mob.player.camera.DialogueCameraDirector.boostSwap(player, 8);
        
        // Mark that player has received instructions and is now using the command
        LazyAIManager.markInstructionsReceived(player.getUsername());
        
        String sanitizedInput = new AIDialogueHandler(gandalf).sanitizeInput(message);
        new AIDialogueHandler(gandalf).processGandalfAIMessage(player, gandalf, sanitizedInput);
    }
    
    /**
     * Find an NPC by ID near specific coordinates
     */
    private static Npc findNearbyNpc(Player player, int npcId, int targetX, int targetY, int maxDistance) {
        for (Npc npc : World.getNpcs()) {
            if (npc.id == npcId && npc.getPosition().getHeight() == player.getPosition().getHeight()) {
                int distX = Math.abs(npc.getPosition().getX() - targetX);
                int distY = Math.abs(npc.getPosition().getY() - targetY);
                if (distX <= maxDistance && distY <= maxDistance) {
                    return npc;
                }
            }
        }
        return null;
    }
    
    private String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        String cleaned = input.trim().replaceAll("[<>]", "");
        return cleaned.substring(0, Math.min(cleaned.length(), 500));
    }
    
    private void processGandalfAIMessage(Player player, Npc npc, String message) {
        // Spawn chair at NPC position (facing WEST)
        Position chairPos = npc.getPosition().copy();
        CustomGameObject chair = new CustomGameObject(1096, chairPos, ObjectDirection.WEST, com.runehive.game.world.object.ObjectType.GENERAL_PROP);
        chair.register();
        
        // Start thinking animation and speak "Hmmmmm" for 3 ticks
        // Treat as advancing into NPC turn (flip camera behind NPC and speed up the turn slightly).
        player.dialogueCamMode = com.runehive.game.world.entity.mob.player.camera.DialogueCameraDirector.Mode.BEHIND_NPC;
        com.runehive.game.world.entity.mob.player.camera.DialogueCameraDirector.boostSwap(player, 8);
        npc.animate(new com.runehive.game.Animation(4079));
        npc.speak("Hmmmmm");
        
        logger.info("Processing Gandalf AI request from {} (length: {})", 
                   player.getUsername(), message.length());
        
        LazyAIManager.getOpenAIService()
            .processPlayerMessage(player.getUsername(), message, npc.id)
            .thenAccept(response -> {
                World.schedule(new Task(3) {
                    @Override
                    public void execute() {
                        if (player == null || !player.isRegistered()) {
                            logger.warn("Player {} is no longer online, skipping response", 
                                player != null ? player.getUsername() : "null");
                            chair.unregister();
                            cancel();
                            return;
                        }
                        
                        try {
                            // Remove chair and reset animation after 3 ticks
                            chair.unregister();
                            npc.animate(new com.runehive.game.Animation(-1));
                            player.locking.lock();
                            
                            if (response != null && !response.isEmpty()) {
                                streamGandalfResponse(player, npc, response);
                            } else {
                                player.locking.unlock();
                                player.dialogueFactory
                                    .sendNpcChat(npc.id, Expression.SAD,
                                        "The mystical connection faltered.",
                                        "Perhaps try again with a different query.")
                                    .onAction(() -> {
                                        player.locking.unlock();
                                        player.interfaceManager.close();
                                    })
                                    .execute();
                            }
                        } catch (Exception e) {
                            logger.error("Exception while processing AI response for {}", player.getUsername(), e);
                            player.locking.unlock();
                        }
                        cancel();
                    }
                });
            })
            .exceptionally(throwable -> {
                logger.error("Error in Gandalf AI processing", throwable);
                World.schedule(new Task(1) {
                    @Override
                    public void execute() {
                        if (player == null || !player.isRegistered()) {
                            cancel();
                            return;
                        }
                        
                        try {
                            player.locking.unlock();
                            player.dialogueFactory
                                .sendNpcChat(npc.id, Expression.ANGRY,
                                    "The AI spirits are restless.",
                                    "Please try again in a moment.")
                                .onAction(() -> {
                                    player.interfaceManager.close();
                                })
                                .execute();
                        } catch (Exception e) {
                            logger.error("Exception in error handler for {}", player.getUsername(), e);
                            player.locking.unlock();
                        }
                        cancel();
                    }
                });
                return null;
            });
    }
    
    private void streamGandalfResponse(Player player, Npc npc, String response) {
        logger.info("[TTS-SERVER] ========================================");
        logger.info("[TTS-SERVER] Sending Gandalf response to player: {}", player.getUsername());
        logger.info("[TTS-SERVER] Full response: {}", response);
        
        String[] lines = wrapTextToLines(response, 50);
        logger.info("[TTS-SERVER] Wrapped into {} lines", lines.length);
        for (int i = 0; i < lines.length; i++) {
            logger.info("[TTS-SERVER] Line {}: {}", i+1, lines[i]);
        }
        
        DialogueFactory factory = player.dialogueFactory;
        
        // Set camera to behind NPC for AI response
        player.dialogueCamMode = com.runehive.game.world.entity.mob.player.camera.DialogueCameraDirector.Mode.BEHIND_NPC;
        
        int linesPerPage = 3;
        for (int i = 0; i < lines.length; i += linesPerPage) {
            int endIndex = Math.min(i + linesPerPage, lines.length);
            String[] pageLines = java.util.Arrays.copyOfRange(lines, i, endIndex);
            
            logger.info("[TTS-SERVER] Sending dialogue page {}: {} lines", (i/linesPerPage)+1, pageLines.length);
            
            factory.sendNpcChat(npc.id, Expression.HAPPY, pageLines)
                   .onAction(() -> {
                       String combinedText = String.join(" ", pageLines);
                       npc.speak(combinedText);
                   });
        }
        logger.info("[TTS-SERVER] ========================================");
        
        factory.sendOption(
            "Ask another question", () -> {
                // ASK ANOTHER QUESTION: Immediately reset camera & unlock
                com.runehive.game.world.entity.mob.player.camera.DialogueCameraDirector.requestReset(player);
                player.locking.unlock();
                player.dialogueFactory.sendDialogue(new AIDialogueHandler(npc)).execute();
            },
            "No further questions", () -> {
                // OPT OUT: Rewind to BEHIND_PLAYER (buttery swap), wait 4 ticks, then overhead line + wave, then unlock and reset
                player.interfaceManager.close();  // Close interface first
                player.dialogueCamMode = com.runehive.game.world.entity.mob.player.camera.DialogueCameraDirector.Mode.BEHIND_PLAYER;
                com.runehive.game.world.entity.mob.player.camera.DialogueCameraDirector.boostSwap(player, 6);
                
                // Wait 5 ticks for camera to reposition behind player, then player speaks
                World.schedule(new Task(5) {
                    @Override
                    public void execute() {
                        // Player overhead text
                        player.speak("No, that's all, thank you.");
                        
                        // 1 tick later, NPC responds with overhead + wave
                        World.schedule(new Task(1) {
                            @Override
                            public void execute() {
                                npc.speak("Come back again, anytime!");
                                npc.animate(863);
                                
                                // After overhead line + animation, unlock & reset camera
                                World.schedule(new Task(3) {
                                    @Override
                                    public void execute() {
                                        LazyAIManager.clearPlayerConsent(player.getUsername());
                                        player.locking.unlock();
                                        com.runehive.game.world.entity.mob.player.camera.DialogueCameraDirector.requestReset(player);
                                        cancel();
                                    }
                                });
                                cancel();
                            }
                        });
                        cancel();
                    }
                });
            }
        );
        
        factory.execute();
    }
    
    private String[] wrapTextToLines(String text, int maxCharsPerLine) {
        String[] words = text.split(" ");
        StringBuilder current = new StringBuilder();
        java.util.List<String> lines = new java.util.ArrayList<>();
        
        for (String word : words) {
            if (current.length() + word.length() + 1 > maxCharsPerLine) {
                if (current.length() > 0) {
                    lines.add(current.toString().trim());
                    current = new StringBuilder(word);
                } else {
                    lines.add(word);
                }
            } else {
                if (current.length() > 0) {
                    current.append(" ");
                }
                current.append(word);
            }
        }
        
        if (current.length() > 0) {
            lines.add(current.toString().trim());
        }
        
        return lines.toArray(new String[0]);
    }
}
