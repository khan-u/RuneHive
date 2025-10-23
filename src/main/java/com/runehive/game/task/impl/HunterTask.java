package com.runehive.game.task.impl;

import com.runehive.content.skill.impl.hunter.trap.Trap;
import com.runehive.content.skill.impl.hunter.trap.TrapExecution;
import com.runehive.content.skill.impl.hunter.trap.TrapManager;
import com.runehive.game.task.Task;
import com.runehive.game.world.World;

import java.util.Iterator;

public class HunterTask extends Task {
    private static boolean RUNNING;

    public static void intialize() {
        if (!RUNNING) {
            RUNNING = true;
            World.schedule(new HunterTask());
        }
    }

    public HunterTask() {
        super(false, 1);
    }

    @Override
    protected void execute() {
        final Iterator<Trap> iterator = TrapManager.traps.iterator();
        while (iterator.hasNext()) {
            final Trap trap = iterator.next();
            if (trap == null)
                continue;
            if (trap.getOwner() == null || !trap.getOwner().isRegistered())
                TrapManager.deregister(trap);
            TrapExecution.setTrapProcess(trap);
            TrapExecution.trapTimerManagement(trap);
        }

        if (TrapManager.traps.isEmpty()) {
            cancel();
        }
    }

    @Override
    protected void onCancel(boolean logout) {
        RUNNING = false;
    }
}
