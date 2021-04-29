package com.github.xathviar.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.github.xathviar.ServerDaemon;
import com.github.xathviar.SoulsHackMainClass;

public class DesktopLauncher {
    public static void main(String[] args) {
        ServerDaemon serverDaemon = new ServerDaemon();
        serverDaemon.startAsync();
        new LwjglApplication(new SoulsHackMainClass());
    }
}