package com.github.xathviar;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum MessageHandler {
    KEY {
        @Override
        public void handleMessage(ServerConnectionHandler source) throws Exception {
            String key = CoreUtils.receiveWithLength(this, source.gettConnection());
            log.info("Received Key=" + key);
        }
    },
    MAP {
        @Override
        public void handleMessage(ServerConnectionHandler source) throws Exception {
            String message = MapHelper.getWorldFromSeed(MapSingleton.getInstance().getSeed());
            CoreUtils.sendWithLength(source.gettConnection(), "map");
            CoreUtils.sendWithLength(source.gettConnection(), message);
            log.info("Sending Map with Seed (" + MapSingleton.getInstance().getSeed() + ")");
        }
    },
    QUIT {
        @Override
        public void handleMessage(ServerConnectionHandler source) throws Exception {
            source.close();
        }
    };


    public abstract void handleMessage(ServerConnectionHandler source) throws Exception;
}
