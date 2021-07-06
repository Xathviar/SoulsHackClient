package com.github.xathviar.EntitySystem.Components;

import com.badlogic.ashley.core.Component;
import lombok.Data;

@Data
public class VelocityComponent implements Component {
    public float x = 0.0f;
    public float y = 0.0f;
}
