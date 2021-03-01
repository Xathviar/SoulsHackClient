package com.github.xathviar.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.github.xathviar.Components.ComponentAction.ComponentAction;
import com.github.xathviar.Components.ComponentAction.FullScreenComponentAction;
import com.github.xathviar.Components.ComponentAction.ScreenResolutionComponentAction;
import com.github.xathviar.SoulsHackMainClass;

import java.util.HashMap;
import java.util.TreeSet;

public class MultipleChoice implements SceneComponent {
    private ComponentAction[] componentActions;
    private int selected;
    private String label;
    private boolean isSelected;
    private SoulsHackMainClass mainClass;

    public MultipleChoice(SoulsHackMainClass mainClass, String label, TreeSet<ComponentAction> componentActions) {
        this.label = label;
        this.componentActions = componentActions.toArray(new ComponentAction[componentActions.size()]);
        this.mainClass = mainClass;
        getSelected();
    }

    public MultipleChoice(SoulsHackMainClass mainClass, String label, ComponentAction... componentActions) {
        this.label = label;
        this.componentActions = componentActions;
        this.mainClass = mainClass;
        getSelected();
    }

    private void getSelected() {
        int prefWidth = mainClass.getPrefWidth();
        int prefHeight = mainClass.getPrefHeight();
        for (int i = 0; i < componentActions.length; i++) {
            if (componentActions[i] instanceof ScreenResolutionComponentAction) {
                int width = ((ScreenResolutionComponentAction) componentActions[i]).getWidth();
                int height = ((ScreenResolutionComponentAction) componentActions[i]).getHeight();
                if (prefWidth == width && height == prefHeight) {
                    selected = i;
                    return;
                }
            } else if (componentActions[i] instanceof FullScreenComponentAction) {
                if (Gdx.graphics.isFullscreen()) {
                    selected = 1;
                } else {
                    selected = 0;
                }
                return;
            }
        }
        selected = 0;
    }


    @Override
    public String drawComponent() {
        return String.format("%s: <%s>", label, componentActions[selected].toString());
    }

    @Override
    public void handleKeyDown(int key, HashMap<String, String> parameters) {
        if (!isSelected) {
            if (key == Input.Keys.SPACE || key == Input.Keys.ENTER) {
                isSelected = true;
            }
        } else {
            if (key == Input.Keys.ENTER || key == Input.Keys.ESCAPE || key == Input.Keys.SPACE) {
                componentActions[selected].handleAction();
                isSelected = false;
            } else if (key == Input.Keys.LEFT || key == Input.Keys.A) {
                decreasePercentage();
            } else if (key == Input.Keys.RIGHT || key == Input.Keys.D) {
                increasePercentage();
            }
        }
    }

    private void decreasePercentage() {
        if (selected == 0) {
            selected = componentActions.length;
        }
        selected--;
    }

    private void increasePercentage() {
        selected++;
        if (selected == componentActions.length) {
            selected = 0;
        }
    }

    @Override
    public void handleKeyTyped(char key) {

    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }
}
