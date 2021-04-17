package com.github.xathviar;

import com.badlogic.ashley.core.Engine;
import com.github.xathviar.SoulsHackCore.WorldGenerator;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.common.util.concurrent.AbstractScheduledService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.lwjgl.Sys;
import org.openmuc.jositransport.ServerTSap;
import org.openmuc.jositransport.TConnection;
import org.openmuc.jositransport.TConnectionListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Slf4j
@Data
public class ServerDaemon extends AbstractScheduledService implements TConnectionListener {
    private ServerTSap serverTSap;
    private Engine engine;
    private long lastUpdatetime;
    private List<ServerConnectionHandler> serverConnectionHandlers;

    @Override
    protected void startUp() throws Exception {
        serverTSap = new ServerTSap(5555, this);
        serverTSap.startListening();
        serverConnectionHandlers = new ArrayList<>();
        lastUpdatetime = -1;
        engine = new Engine();
    }

    @Override
    protected void shutDown() throws Exception {
        serverTSap.stopListening();
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedRateSchedule(0L, 500L, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void runOneIteration() throws Exception {
        if (lastUpdatetime < 0) {
            lastUpdatetime = System.nanoTime();
        }
        long currentUpdateTime = System.nanoTime();
        //TODO Watch out for the nano Seconds!
        engine.update(currentUpdateTime - lastUpdatetime);
        lastUpdatetime = currentUpdateTime;
    }


    @Override
    public void connectionIndication(TConnection tConnection) {
        ServerConnectionHandler connectionHandler = new ServerConnectionHandler();
        connectionHandler.settConnection(tConnection);
        try {
            connectionHandler.setMessageHandler(this);
            connectionHandler.handleLogin();
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
                log.error("Message - " + message + " - has no Handler", e);
                SessionSingleton.getInstance().deleteActor(source.getUuid());
                serverConnectionHandlers.remove(source);
                source.close();
            } catch (Exception e) {
                log.error(e.toString(), e);
            }
        }
    }
}
