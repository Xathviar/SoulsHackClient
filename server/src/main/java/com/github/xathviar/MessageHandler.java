package com.github.xathviar;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum MessageHandler {
    KEY {
        @Override
        public void handleMessage(ServerConnectionHandler source) throws Exception {
            String key = CoreUtils.receive(source.gettConnection());
            log.info("Received Key=" + key);
        }
    };


    public abstract void handleMessage(ServerConnectionHandler source) throws Exception;
}
