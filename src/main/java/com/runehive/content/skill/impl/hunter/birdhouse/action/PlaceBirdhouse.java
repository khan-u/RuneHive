package com.runehive.content.skill.impl.hunter.birdhouse.action;

import com.runehive.content.skill.impl.hunter.birdhouse.BirdhouseData;
import com.runehive.content.skill.impl.hunter.birdhouse.PlayerBirdHouseData;
import com.runehive.game.action.Action;
import com.runehive.game.action.policy.WalkablePolicy;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.entity.mob.player.persist.PlayerSerializer;
import com.runehive.game.world.items.Item;
import com.runehive.game.world.object.CustomGameObject;
import com.runehive.game.world.object.GameObject;
import com.runehive.net.packet.out.SendAddObject;

public class PlaceBirdhouse extends Action<Player> {

    private BirdhouseData birdhouseData;
    private GameObject gameObject;
    public PlaceBirdhouse(Player player, BirdhouseData birdhouseData, GameObject gameObject) {
        super(player, 1);
        this.birdhouseData = birdhouseData;
        this.gameObject = gameObject;
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.WALKABLE;
    }

    @Override
    public String getName() {
        return "Place birdhouse";
    }

    @Override
    protected void execute() {
        getMob().inventory.remove(new Item(birdhouseData.birdHouseId));
        getMob().inventory.refresh();
        getMob().birdHouseData.add(new PlayerBirdHouseData(birdhouseData, gameObject.getId(), gameObject.getPosition(), gameObject.getDirection().getId(), gameObject.getObjectType().getId()));
        getMob().send(new SendAddObject(new CustomGameObject(birdhouseData.objectData[0], gameObject.getPosition(), gameObject.getDirection(), gameObject.getObjectType())));
        PlayerSerializer.save(getMob());
        getMob().action.getCurrentAction().cancel();
    }

}
