package com.github.xathviar;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.openmuc.jositransport.TConnection;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Slf4j
@Data
public class ServerConnectionHandler extends Thread implements Runnable {
    private TConnection tConnection;
    private ServerDaemon messageHandler;
    private boolean started;
    private String uuid;

    public ServerConnectionHandler() {
        super();
        started = true;
    }

    @Override
    public void run() {
        log.info("Started ServerConnectionHandler!");
        while (started) {
            try {
                if (messageHandler != null) {
                    messageHandler.handleMessage(CoreUtils.receive(tConnection), this);
                }
            } catch (InterruptedException e) {
                started = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info("Stopped ServerConnectionHandler!");
    }

    public TConnection gettConnection() {
        return tConnection;
    }

    public void settConnection(TConnection tConnection) {
        this.tConnection = tConnection;
    }

    public void handleLogin() throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocate(8192);
        tConnection.receive(byteBuffer);
        String uuid = StandardCharsets.UTF_8.decode(byteBuffer).toString();
        if (SessionSingleton.getInstance().containsActor(uuid)) {
            NamedActor actor = SessionSingleton.getInstance().getActor(uuid);
            if (actor instanceof ClientConnectionActor) {
                log.info("Client connected!");
                ((ClientConnectionActor) actor).setServerConnectionHandler(this);
                this.uuid = uuid;
            } else {
                log.warn("Invalid Actor detected");
                throw new Exception("Invalid Actor detected");
            }
        } else {
            log.warn("Unknown Session");
            throw new Exception("Unknown Session");
        }
    }

    public void close() {
        started = false;
        tConnection.close();
    }
}
