package com.github.xathviar;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class MapSingleton {
    private static MapSingleton singleton;
    private TiledMap map;


    public static synchronized MapSingleton getInstance() {
        if (singleton == null) {
            singleton = new MapSingleton();
        }
        return singleton;
    }

    public MapSingleton() {
        AssetManager manager = new AssetManager();
        manager.setLoader(TiledMap.class, new TmxMapLoader());
        manager.load("test2.tmx", TiledMap.class);
        manager.finishLoading();
        map = manager.get("test2.tmx", TiledMap.class);
        manager.dispose();
    }

    public boolean isWalkable(int x, int y) {
        TiledMapTileLayer objects = (TiledMapTileLayer)map.getLayers().get(0);
        TiledMapTileLayer.Cell cell = objects.getCell(x,y);
        return cell.getTile().getId() != 0;
    }

}
