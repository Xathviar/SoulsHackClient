package com.github.xathviar.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.xathviar.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.openmuc.jositransport.ClientTSap;
import org.openmuc.jositransport.TConnection;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Locale;

@Slf4j
@Data
public class GameScreen implements Screen, InputProcessor, Runnable {
    private SoulsHackMainClass mainClass;
    private String uuid;
    private ClientTSap clientTSap;
    private TConnection tConnection;
    private Thread receiveConnection;
    private TiledMap map;
    private AssetManager manager;
    private int tileWidth, tileHeight,
            mapWidthInTiles, mapHeightInTiles,
            mapWidthInPixels, mapHeightInPixels;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer renderer;
    private Stage stage;
    private SpriteBatch batch;
    private boolean doCreate = false;
    private TiledMapTileLayer wall;
    private TiledMapTileLayer floor;
    private boolean doRun = true;


    public GameScreen(SoulsHackMainClass mainClass, HashMap<String, String> parameters) {
        this.mainClass = mainClass;
        this.uuid = SessionSingleton.getInstance().createClientConnectionActor(parameters.get("Playername"));
        clientTSap = new ClientTSap();
        try {
            receiveConnection = new Thread(this);
            receiveConnection.setDaemon(true);
            receiveConnection.setName("Client Connection Reciever");
//            Thread.sleep(5000);
            tConnection = clientTSap.connectTo(InetAddress.getLoopbackAddress(), 5555);
            CoreUtils.sendWithLength(tConnection, uuid);
            CoreUtils.sendWithLength(tConnection, "map");
            receiveConnection.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void create() {
        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);
        manager = new AssetManager(new AssetResolver(), true);
        manager.setLoader(TiledMap.class, new TmxMapLoader());
        manager.load("tempmap.tmx", TiledMap.class);
        manager.finishLoading();

        map = manager.get("tempmap.tmx", TiledMap.class);
        MapProperties properties = map.getProperties();
        tileWidth = properties.get("tilewidth", Integer.class);
        tileHeight = properties.get("tileheight", Integer.class);
        mapWidthInTiles = properties.get("width", Integer.class);
        mapHeightInTiles = properties.get("height", Integer.class);
        mapWidthInPixels = mapWidthInTiles * tileWidth;
        mapHeightInPixels = mapHeightInTiles * tileHeight;
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.x = mapWidthInPixels * .5f;
        camera.position.y = mapHeightInPixels * .35f;
//        camera.position.x = 0;
//        camera.position.y = 0;
        renderer = new OrthogonalTiledMapRenderer(map);
        MapLayers mapLayer = map.getLayers();
        floor = (TiledMapTileLayer) mapLayer.get("Floor");
        wall = (TiledMapTileLayer) mapLayer.get("Walls");
    }

    public GameScreen() {

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (camera == null && !doCreate) {
            return;
        }
        if (doCreate) {
            doCreate = false;
            create();
            return;
        }
        camera.update();
        renderer.setView(camera);
        renderer.getBatch().begin();
        renderer.renderTileLayer(floor);
        renderer.renderTileLayer(wall);
        renderer.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        if (camera != null) {
            camera.setToOrtho(true, width, height);
            stage.getViewport().update(width, height, false);
            batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
        }

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        try {
            CoreUtils.sendWithLength(tConnection, "quit");
            manager.dispose();
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tConnection.close();
    }

    @Override
    public boolean keyDown(int keycode) {
        try {
            if (Input.Keys.Q == keycode) {
                CoreUtils.sendWithLength(tConnection, "quit");
                doRun = false;
                Gdx.app.exit();
            } else {
                CoreUtils.sendWithLength(tConnection, "key");
                CoreUtils.sendWithLength(tConnection, Integer.toString(keycode));
            }
            return true;
        } catch (SocketException e) {
            log.error("Server stopped responding");
            Gdx.app.exit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void run() {
        while (doRun) {
            try {
                String s = CoreUtils.receiveWithLength(this, tConnection);
                ClientMessageHandler messageHandler = ClientMessageHandler.valueOf(s.toUpperCase(Locale.ROOT));
                messageHandler.handleMessage(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }


    public TConnection getTConnection() {
        return tConnection;
    }
}
