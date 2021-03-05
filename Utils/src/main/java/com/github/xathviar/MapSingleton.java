package com.github.xathviar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class MapSingleton {
    private static MapSingleton singleton;
    private String seed;
    private TiledMap map;


    public static synchronized MapSingleton getInstance() {
        if (singleton == null) {
            singleton = new MapSingleton();
            singleton.seed = Double.toString(300d * Math.random());
        }
        return singleton;
    }

    public MapSingleton() {
        com.github.xathviar.TmxMapLoader loader = new com.github.xathviar.TmxMapLoader(s -> Gdx.files.classpath(s));
        map = loader.load("test3.tmx");
    }

    public boolean isWalkable(int x, int y) {
        TiledMapTileLayer objects = (TiledMapTileLayer) map.getLayers().get(0);
        TiledMapTileLayer.Cell cell = objects.getCell(x, y);
        return cell.getTile().getId() != 0;
    }

    public String getSeed() {
        return seed;
    }
}