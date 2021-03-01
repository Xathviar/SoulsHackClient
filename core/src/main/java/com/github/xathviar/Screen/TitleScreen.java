package com.github.xathviar.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.xathviar.Components.ChangeScreenButton;
import com.github.xathviar.Components.SelectionHelper;
import com.github.xathviar.SoulsHackMainClass;

import java.util.HashMap;


public class TitleScreen implements Screen, InputProcessor {
    private SoulsHackMainClass mainClass;
    private Stage stage;
    private BitmapFont font;
    private SpriteBatch batch;
    private SelectionHelper helper;

    public TitleScreen(SoulsHackMainClass mainClass) {
        this.mainClass = mainClass;
        create();
    }

    public TitleScreen() {

    }

    public void create() {
        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);
        this.font = mainClass.getFont();
        helper = new SelectionHelper(new ChangeScreenButton("Connect to Server", mainClass, new ConnectScreen())
                , new ChangeScreenButton("Options", mainClass, new OptionsScreen())
                , new ChangeScreenButton("Quit Game", mainClass, null));
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
    }

    public void render(float t) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(t);
        stage.draw();
        batch.begin();
        SelectionHelper.drawSoulsHackLogo(batch, font, stage.getWidth(), stage.getHeight());
        helper.drawAllFonts(batch, font, stage.getWidth(), stage.getHeight() / 2);
        batch.end();
    }

    @Override
    public void show() {

    }

    public void dispose() {
        stage.dispose();
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
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        helper.manageKey(keycode, new HashMap<String, String>());
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        helper.getSelectedActor().handleKeyTyped(character);
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
