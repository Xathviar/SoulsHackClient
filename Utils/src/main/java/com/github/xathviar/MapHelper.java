package com.github.xathviar;

import com.github.xathviar.SoulsHackCore.WorldGenerator;

public class MapHelper {

    public static String getWorldFromSeed(String seed) {
        WorldGenerator gen = new WorldGenerator(120, 120, seed);
        return gen.generateTiledMap().toString();
    }
}
