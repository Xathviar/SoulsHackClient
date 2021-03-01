package com.github.xathviar;

import java.util.HashMap;

public class SessionSingleton {
    private static SessionSingleton sessionSingleton;
    private HashMap<String, NamedActor> actorMap = new HashMap<>();

    public static synchronized SessionSingleton getInstance() {
        if (sessionSingleton == null) {
            sessionSingleton = new SessionSingleton();
        }
        return sessionSingleton;
    }

    public void registerActor(String key, NamedActor value) {
        actorMap.put(key, value);
    }

    public NamedActor getActor(String key) {
        return actorMap.get(key);
    }

    public void deleteActor(String key) {
        actorMap.remove(key);
    }

    public boolean containsActor(String key) {
        return actorMap.containsKey(key);
    }

    public String createClientConnectionActor (String userName) {
        String uuid = CoreUtils.generateUUIDFromName(userName);
        NamedActor namedActor = new ClientConnectionActor(uuid);
        actorMap.put(namedActor.getName(), namedActor);
        return uuid;
    }
}
