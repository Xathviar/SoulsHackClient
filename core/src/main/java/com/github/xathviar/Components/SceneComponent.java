package com.github.xathviar.Components;

import java.util.HashMap;

public interface SceneComponent {
    public String drawComponent();
    public void handleKeyDown(int key, HashMap<String, String> parameters);
    public void handleKeyTyped(char key);
    public boolean isSelected();

}
