package com.github.xathviar.Connections;

import com.github.xathviar.ServerDaemon;

public class ClientConnection {
    private ServerDaemon serverDaemon;

    public void connect() {
        serverDaemon = new ServerDaemon();
        serverDaemon.startAsync();

    }
}