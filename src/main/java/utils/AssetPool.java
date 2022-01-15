package utils;

import renderer.Shader;
import renderer.Texture;
import wavemotion.components.SpriteSheet;

import java.io.File;
import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    private static Map<String, Shader> shaderPool = new HashMap<>();
    private static Map<String, Texture> texturePool = new HashMap<>();
    private static Map<String, SpriteSheet> spriteSheetPool = new HashMap<>();

    public static Shader getShader(String path) {
        File file = new File(path);
        if(shaderPool.containsKey(file.getAbsolutePath())) {
            return shaderPool.get(file.getAbsolutePath());
        }

        Shader shader = new Shader(path);
        shader.compileAndLink();
        AssetPool.shaderPool.put(file.getAbsolutePath(), shader);
        return shader;
    }

    public static Texture getTexture(String path) {
        File file = new File(path);
        if(texturePool.containsKey(file.getAbsolutePath())) {
            return texturePool.get(file.getAbsolutePath());
        }

        Texture texture = new Texture();
        texture.init(path);
        AssetPool.texturePool.put(file.getAbsolutePath(), texture);
        return texture;
    }

    public static void addSpriteSheet(String name, SpriteSheet spriteSheet) {
        File file = new File(name);
        if(!spriteSheetPool.containsKey(file.getAbsolutePath())) {
            AssetPool.spriteSheetPool.put(file.getAbsolutePath(), spriteSheet);
        }
    }

    public static SpriteSheet getSpriteSheet(String name) throws InvalidKeyException {
        File file = new File(name);
        if(!AssetPool.spriteSheetPool.containsKey(file.getAbsolutePath())) {
            throw new InvalidKeyException("Sprite sheet " + name + " not in pool");
        }

        // TODO: replace default value parameter with "empty" sprite sheet
        return AssetPool.spriteSheetPool.getOrDefault(file.getAbsolutePath(), null);
    }
}
