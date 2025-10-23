package com.runehive.game.world.entity.mob.player.camera;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.position.Position;
import com.runehive.game.world.entity.mob.npc.Npc;
import com.runehive.net.packet.out.SendCameraMove;
import com.runehive.net.packet.out.SendCameraTurn;
import com.runehive.net.packet.out.SendCameraReset;

/**
 * Drives a simple "over-the-shoulder" dialogue camera:
 *  - BEHIND_PLAYER: camera behind player, looks at NPC
 *  - BEHIND_NPC:    camera behind NPC, looks at player
 * Smoothness is controlled by camera packet speeds (const/var).
 */
public final class DialogueCameraDirector {

    public enum Mode { OFF, BEHIND_PLAYER, BEHIND_NPC, RESET_PENDING }
    
    // Enable debug logging (set to false for production)
    private static final boolean DEBUG = true;

    // --- Shoulder/eye-line tuning ---
    private static final double BACK       = 1.7;   // tiles behind current speaker
    private static final double RIGHT_BASE = 0.35;  // subtle shoulder offset baseline
    private static final double RIGHT_NUDGE= 0.25;  // 0.35 + 0.25 = 0.60 total
    private static final double RIGHT      = RIGHT_BASE + RIGHT_NUDGE; // = 0.60 effective
    private static final int MOVE_Z        = 220;   // eye/shoulder-ish height for 166
    private static final int TURN_TILT_Z   = 210;   // comfortable tilt for 177

    // --- Base smooth speeds (constant speed, no distance-based slowdown) ---
    private static final int MOVE_CONST_BASE = 18, MOVE_VAR_BASE = 0;
    private static final int TURN_CONST_BASE = 20, TURN_VAR_BASE = 0;
    // --- Slightly faster turn during swaps (constant speed) ---
    private static final int MOVE_CONST_SWAP = 16, MOVE_VAR_SWAP = 0;
    private static final int TURN_CONST_SWAP = 14, TURN_VAR_SWAP = 0;

    private DialogueCameraDirector() {}

    /** Recompute and send camera on each tick while active. */
    public static void tick(Player p) {
        if (p.dialogueCamMode == Mode.OFF) return;
        if (p.regionChange) return; // skip 1-2 ticks while scene base shifts
        
        // Handle delay before camera activation
        if (p.dialogueCamDelayTicks > 0) {
            p.dialogueCamDelayTicks--;
            if (p.dialogueCamDelayTicks == 0) {
                p.dialogueCamMode = Mode.BEHIND_PLAYER; // now safe to activate
                if (DEBUG) {
                    System.out.println("[DialogueCam] Delay complete, activating BEHIND_PLAYER mode");
                }
            }
            if (DEBUG) {
                System.out.println("[DialogueCam] Delaying activation: " + p.dialogueCamDelayTicks + " ticks remaining");
            }
            return;
        }

        if (p.dialogueCamMode == Mode.OFF) return;

        final Npc npc = p.dialogueCamNpc;
        if (npc == null || npc.isDead() || p.isDead()) {
            // If target is gone, reset smoothly
            requestReset(p);
            return;
        }

        final Position playerPos = p.getPosition();
        final Position npcPos    = npc.getPosition();

        // Choose speeds; during swap-boost we turn a bit faster
        final boolean swapBoost = p.dialogueCamSwapBoostTicks > 0;
        final int moveConst = swapBoost ? MOVE_CONST_SWAP : MOVE_CONST_BASE;
        final int moveVar   = swapBoost ? MOVE_VAR_SWAP   : MOVE_VAR_BASE;
        final int turnConst = swapBoost ? TURN_CONST_SWAP : TURN_CONST_BASE;
        final int turnVar   = swapBoost ? TURN_VAR_SWAP   : TURN_VAR_BASE;

        if (DEBUG) {
            System.out.println("[DialogueCam] Mode=" + p.dialogueCamMode +
                " | Delay=" + p.dialogueCamDelayTicks +
                " | PlayerPos=" + p.getPosition() +
                " | NpcPos=" + (p.dialogueCamNpc != null ? p.dialogueCamNpc.getPosition() : "null"));
        }

        switch (p.dialogueCamMode) {
            case BEHIND_PLAYER -> {
                shoulderShot(p, playerPos, npcPos, moveConst, moveVar, turnConst, turnVar); // behind player, look at NPC
            }
            case BEHIND_NPC -> {
                shoulderShot(p, npcPos, playerPos, moveConst, moveVar, turnConst, turnVar); // behind NPC, look at player
            }
            case RESET_PENDING -> {
                // Send one gentle easing frame, then clear cinematics
                shoulderShot(p, playerPos, playerPos, MOVE_CONST_BASE, MOVE_VAR_BASE, TURN_CONST_BASE, TURN_VAR_BASE); // short ease toward default
                p.send(new SendCameraReset());         // opcode 107
                p.dialogueCamMode = Mode.OFF;
                p.dialogueCamNpc  = null;
                p.dialogueCamSwapBoostTicks = 0;
            }
            default -> { /* no-op */ }
        }

        if (p.dialogueCamSwapBoostTicks > 0) {
            p.dialogueCamSwapBoostTicks--;
        }
    }

    /** Place camera behind 'from' and turn toward 'to'. */
    private static void shoulderShot(Player p, Position from, Position to,
                                     int moveConst, int moveVar, int turnConst, int turnVar) {
        System.out.println("== Dialogue Camera Shot ==");
        System.out.println("Player world pos : " + from);
        System.out.println("Target world pos : " + to);
        System.out.println("Player position  : " + p.getPosition());
        System.out.println("from.getLocalX(p.pos) : " + from.getLocalX(p.getPosition()));
        System.out.println("from.getLocalY(p.pos) : " + from.getLocalY(p.getPosition()));
        System.out.println("to.getLocalX(p.pos)   : " + to.getLocalX(p.getPosition()));
        System.out.println("to.getLocalY(p.pos)   : " + to.getLocalY(p.getPosition()));
        
        final double dx = to.getX() - from.getX();
        final double dy = to.getY() - from.getY();
        final double len = Math.max(1e-6, Math.hypot(dx, dy));

        // Forward vector from 'from' → 'to'
        final double fx = dx / len;
        final double fy = dy / len;

        // Right-hand vector (perpendicular on the plane)
        final double rx =  fy, ry = -fx;

        // Behind-right shoulder camera position
        final double camX = from.getX() - fx * BACK + rx * RIGHT;
        final double camY = from.getY() - fy * BACK + ry * RIGHT;

        final Position cam  = new Position((int)Math.round(camX), (int)Math.round(camY), from.getHeight());
        final Position look = to; // look straight at the target speaker/listener

        // Guard: skip if we haven't set the scene base yet (first tick before MapRegion)
        if (p.getSceneBaseChunkX() == 0 && p.getSceneBaseChunkY() == 0) {
            return;
        }

        final int localCamX  = p.toSceneLocalX(cam);
        final int localCamY  = p.toSceneLocalY(cam);
        final int localLookX = p.toSceneLocalX(look);
        final int localLookY = p.toSceneLocalY(look);

        // Safety bounds
        if ((localCamX|localCamY|localLookX|localLookY) < 0 ||
            localCamX > 104 || localCamY > 104 || localLookX > 104 || localLookY > 104) {
            System.err.printf("[Camera BUG] Out-of-bounds: LocalCam=(%d,%d) LocalLook=(%d,%d)%n",
                localCamX, localCamY, localLookX, localLookY);
            return;
        }

        if (DEBUG) {
            System.out.printf("GlobalCam=(%d,%d) GlobalLook=(%d,%d) | SceneBaseTiles=(%d,%d) | LocalCam=(%d,%d) LocalLook=(%d,%d)%n",
                cam.getX(), cam.getY(),
                look.getX(), look.getY(),
                p.getSceneBaseChunkX() * 8, p.getSceneBaseChunkY() * 8,
                localCamX, localCamY,
                localLookX, localLookY
            );
        }

        p.send(new SendCameraMove(localCamX, localCamY, MOVE_Z, moveConst, moveVar));
        p.send(new SendCameraTurn(localLookX, localLookY, TURN_TILT_Z, turnConst, turnVar));
    }

    /** Ask for a smooth reset on the next tick. */
    public static void requestReset(Player p) {
        if (p.dialogueCamMode != Mode.OFF) {
            p.dialogueCamMode = Mode.RESET_PENDING;
        }
    }

    /** Small helper to prime faster turn for a few ticks after a mode swap. */
    public static void boostSwap(Player p, int ticks) {
        p.dialogueCamSwapBoostTicks = Math.max(p.dialogueCamSwapBoostTicks, Math.max(1, ticks));
    }

}
