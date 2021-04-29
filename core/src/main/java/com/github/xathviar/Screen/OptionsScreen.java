package com.github.xathviar.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.xathviar.Components.*;
import com.github.xathviar.Components.ComponentAction.ComponentAction;
import com.github.xathviar.Components.ComponentAction.FullScreenComponentAction;
import com.github.xathviar.Components.ComponentAction.ScreenResolutionComponentAction;
import com.github.xathviar.SoulsHackMainClass;

import java.util.HashMap;
import java.util.TreeSet;

public class OptionsScreen implements Screen, InputProcessor {
    private SoulsHackMainClass mainClass;
    private Stage stage;
    private BitmapFont font;
    private SpriteBatch batch;
    private SelectionHelper helper;
    private TreeSet<ComponentAction> componentActionSet;
    private TextInput fontScaling;

    public OptionsScreen(SoulsHackMainClass mainClass) {
        this.mainClass = mainClass;
        create();
    }

    public OptionsScreen() {

    }

    public void create() {
        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);
        this.font = mainClass.getFont();
        componentActionSet = new TreeSet<>();
        for (Graphics.DisplayMode displayMode : Gdx.graphics.getDisplayModes()) {
            componentActionSet.add(new ScreenResolutionComponentAction(String.format("%dx%d", displayMode.width, displayMode.height), displayMode.width, displayMode.height, mainClass));
        }
        fontScaling = new TextInput("FontScaling", true);
        fontScaling.setInput(Integer.toString(mainClass.getPrefScaleFactor()));
        helper = new SelectionHelper(new Asciibar("Sound", "volume")
                , new MultipleChoice(mainClass, "Resolution", componentActionSet)
                , new MultipleChoice(mainClass, "Fullscreen", new FullScreenComponentAction(false, mainClass), new FullScreenComponentAction(true, mainClass))
                , fontScaling
                , new SubmitButton(mainClass, fontScaling)
                , new ChangeScreenButton("Go Back", mainClass, new TitleScreen()));
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
        batch.begin();
        SelectionHelper.drawSoulsHackLogo(batch, font, stage.getWidth(), stage.getHeight());
        helper.drawAllFonts(batch, font, stage.getWidth(), stage.getHeight() / 2);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
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
