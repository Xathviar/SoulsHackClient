package com.github.xathviar;

import com.github.xathviar.Screen.GameScreen;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.FileWriter;
@Slf4j
public enum ClientMessageHandler {
    MAP {
        @Override
        public void handleMessage(GameScreen source) throws Exception {
            String map = CoreUtils.receiveWithLength(this, source.getTConnection());
            FileWriter writer = new FileWriter("tempmap.tmx");
            IOUtils.write(map, writer);
            writer.flush();
            IOUtils.closeQuietly(writer);
            source.setDoCreate(true);
        }
    },
    PING {
        @Override
        public void handleMessage(GameScreen source) throws Exception {
            log.info("Received ping");
        }
    };

    public abstract void handleMessage(GameScreen source) throws Exception;

}
