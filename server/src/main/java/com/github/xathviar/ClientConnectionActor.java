package com.github.xathviar;

public class ClientConnectionActor extends NamedActor{
    private ServerConnectionHandler serverConnectionHandler;

    public ClientConnectionActor(String name) {
        super(name);
    }

    public void setServerConnectionHandler(ServerConnectionHandler serverConnectionHandler) {
        this.serverConnectionHandler = serverConnectionHandler;
    }

    public ServerConnectionHandler getServerConnectionHandler() {
        return serverConnectionHandler;
    }

    public void closeServerConnectionHandler() {
        serverConnectionHandler.close();
    }
}
