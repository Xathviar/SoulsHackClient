package com.github.xathviar;

public abstract class NamedActor implements Actor {
    private String uuid;

    public NamedActor(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

}
