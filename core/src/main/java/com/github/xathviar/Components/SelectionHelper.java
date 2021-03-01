package com.github.xathviar.Components;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.github.xathviar.SoulsHackMainClass;

import java.util.HashMap;

import static com.badlogic.gdx.graphics.Color.CORAL;

public class SelectionHelper {
    private SceneComponent[] sceneComponent;
    private int selected;

    public SelectionHelper(SceneComponent... selections) {
        this.sceneComponent = selections;
    }

    public void selectAbove() {
        if (selected == 0) {
            selected = sceneComponent.length - 1;
        } else {
            selected--;
        }
    }

    public void selectBelow() {
        if (selected == sceneComponent.length - 1) {
            selected = 0;
        } else {
            selected++;
        }
    }

    public boolean isSelected(int current) {
        return current == selected;
    }

    public String getOptionAt(int index) {
        return sceneComponent[index].drawComponent();
    }

    public int getSelected() {
        return selected;
    }

    public void drawFontAt(Batch batch, BitmapFont font, float width, float y, int index) {
        font.setColor(Color.WHITE);
        if (selected == index) {
            font.setColor(Color.CORAL);
            if (sceneComponent[index].isSelected()) {
                font.setColor(Color.GREEN);
            }
        }
        SoulsHackMainClass.getGlyphLayout().setText(font, sceneComponent[index].drawComponent());
        font.draw(batch, SoulsHackMainClass.getGlyphLayout(), (width / 2) - (SoulsHackMainClass.getGlyphLayout().width / 2), y);
        font.setColor(Color.WHITE);
    }

    public void drawAllFonts(Batch batch, BitmapFont font, float width, float y) {
        for (int i = 0; i < sceneComponent.length; i++) {
            SoulsHackMainClass.getGlyphLayout().setText(font, sceneComponent[0].drawComponent());
            drawFontAt(batch, font, width, y, i);
            y -= SoulsHackMainClass.getGlyphLayout().height;
        }
    }

    public static void drawSoulsHackLogo(Batch batch, BitmapFont font, float width, float height) {
        font.setColor(CORAL);
        height -= font.getLineHeight() * 5;
        SoulsHackMainClass.getGlyphLayout().setText(font, "  _________            .__           ___ ___                __    ");
        float calulatedWidth = (width / 2) - (SoulsHackMainClass.getGlyphLayout().width / 2);
        font.draw(batch, "  _________            .__           ___ ___                __    ", calulatedWidth, height);
        height -= font.getLineHeight();
        font.draw(batch, " /   _____/ ____  __ __|  |   ______/   |   \\_____    ____ |  | __", calulatedWidth, height);
        height -= font.getLineHeight();
        font.draw(batch, " \\_____  \\ /  _ \\|  |  \\  |  /  ___/    ~    \\__  \\ _/ ___\\|  |/ /", calulatedWidth, height);
        height -= font.getLineHeight();
        font.draw(batch, " /        (  <_> )  |  /  |__\\___ \\\\    Y    // __ \\\\  \\___|    < ", calulatedWidth, height);
        height -= font.getLineHeight();
        font.draw(batch, "/_______  /\\____/|____/|____/____  >\\___|_  /(____  /\\___  >__|_ \\", calulatedWidth, height);
        height -= font.getLineHeight();
        font.draw(batch, "        \\/                       \\/       \\/      \\/     \\/     \\/", calulatedWidth, height);
        font.setColor(Color.WHITE);
    }

    public SceneComponent getSelectedActor() {
        return sceneComponent[selected];
    }

    public void manageKey(int keycode, HashMap<String, String> parameters) {
        if (sceneComponent[selected].isSelected()) {
            sceneComponent[selected].handleKeyDown(keycode, parameters);
            return;
        }
        if (keycode == Input.Keys.UP || keycode == Input.Keys.W) {
            this.selectAbove();
        } else if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
            this.selectBelow();
        } else {
            sceneComponent[selected].handleKeyDown(keycode, parameters);
        }
    }

    public String getTextInput(String name) {
        for (SceneComponent component : sceneComponent) {
            if (component instanceof TextInput) {
                 if (((TextInput) component).getLabel().equals(name)) {
                     return ((TextInput) component).getInput();
                 }
            }
        }
        return null;
    }
}
