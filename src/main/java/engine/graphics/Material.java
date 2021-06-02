package engine.graphics;


import de.matthiasmann.twl.utils.PNGDecoder;
import static org.lwjgl.opengl.GL46.*;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Material {
    private int width, height;

    private int textureID;
    private String path;

    public Material(String path) {
        this.path = path;
    }

    public void create() {
        try {
            PNGDecoder decoder = new PNGDecoder(Material.class.getResourceAsStream(path));
            height = decoder.getHeight();
            width = decoder.getWidth();
            ByteBuffer buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, width * 4, PNGDecoder.Format.RGBA);
            buffer.flip();

            textureID = glGenTextures();

            System.out.println(textureID);
            System.out.println("Texture loaded");
        } catch (IOException e) {
            System.err.println("ERROR: Can't find texture at " + path);
        }
    }

    public void destroy() {
        glDeleteTextures(textureID);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public int getTextureID() {
        return textureID;
    }
}
