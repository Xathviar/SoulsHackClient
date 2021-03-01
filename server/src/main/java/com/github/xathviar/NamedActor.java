package com.github.xathviar;

public abstract class NamedActor implements Actor {
    private String name;

    public NamedActor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
