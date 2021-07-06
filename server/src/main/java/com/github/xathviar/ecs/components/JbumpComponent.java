package com.github.xathviar.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.dongbat.jbump.Item;
import lombok.Data;

@Data
public class JbumpComponent implements Component {
    Item<Entity> item;
    public float x;
    public float y;
    public float width;
    public float height;
}
