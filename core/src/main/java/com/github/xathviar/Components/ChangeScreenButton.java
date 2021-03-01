package com.github.xathviar.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.github.xathviar.Screen.*;
import com.github.xathviar.SoulsHackMainClass;

import java.util.HashMap;

public class ChangeScreenButton implements SceneComponent {
    private String label;
    private SoulsHackMainClass soulsHackMainClass;
    private Screen screen;

    public ChangeScreenButton(String label, SoulsHackMainClass soulsHackMainClass, Screen screen) {
        this.label = label;
        this.soulsHackMainClass = soulsHackMainClass;
        this.screen = screen;
    }

    @Override
    public String drawComponent() {
        return label;
    }

    @Override
    public void handleKeyDown(int key, HashMap<String, String> parameters) {
        if (key == Input.Keys.ENTER || key == Input.Keys.SPACE) {
            if (screen instanceof GameScreen) {
                if (parameters.get("Playername").length() == 0) {
                    return;
                }
                soulsHackMainClass.setGameScreen(parameters);
            } else if (screen instanceof OptionsScreen) {
                soulsHackMainClass.setOptionsScreen();
            } else if (screen instanceof SelectCharacterScreen) {
                soulsHackMainClass.setSelectCharacterScreen();
            } else if (screen instanceof TitleScreen) {
                soulsHackMainClass.setTitleScreen();
            } else if (screen instanceof ConnectScreen) {
                soulsHackMainClass.setConnectScreen();
            } else {
                Gdx.app.exit();
            }
        }
    }

    @Override
    public void handleKeyTyped(char key) {
    }

    @Override
    public boolean isSelected() {
        return false;
    }


}
