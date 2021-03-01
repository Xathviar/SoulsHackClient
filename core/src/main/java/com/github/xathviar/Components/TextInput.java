package com.github.xathviar.Components;


import com.badlogic.gdx.Input;

import java.util.HashMap;


public class TextInput implements SceneComponent {
    private String input;
    private String label;
    private boolean isSelected;
    private boolean onlyDigits;

    public TextInput(String label, boolean onlyDigits) {
        this.label = label;
        this.input = "";
        isSelected = false;
        this.onlyDigits = onlyDigits;
    }

    public void deleteLastLetter() {
        if (input.length() > 0) {
            input = input.substring(0, input.length() - 1);
        }
    }


    public String drawComponent() {
        return label + ": " + input;
    }

    @Override
    public void handleKeyDown(int key, HashMap<String, String> parameters) {
        if (!isSelected) {
            if (key == Input.Keys.SPACE || key == Input.Keys.ENTER) {
                isSelected = true;
            }
        } else {
            if (key == Input.Keys.ENTER || key == Input.Keys.ESCAPE) {
                isSelected = false;
            }
        }
    }

    @Override
    public void handleKeyTyped(char key) {
        if (!isSelected) {
            return;
        }
        if (key == 8) {
            deleteLastLetter();
        }
        if (onlyDigits) {
            if (key >= '0' && key <= '9') {
                input = Character.toString(key);
            }
        } else if (key >= 32) {
            input += key;
        }
    }


    public boolean isSelected() {
        return isSelected;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getLabel() {
        return label;
    }
}
