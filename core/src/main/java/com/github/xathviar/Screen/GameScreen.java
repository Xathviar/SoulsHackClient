package com.github.xathviar.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.github.xathviar.CoreUtils;
import com.github.xathviar.ServerDaemon;
import com.github.xathviar.SessionSingleton;
import com.github.xathviar.SoulsHackMainClass;
import lombok.extern.slf4j.Slf4j;
import org.openmuc.jositransport.ClientTSap;
import org.openmuc.jositransport.TConnection;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;

@Slf4j
public class GameScreen implements Screen, InputProcessor {
    private SoulsHackMainClass mainClass;
    private String uuid;
    private ClientTSap clientTSap;
    private TConnection tConnection;

    public GameScreen(SoulsHackMainClass mainClass, HashMap<String, String> parameters) {
        this.mainClass = mainClass;
        this.uuid = SessionSingleton.getInstance().createClientConnectionActor(parameters.get("Playername"));
        ServerDaemon serverDaemon = new ServerDaemon();
        serverDaemon.startAsync();
        clientTSap = new ClientTSap();
        try {
            Thread.sleep(5000);
            tConnection = clientTSap.connectTo(InetAddress.getLoopbackAddress(), 5555);
            CoreUtils.send(tConnection, uuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GameScreen() {

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

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
            CoreUtils.send(tConnection, "quit");
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
                CoreUtils.send(tConnection, "quit");
            } else {
                CoreUtils.send(tConnection, "key");
                CoreUtils.send(tConnection, Integer.toString(keycode));
            }
        } catch (SocketException e) {
            log.error("Server stopped responding");
            Gdx.app.exit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("KeyDown " + keycode);

        return false;
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
}
