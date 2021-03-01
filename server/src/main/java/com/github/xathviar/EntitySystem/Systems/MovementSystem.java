package com.github.xathviar.EntitySystem.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.github.xathviar.EntitySystem.Components.PositionComponent;
import com.github.xathviar.EntitySystem.Components.VelocityComponent;
import com.github.xathviar.EntitySystem.Mapper;

public class MovementSystem extends IteratingSystem {

    public MovementSystem() {
        super(Family.all(PositionComponent.class, VelocityComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = Mapper.pm.get(entity);
        VelocityComponent velocity = Mapper.vm.get(entity);

        float lastX = position.x;
        float lastY = position.y;

        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;

        //TODO Map und so Zeugs
//        if (map[x][y] != walkable) {
//            position.x = lastX;
//            position.y = lastY;
//        }
    }
}
