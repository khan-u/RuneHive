package com.runehive.game.world.items.containers.impl;

import com.runehive.game.world.InterfaceConstants;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.items.Item;
import com.runehive.game.world.items.containers.ItemContainer;
import com.runehive.game.world.items.containers.pricechecker.PriceChecker;
import com.runehive.net.packet.out.SendItemOnInterface;
import com.runehive.net.packet.out.SendMessage;
import com.runehive.net.packet.out.SendString;

/**
 * Handles the donator deposit box.
 *
 * @author DanielDonatorDeposit
 */
public class LostUntradeables extends ItemContainer {
    private static final int DONATOR_DEPOSIT_BOX_DISPLAY_ID = 57307;

    private final Player player;

    public LostUntradeables(Player player) {
        super(30, StackPolicy.ALWAYS);
        this.player = player;
    }

    public void open() {
        refresh();
        player.interfaceManager.open(57300);
    }

    public void close() {
        clear(false);
        refresh();
    }

    public boolean deposit(Item item) {
        int id = item.getId();
        setFiringEvents(false);
        add(id, item.getAmount());
        setFiringEvents(true);
        refresh();
        return true;
    }

    public boolean claim(int slot, int id) {
        if (!player.interfaceManager.isInterfaceOpen(57300)) {
            return false;
        }

        Item item = get(slot);

        if (item == null || item.getId() != id) {
            return false;
        }

        if (player.inventory.getFreeSlots() == 0) {
            player.message("You do not have enough free inventory space!");
            return false;
        }

        if (!player.inventory.contains(13307, 350)) {
            player.message("You need 350 blood money to re-claim this item!");
            return false;
        }

        if (remove(item.getId(), 1)) {
            player.inventory.add(id, 1);
            player.inventory.remove(13307, 350);
            shift();
            refresh();
        }
        return true;
    }

    private void refresh() {
        refresh(player, DONATOR_DEPOSIT_BOX_DISPLAY_ID);
    }

    @Override
    public void onRefresh() {
        player.inventory.refresh();
        player.send(new SendString(this.size() + "/" + this.capacity(), 57306));
        player.send(new SendItemOnInterface(DONATOR_DEPOSIT_BOX_DISPLAY_ID, getItems()));
    }
}
