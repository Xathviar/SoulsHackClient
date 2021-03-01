package com.github.xathviar.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.xathviar.Components.*;
import com.github.xathviar.SoulsHackMainClass;

import java.util.HashMap;


public class ConnectScreen implements Screen, InputProcessor {
    private SoulsHackMainClass mainClass;
    private Stage stage;
    private BitmapFont font;
    private SpriteBatch batch;
    private SelectionHelper helper;

    public ConnectScreen(SoulsHackMainClass mainClass) {
        this.mainClass = mainClass;
        create();
    }

    public ConnectScreen ()  {}

    private void create() {
        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);
        this.font = mainClass.getFont();
        helper = new SelectionHelper(new TextInput("Playername", false)
                , new TextInput("Password", false)
                , new ChangeScreenButton("Submit", mainClass, new GameScreen())
                , new ChangeScreenButton("Go Back", mainClass, new TitleScreen()));
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(v);
        stage.draw();
        batch.begin();
        SelectionHelper.drawSoulsHackLogo(batch, font, stage.getWidth(), stage.getHeight());
        helper.drawAllFonts(batch, font, stage.getWidth(), stage.getHeight() / 2);
        batch.end();
    }

    @Override
    public void resize(int i, int i1) {

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

    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("Playername", helper.getTextInput("Playername"));
        parameters.put("Password", helper.getTextInput("Password"));
        helper.manageKey(keycode, parameters);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        helper.getSelectedActor().handleKeyTyped(character);
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }
}
