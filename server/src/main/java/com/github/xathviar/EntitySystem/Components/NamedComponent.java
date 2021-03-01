package com.github.xathviar.EntitySystem.Components;

import com.badlogic.ashley.core.Component;
import lombok.Data;

@Data
public abstract class NamedComponent implements Component {
    private String name;
    private String uuid;

    public NamedComponent(String uuid) {
        this.uuid = uuid;
    }

    public NamedComponent() {
    }
}
