package com.github.xathviar;


import com.github.xathviar.SoulsHackCore.WorldGenerator;

public class MapHelper {

    public static String getWorldFromSeed(String seed) {
        WorldGenerator gen = null;
        int i = 0;
        try {

            for (i = 0; ; i += (int) (Math.random() * 1000)) {
                gen = new WorldGenerator(120, 120, Integer.toString(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Seed: " + Integer.toString(i));
            throw e;
        }
    }
}
