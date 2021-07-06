package com.github.xathviar.Screen;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dongbat.jbump.*;
import com.dongbat.jbump.World;
import com.github.xathviar.*;
import com.github.xathviar.SoulsHackCore.WorldGenerator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.openmuc.jositransport.ClientTSap;
import org.openmuc.jositransport.TConnection;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Slf4j
@Data
public class GameScreen implements Screen, InputProcessor, Runnable, CollisionFilter {
    private WorldGenerator generator;
    private SoulsHackMainClass mainClass;
    private com.dongbat.jbump.World<String> world;
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
    private boolean createFinished = false;
    private TiledMapTileLayer floor;
    private boolean doRun = true;
    private Item<String> player;
    private BitmapFont font;
    private BitmapFont font2;
    private final List<Vector2> hurtBoxes = new ArrayList<>();


    public GameScreen(SoulsHackMainClass mainClass, HashMap<String, String> parameters) {
        this.mainClass = mainClass;
        this.uuid = SessionSingleton.getInstance().createClientConnectionActor(parameters.get("Playername"));
        clientTSap = new ClientTSap();
        font = mainClass.getFont();
        font2 = mainClass.getFont();
        try {
            receiveConnection = new Thread(this);
            receiveConnection.setDaemon(true);
            receiveConnection.setName("Client Connection Reciever");
//            Thread.sleep(5000);
            tConnection = clientTSap.connectTo(InetAddress.getLoopbackAddress(), 5555);
            CoreUtils.sendWithLength(tConnection, uuid);
            CoreUtils.sendWithLength(tConnection, "map");
            font.getData().setScale(4);
            receiveConnection.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean create() {
        try {
            stage = new Stage(new ScreenViewport());
            batch = new SpriteBatch();
            manager = new AssetManager(new AssetResolver(), true);
            TmxMapLoader.Parameters parameters = new TmxMapLoader.Parameters();
            parameters.flipY = true;
            manager.setLoader(TiledMap.class, new TmxMapLoader());
            manager.load("tempmap.tmx", TiledMap.class, parameters);
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
            camera.zoom = 2f;
            renderer = new OrthogonalTiledMapRenderer(map);
            MapLayers mapLayer = map.getLayers();
            floor = (TiledMapTileLayer) mapLayer.get("Floor");
        } catch (Exception _e) {
            return false;
        }
        return true;
    }

    public GameScreen() {

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        if (camera == null && !doCreate) {
            return;
        }
        if (doCreate) {
            doCreate = false;
            create();
        }
        if (stage != null) {
            camera.update();
            renderer.setView(camera);
            renderer.getBatch().begin();
            renderer.renderTileLayer(floor);
            font2.setColor(Color.RED);
            font2.draw(renderer.getBatch(), "@", camera.position.x, camera.position.y + 8);
            font2.setColor(Color.CORAL);
            if (log.isDebugEnabled()) {
                synchronized (hurtBoxes) {
                    for (Vector2 hurtBox : hurtBoxes) {
                        font2.draw(renderer.getBatch(), "#", hurtBox.x, hurtBox.y);
                    }
                }
            }
            font2.setColor(Color.WHITE);
            renderer.getBatch().end();
        }
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
            Rect playerCoordinates = world.getRect(player);
            switch (keycode) {
                case Input.Keys.A:
                    world.move(player, playerCoordinates.x - 32, playerCoordinates.y, this);
                    break;
                case Input.Keys.D:
                    world.move(player, playerCoordinates.x + 32, playerCoordinates.y, this);
                    break;
                case Input.Keys.S:
                    world.move(player, playerCoordinates.x, playerCoordinates.y - 32, this);
                    break;
                case Input.Keys.W:
                    world.move(player, playerCoordinates.x, playerCoordinates.y + 32, this);
                    break;
            }
            playerCoordinates = world.getRect(player);
            camera.position.set(playerCoordinates.x, playerCoordinates.y, 0);
            log.debug(String.format("%f.x/%f.y", playerCoordinates.x / 32, playerCoordinates.y / 32));
        } catch (NullPointerException ignored) {
        }
//        try {
//            if (Input.Keys.Q == keycode) {
//                CoreUtils.sendWithLength(tConnection, "quit");
//                doRun = false;
//                Gdx.app.exit();
//            } else {
//                CoreUtils.sendWithLength(tConnection, "keydown");
//                CoreUtils.sendWithLength(tConnection, Integer.toString(keycode));
//            }
//            return true;
//        } catch (SocketException e) {
//            log.error("Server stopped responding");
//            Gdx.app.exit();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return false;
    }

    @Override
    public void run() {
        while (doRun) {
            try {
                String s = CoreUtils.receiveWithLength(this, tConnection);
                ClientMessageHandler messageHandler = ClientMessageHandler.valueOf(s.toUpperCase(Locale.ROOT));
                final GameScreen _temp = this;
                Thread thread = new Thread(() -> {
                    try {
                        messageHandler.handleMessage(_temp);
                    } catch (Exception e) {
                        log.warn(e.getMessage(), e);
                    }
                });
                thread.start();
                thread.join(5000);
                log.info("Thread finished!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean keyUp(int keycode) {
//        try {
//            if (Input.Keys.Q == keycode) {
//                CoreUtils.sendWithLength(tConnection, "quit");
//                doRun = false;
//                Gdx.app.exit();
//            } else {
//                CoreUtils.sendWithLength(tConnection, "keyup");
//                CoreUtils.sendWithLength(tConnection, Integer.toString(keycode));
//            }
//            return true;
//        } catch (SocketException e) {
//            log.error("Server stopped responding");
//            Gdx.app.exit();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
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

    public void fillJBumpWorld() {
        boolean[][] booleanWorld = rotate(generator.createWalkableStage(), generator.createWalkableStage().length);
        this.world = new World<>();
        synchronized (hurtBoxes) {
            for (int x = 0; x < booleanWorld.length; x++) {
                for (int y = 0; y < booleanWorld[x].length; y++) {
                    System.out.printf("%3s", booleanWorld[x][y] ? "." : "#");
                    if (!booleanWorld[x][y]) {
                        world.add(new Item<String>(String.format("%dx/%dy", x, y)), x * 32, y * 32 + 32, 32, 32);
                        hurtBoxes.add(new Vector2(x * 32, y * 32 + 32));
                    }
                }
                System.out.println();
            }
        }
    }

    public static boolean[][] rotate(boolean[][] a, int n) {
        for (int i = 0; i < n - 1; i++) {
            for (int j = i; j < n - 1 - i; j++) {
                boolean temp = a[i][j];
                a[i][j] = a[n - 1 - j][i];
                a[n - 1 - j][i] = a[n - 1 - i][n - 1 - j];
                a[n - 1 - i][n - 1 - j] = a[j][n - 1 - i];
                a[j][n - 1 - i] = temp;
            }
        }
        return a;
    }


    public void generatePlayer() {
        player = new Item<>("@");
        boolean[][] booleanWorld = generator.createWalkableStage();
        int x, y;
        do {
            x = (int) (Math.random() * booleanWorld.length);
            y = (int) (Math.random() * booleanWorld.length);
        } while (!booleanWorld[x][y]);
        world.add(player, x * 32, y * 32, 32, 32);
    }

    @Override
    public Response filter(Item item, Item item1) {
        log.debug((String) item.userData);
        log.debug((String) item1.userData);
        return Response.touch;
    }
}
