package com.github.xathviar.Components.ComponentAction;

import com.github.xathviar.Components.NamedComponentAction;
import com.github.xathviar.SoulsHackMainClass;

public class ScreenResolutionComponentAction extends NamedComponentAction {
    private int width;
    private int height;
    private SoulsHackMainClass soulsHackMainClass;

    public ScreenResolutionComponentAction(String name, int width, int height, SoulsHackMainClass soulsHackMainClass) {
        super(name);
        this.width = width;
        this.height = height;
        this.soulsHackMainClass = soulsHackMainClass;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public void handleAction() {
        soulsHackMainClass.changeScreenResolution(width, height);
    }

    @Override
    public int compareTo(ComponentAction o) {
        if (o instanceof ScreenResolutionComponentAction) {
            ScreenResolutionComponentAction other = (ScreenResolutionComponentAction) o;
            int o_width = other.width;
            int o_height = other.height;
            if (o_width == width) {
                if (o_height == height) {
                    return 0;
                } else {
                    return Integer.compare(height, o_height);
                }
            } else {
                return Integer.compare(width, o_width);
            }
        }
        return -1;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}