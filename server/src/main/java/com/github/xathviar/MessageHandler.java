package com.github.xathviar;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum MessageHandler {
    KEY {
        @Override
        public void handleMessage(ServerConnectionHandler source) throws Exception {
            String key = CoreUtils.receiveWithLength(source.gettConnection());
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
    };


    public abstract void handleMessage(ServerConnectionHandler source) throws Exception;
}
