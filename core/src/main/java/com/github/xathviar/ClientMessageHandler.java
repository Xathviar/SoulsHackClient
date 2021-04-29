package com.github.xathviar;

import com.github.xathviar.Screen.GameScreen;
import org.apache.commons.io.IOUtils;

import java.io.FileWriter;

public enum ClientMessageHandler {
    MAP {
        @Override
        public void handleMessage(GameScreen source) throws Exception {
            String map = CoreUtils.receiveWithLength(this, source.getTConnection());
            FileWriter writer = new FileWriter("core/assets/tempmap.tmx");
            IOUtils.write(map, writer);
            writer.flush();
            IOUtils.closeQuietly(writer);
            source.setDoCreate(true);
        }
    };

    public abstract void handleMessage(GameScreen source) throws Exception;

}
