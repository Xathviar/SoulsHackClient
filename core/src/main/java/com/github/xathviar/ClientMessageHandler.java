package com.github.xathviar;

import com.dongbat.jbump.Item;
import com.github.xathviar.Screen.GameScreen;
import com.github.xathviar.SoulsHackCore.WorldGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.FileWriter;

@Slf4j
public enum ClientMessageHandler {
    MAP {
        @Override
        public void handleMessage(GameScreen source) throws Exception {
            String seed = CoreUtils.receiveWithLength(this, source.getTConnection());
            WorldGenerator generator = new WorldGenerator(128, 128, seed);
            source.setGenerator(generator);
            FileWriter writer = new FileWriter("tempmap.tmx");
            IOUtils.write(generator.generateTiledMap().toString(), writer);
            writer.flush();
            IOUtils.closeQuietly(writer);
            source.setDoCreate(true);
            source.fillJBumpWorld();
            source.generatePlayer();

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
