package com.github.xathviar;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

import java.io.File;

public class AssetResolver implements FileHandleResolver {
    @Override
    public FileHandle resolve(String s) {
        return new AssetFileHandle(new File(s));
    }

    public static class AssetFileHandle extends FileHandle {
        public AssetFileHandle(File file) {
            super(file);
        }
    }
}
