package engine.io;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class Input {
    private boolean[] keys = new boolean[GLFW_KEY_LAST];
    private boolean[] mouseButtons = new boolean[GLFW_MOUSE_BUTTON_LAST];
    private double mouseX, mouseY;
    private double scrollX, scrollY;


    private GLFWKeyCallback keyboard;
    private GLFWCursorPosCallback mouseMove;
    private GLFWMouseButtonCallback mouseButton;
    private GLFWScrollCallback mouseScroll;

    public Input() {
        keyboard = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods){
                keys[key] = (action == GLFW_PRESS);
            }
        };

        mouseMove = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                mouseX = xpos;
                mouseY = ypos;
            }
        };

        mouseButton = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                mouseButtons[button] = (action == GLFW_PRESS);
            }
        };

        mouseScroll = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double offsetX, double offsetY) {
                scrollX += offsetX;
                scrollY += offsetY;
            }
        };
    }

    public boolean isKeyDown(int key) {
        return keys[key];
    }

    public boolean isMouseButtonDown(int button) {
        return mouseButtons[button];
    }

    public void destroy() {
        keyboard.free();
        mouseMove.free();
        mouseButton.free();
        mouseScroll.free();
    }

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public GLFWKeyCallback getKeyboard() {
        return keyboard;
    }

    public GLFWCursorPosCallback getMouseMove() {
        return mouseMove;
    }

    public GLFWMouseButtonCallback getMouseButton() {
        return mouseButton;
    }

    public GLFWScrollCallback getMouseScroll() {
        return mouseScroll;
    }
}

