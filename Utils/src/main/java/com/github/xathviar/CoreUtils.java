package com.github.xathviar;

import lombok.extern.slf4j.Slf4j;
import org.openmuc.jositransport.TConnection;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
public class CoreUtils {

    public static String generateUUIDFromName(String name) {
        return UUID.nameUUIDFromBytes(name.getBytes()).toString();
    }

    public static String generateUUIDFromNames(String name1, String name2) {
        return UUID.nameUUIDFromBytes((name1 + ":" + name2).getBytes()).toString();
    }

    private static void send(TConnection tConnection, byte[] array) throws Exception {
        synchronized (tConnection) {
            log.debug("Sending " + new String(array));
            tConnection.send(array, 0, array.length);
        }
    }

    private static void send(TConnection tConnection, String string) throws Exception {
        send(tConnection, string.getBytes(StandardCharsets.UTF_8));
    }

    public static void sendWithLength(TConnection tConnection, String string) throws Exception {
        byte[] buffer = string.getBytes(StandardCharsets.UTF_8);
        send(tConnection, Integer.toString(buffer.length));
        send(tConnection, buffer);
    }


    private static String receive(TConnection tConnection, int bufferSize) throws Exception {
        synchronized (tConnection) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);
            tConnection.receive(byteBuffer);
            String receive = StandardCharsets.UTF_8.decode(byteBuffer).toString();
            log.debug("Received " + receive);
            return receive;
        }
    }


    public static String receiveWithLength(TConnection tConnection) throws Exception {
        int bufferSize = Integer.parseInt(receive(tConnection, 20));
        return receive(tConnection, bufferSize);
    }

}
