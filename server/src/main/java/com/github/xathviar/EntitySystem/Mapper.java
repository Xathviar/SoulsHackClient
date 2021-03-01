package com.github.xathviar.EntitySystem;

import com.badlogic.ashley.core.ComponentMapper;
import com.github.xathviar.EntitySystem.Components.CharacterComponent;
import com.github.xathviar.EntitySystem.Components.PositionComponent;
import com.github.xathviar.EntitySystem.Components.VelocityComponent;

public class Mapper {
    public static final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    public static final ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
    public static final ComponentMapper<CharacterComponent> cm = ComponentMapper.getFor(CharacterComponent.class);
}
