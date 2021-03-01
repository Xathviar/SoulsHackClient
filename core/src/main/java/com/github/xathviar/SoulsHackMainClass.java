package com.github.xathviar;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.xathviar.Screen.*;

import java.io.IOException;
import java.util.HashMap;

public class SoulsHackMainClass extends Game implements ApplicationListener {
    private TitleScreen titleScreen;
    private OptionsScreen optionsScreen;
    private GameScreen gameScreen;
    private SelectCharacterScreen selectCharacterScreen;
    SpriteBatch batch;
    private BitmapFont font;
    Texture img;
    private static GlyphLayout glyphLayout = new GlyphLayout();
    private float fontHeight;
    private Preferences prefs;
    private ConnectScreen connectScreen;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        try {
            font = GdfBitmapFont.create((x) -> Gdx.files.internal(x), "medium.gdfa.gz");
        } catch (IOException e) {
            e.printStackTrace();
        }
        initPreferences();
        if (getPrefScaleFactor() > 0) {
            font.getData().setScale(getPrefScaleFactor());
        } else {
            fontHeight = font.getLineHeight();
            float scaleFactor = (Gdx.graphics.getHeight()) / (50f * fontHeight);
            if (scaleFactor < 1) {
                font.getData().setScale(1f);
            } else {
                font.getData().setScale((float) Math.round(scaleFactor));
            }
        }
        if (!isFullScreen()) {
            Gdx.graphics.setWindowedMode(getPrefWidth(), getPrefHeight());
        } else {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }
        setTitleScreen();
    }

    private boolean isFullScreen() {
        return prefs.getBoolean("fullscreen");
    }

    public void setTitleScreen() {
        this.titleScreen = new TitleScreen(this);
        setScreen(titleScreen);
        Gdx.input.setInputProcessor(titleScreen);
    }

    public void setOptionsScreen() {
        this.optionsScreen = new OptionsScreen(this);
        setScreen(optionsScreen);
        Gdx.input.setInputProcessor(optionsScreen);
    }

    public void setGameScreen(HashMap<String, String> parameters) {
        this.gameScreen = new GameScreen(this, parameters);
        setScreen(gameScreen);
        Gdx.input.setInputProcessor(gameScreen);
    }

    public void setSelectCharacterScreen() {
        this.selectCharacterScreen = new SelectCharacterScreen(this);
        setScreen(selectCharacterScreen);
        Gdx.input.setInputProcessor(selectCharacterScreen);
    }

    public void setConnectScreen() {
        this.connectScreen = new ConnectScreen(this);
        setScreen(connectScreen);
        Gdx.input.setInputProcessor(connectScreen);
    }


    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

    @Override
    public void dispose() {
        prefs.flush();
        super.dispose();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void resize(int width, int height) {
        if (getPrefScaleFactor() > 0) {
            font.getData().setScale(getPrefScaleFactor());
        } else {
            float scaleFactor = (height) / (50f * fontHeight);
            if (scaleFactor < 1) {
                font.getData().setScale(1f);
            } else {
                font.getData().setScale((float) Math.round(scaleFactor));
            }
            super.resize(width, height);
        }
    }

    public BitmapFont getFont() {
        return font;
    }

    public static GlyphLayout getGlyphLayout() {
        return glyphLayout;
    }

    public void initPreferences() {
        prefs = Gdx.app.getPreferences("Options");
        if (!prefs.contains("height"))
            prefs.putInteger("height", 1080);
        if (!prefs.contains("width"))
            prefs.putInteger("width", 1920);
        if (!prefs.contains("fullscreen"))
            prefs.putBoolean("fullscreen", true);
        if (!prefs.contains("volume"))
            prefs.putInteger("volume", 100);
        if (!prefs.contains("FontScaling"))
            prefs.putInteger("FontScaling", 0);
    }

    public void updatePreferences() {
        if (prefs.getBoolean("fullscreen")) {
            if (!Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
        } else {
            Gdx.graphics.setWindowedMode(getPrefWidth(), getPrefHeight());
        }
    }

    public void flushPreferences() {
        prefs.flush();
    }

    public void changeScreenResolution(int width, int height) {
        prefs.putInteger("width", width);
        prefs.putInteger("height", height);
    }

    public Preferences getPrefs() {
        return prefs;
    }

    public int getPrefWidth() {
        return prefs.getInteger("width");
    }

    public int getPrefHeight() {
        return prefs.getInteger("height");
    }

    public void setFullScreen(boolean param) {
        prefs.putBoolean("fullscreen", param);
    }

    public int getPrefScaleFactor() {
        return prefs.getInteger("FontScaling");
    }

}
