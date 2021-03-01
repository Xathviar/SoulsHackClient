package com.github.xathviar;

import com.google.common.util.concurrent.AbstractIdleService;
import lombok.extern.slf4j.Slf4j;
import org.openmuc.jositransport.ServerTSap;
import org.openmuc.jositransport.TConnection;
import org.openmuc.jositransport.TConnectionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
public class ServerDaemon extends AbstractIdleService implements TConnectionListener {
    private ServerTSap serverTSap;
    private List<ServerConnectionHandler> serverConnectionHandlers;

    @Override
    protected void startUp() throws Exception {
        serverTSap = new ServerTSap(5555, this);
        serverTSap.startListening();
        serverConnectionHandlers = new ArrayList<>();
    }

    @Override
    protected void shutDown() throws Exception {
        serverTSap.stopListening();
    }


    @Override
    public void connectionIndication(TConnection tConnection) {
        ServerConnectionHandler connectionHandler = new ServerConnectionHandler();
        connectionHandler.settConnection(tConnection);
        try {
            connectionHandler.handleLogin();
            connectionHandler.setMessageHandler(this);
            connectionHandler.start();
            serverConnectionHandlers.add(connectionHandler);
        } catch (Exception e) {
            connectionHandler.gettConnection().close();
            log.error(String.format("Connection %s was unexpectedly closed! Login failed.", connectionHandler.gettConnection().toString()));
            e.printStackTrace();
        }
    }

    @Override
    public void serverStoppedListeningIndication(IOException e) {
        for (ServerConnectionHandler serverConnectionHandler : serverConnectionHandlers) {
            serverConnectionHandler.interrupt();
        }
    }

    public static void main(String[] args) {
        ServerDaemon serverDaemon = new ServerDaemon();
        serverDaemon.startAsync();
    }

    public void handleMessage(String message, ServerConnectionHandler source) {
        if ("quit".equals(message)) {
            SessionSingleton.getInstance().deleteActor(source.getUuid());
            serverConnectionHandlers.remove(source);
            source.close();
        } else {
            try {
                MessageHandler messageHandler = MessageHandler.valueOf(message.toUpperCase(Locale.ROOT));
                messageHandler.handleMessage(source);
            } catch (IllegalArgumentException e) {
                log.error("Message - " + message + " - has no Handler");
                SessionSingleton.getInstance().deleteActor(source.getUuid());
                serverConnectionHandlers.remove(source);
                source.close();
            } catch (Exception e) {
                log.error(e.toString());
            }
        }
    }
}
