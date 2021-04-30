package com.github.xathviar;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.openmuc.jositransport.TConnection;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
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
            int maxBuffer = 60000;
            if (array.length > maxBuffer) {
                List<byte[]> byteList = new ArrayList<>();
                for (int i = 0; i < array.length; i += maxBuffer) {
                    if (array.length - i > maxBuffer) {
                        byteList.add(ArrayUtils.subarray(array, i, i + maxBuffer));
                    } else {
                        byteList.add(ArrayUtils.subarray(array, i, array.length));
                    }
                }
                send(tConnection, byteList);
                return;
            }
            tConnection.send(array, 0, array.length);
        }
    }

    private static void send(TConnection tConnection, List<byte[]> array) throws Exception {
        synchronized (tConnection) {
            log.debug("Sending " + new String(array.toString()));
            List<Integer> offsets = new ArrayList<>();
            List<Integer> lengths = new ArrayList<>();
            for (byte[] bytes : array) {
                offsets.add(0);
                lengths.add(bytes.length);
            }
            tConnection.send(array, offsets, lengths);
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


    private static String receive(Object sync, TConnection tConnection, int bufferSize) throws Exception {
        synchronized (sync) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);
            tConnection.receive(byteBuffer);
            String receive = StandardCharsets.UTF_8.decode(byteBuffer).toString();
            log.debug("Received " + receive);
            return receive;
        }
    }


    public static String receiveWithLength(Object sync, TConnection tConnection) throws Exception {
        int bufferSize = Integer.parseInt(receive(sync, tConnection, 20));
        return receive(sync, tConnection, bufferSize + 512);
    }

}
