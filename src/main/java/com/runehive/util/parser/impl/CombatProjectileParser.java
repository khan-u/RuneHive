package com.runehive.util.parser.impl;

import com.google.gson.JsonObject;
import com.runehive.game.Animation;
import com.runehive.game.Graphic;
import com.runehive.game.Projectile;
import com.runehive.game.world.entity.combat.CombatImpact;
import com.runehive.game.world.entity.combat.magic.MagicImpact;
import com.runehive.game.world.entity.combat.projectile.CombatProjectile;
import com.runehive.game.world.entity.combat.ranged.RangedEffects;
import com.runehive.util.parser.GsonParser;

public class CombatProjectileParser extends GsonParser {

    public CombatProjectileParser() {
        super("def/combat/projectile_definitions", false);
    }

    @Override
    public void parse(JsonObject reader){
        String name = reader.get("name").getAsString();

        int hitDelay = -1;
        if (reader.has("hit-delay")) {
            hitDelay = reader.get("hit-delay").getAsInt();
        }

        int hitsplatDelay = -1;
        if (reader.has("hitsplat-delay")) {
            hitsplatDelay = reader.get("hitsplat-delay").getAsInt();
        }

        int maxHit = -1;
        if (reader.has("max-hit")) {
            maxHit = reader.get("max-hit").getAsInt();
        }

        CombatImpact effect = null;
        if (reader.has("magic-effect")) {
            String effectName = reader.get("magic-effect").getAsString();
            effect = MagicImpact.valueOf(effectName).getEffect();
        } else if (reader.has("ranged-effect")) {
            String effectName = reader.get("ranged-effect").getAsString();
            effect = RangedEffects.valueOf(effectName).getEffect();
        }

        Animation animation = null;
        if (reader.has("animation")) {
            animation = builder.fromJson(reader.get("animation"), Animation.class);
        }

        Graphic start = null;
        if (reader.has("start")) {
            start = builder.fromJson(reader.get("start"), Graphic.class);
        }

        Graphic end = null;
        if (reader.has("end")) {
            end = builder.fromJson(reader.get("end"), Graphic.class);
        }

        Projectile projectile = null;
        if (reader.has("projectile")) {
            projectile = builder.fromJson(reader.get("projectile"), Projectile.class);
        }

        CombatProjectile.definitions.put(name, new CombatProjectile(name, maxHit, effect, animation, start, end, projectile, hitDelay, hitsplatDelay));
    }

}
