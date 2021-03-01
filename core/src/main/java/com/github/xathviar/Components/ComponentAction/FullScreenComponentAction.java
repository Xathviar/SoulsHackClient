package com.github.xathviar.Components.ComponentAction;

import com.github.xathviar.SoulsHackMainClass;

public class FullScreenComponentAction implements ComponentAction {
    private SoulsHackMainClass mainClass;
    private boolean isFullscreen;

    public FullScreenComponentAction(boolean isFullscreen, SoulsHackMainClass mainClass) {
        this.isFullscreen = isFullscreen;
        this.mainClass = mainClass;
    }

    @Override
    public void handleAction() {
        mainClass.setFullScreen(isFullscreen);
    }

    @Override
    public int compareTo(ComponentAction o) {
        return 0;
    }

    @Override
    public String toString() {
        return isFullscreen ? "True" : "False";
    }

    public boolean isFullscreen() {
        return isFullscreen;
    }
}
