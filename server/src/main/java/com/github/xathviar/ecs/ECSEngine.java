package com.github.xathviar.ecs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.World;
import com.github.xathviar.ecs.components.JbumpComponent;
import com.github.xathviar.ecs.components.PlayerComponent;

public class ECSEngine extends PooledEngine {
    private final World<Entity> world;

    public ECSEngine(World<Entity> world) {
        super();
        this.world = world;
    }

    public void createPlayer(final Vector2 playerSpawnLocation) {
        final Entity player = this.createEntity();

        final PlayerComponent playerComponent = this.createComponent(PlayerComponent.class);
        final JbumpComponent jbumpComponent = this.createComponent(JbumpComponent.class);
        jbumpComponent.x = playerSpawnLocation.x;
        jbumpComponent.y = playerSpawnLocation.y;
        ComponentHelperClass.createPlayer(playerComponent);
        player.add(playerComponent);
        player.add(jbumpComponent);
    }
}
