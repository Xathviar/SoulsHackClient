package com.github.xathviar.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;

import java.util.HashMap;

public class Asciibar implements SceneComponent {
    private int percentage;
    private String label;
    private boolean isSelected;
    private int firstKey;
    private Preferences preferences;
    private String option;

    public Asciibar(String label, String option) {
        this.label = label;
        isSelected = false;
        firstKey = -1;
        this.option = option;
        this.preferences =  Gdx.app.getPreferences("Options");
        this.percentage = preferences.getInteger(option);
    }

    @Override
    public String drawComponent() {
        return String.format("%s: <%s> %3d", label, drawBalken(), percentage) + "%";
    }

    @Override
    public void handleKeyDown(int key, HashMap<String, String> parameters) {
        if (!isSelected) {
            if (key == Input.Keys.SPACE || key == Input.Keys.ENTER) {
                isSelected = true;
            }
        } else {
            if (key == Input.Keys.ENTER || key == Input.Keys.ESCAPE || key == Input.Keys.SPACE) {
                firstKey = -1;
                isSelected = false;
            } else if (key == Input.Keys.LEFT || key == Input.Keys.A) {
                decreasePercentage();
            } else if (key == Input.Keys.RIGHT || key == Input.Keys.D) {
                increasePercentage();
            }
        }
    }

    private void decreasePercentage() {
        if (percentage > 0) {
            percentage -= 10;
            if (percentage < 0) {
                percentage = 0;
            }
        }
        preferences.putInteger(option, percentage);
    }

    private void increasePercentage() {
        if (percentage < 100) {
            percentage += 10;
            if (percentage > 100) {
                percentage = 100;
            }
        }
        preferences.putInteger(option, percentage);
    }

    @Override
    public void handleKeyTyped(char key) {
        if (!isSelected) {
            return;
        }
        if (key >= '0' && key <= '9') {
            if (firstKey == -1) {
                firstKey = Character.getNumericValue(key);
                percentage = firstKey;
            } else {
                percentage = (firstKey * 10) + Character.getNumericValue(key);
                isSelected = false;
                firstKey = -1;
            }
            preferences.putInteger(option, percentage);
        }
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    private String drawBalken() {
        StringBuilder s = new StringBuilder();
        int iterations = 10;
        int percentage = this.percentage / 10;
        for (int i = 0; i < percentage; i++) {
            s.append((char)2);
            iterations--;
        }
        while (iterations > 0) {
            s.append((char)0);
            iterations--;
        }
        return s.toString();
    }
}
