package com.github.xathviar.Components;

import com.github.xathviar.Components.ComponentAction.ComponentAction;

public abstract class NamedComponentAction implements ComponentAction {
    private String name;

    public NamedComponentAction(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
