package com.github.xathviar.Screen;

import com.badlogic.gdx.Input;
import com.github.xathviar.Components.SceneComponent;
import com.github.xathviar.Components.TextInput;
import com.github.xathviar.SoulsHackMainClass;

import java.util.HashMap;

public class SubmitButton implements SceneComponent {
    private SoulsHackMainClass mainClass;
    private SceneComponent[] sceneComponents;

    public SubmitButton(SoulsHackMainClass mainClass, SceneComponent... sceneComponents) {
        this.mainClass = mainClass;
        this.sceneComponents = sceneComponents;
    }

    @Override
    public String drawComponent() {
        return "Save Changes";
    }

    @Override
    public void handleKeyDown(int key, HashMap<String, String> parameters) {
        if (key == Input.Keys.SPACE || key == Input.Keys.ENTER) {
            for (SceneComponent sceneComponent : sceneComponents) {
                if (sceneComponent instanceof TextInput) {
                    mainClass.getPrefs().putInteger(((TextInput) sceneComponent).getLabel(), Integer.parseInt(((TextInput) sceneComponent).getInput()));
                }
            }
            mainClass.flushPreferences();
            mainClass.updatePreferences();
            mainClass.setTitleScreen();
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
