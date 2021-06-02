package engine.io;

import engine.maths.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.opengl.GL46.*; // If problems change to GL11
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    // Window Properties
    private int height, width, heightCopy, widthCopy;
    private String title;
    private GLFWWindowSizeCallback sizeCallback;
    private GLFWVidMode videoMode;
    private boolean resized;
    private boolean fullscreen;
    private int[] windowPosX = new int[1], windowPosY = new int[1];


    private boolean LMB_TOGGLE = false;

    // Window reference
    private long windowHandle;

    // Input reference
    private Input input;

    // Key Toggle
    private boolean[] keyToggle = new boolean[GLFW_KEY_LAST];
    // Button Toggle
    private boolean[] mouseButtonToggle = new boolean[GLFW_MOUSE_BUTTON_LAST];

    // FPS Vars
    private static final int SECOND_IN_NANO = 1000000000;
    // How many times per second will we sample FPS higher = less accuracy
    private static final double SAMPLE_RATE = 1;
    public int frames;
    private long timeNano_OLD;
    private long sampleRateNano;

    //GL
    private Vector3f background = new Vector3f();

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public void create() {
        if ( !GLFW.glfwInit() ) {
            System.err.println("ERROR: GLFW wasn't initialized!");
            return;
        }

        input = new Input();

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        windowHandle = GLFW.glfwCreateWindow(width, height, title, (fullscreen ? glfwGetPrimaryMonitor() : NULL), NULL);

        if ( windowHandle == NULL ) {
            System.err.println("ERROR: Window wasn't created!");
            return;
        }

        videoMode = glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        if ( videoMode == null ) {
            System.err.println("ERROR: Video Mode failed to fetch primary monitor!");
            return;
        }

        windowPosX[0] = (videoMode.width() - width) / 2;
        windowPosY[0] = (videoMode.height() - height) / 2;

        // Center the window
        glfwSetWindowPos(
                windowHandle,
                windowPosX[0],
                windowPosY[0]
        );

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowHandle);
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        createCallbacks();

        // Turn on V-sync
        glfwSwapInterval(1);


        long currentTime = System.nanoTime();
        timeNano_OLD = currentTime;
        sampleRateNano = currentTime;

        // Make the window visible
        glfwShowWindow(windowHandle);
    }

    /**
     * Updates the window.
     */
    public void update() {
        if (resized) {
            glViewport(0,0, width, height);
            resized = false;

        }
        glClearColor(background.getX(), background.getY(), background.getZ(), 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glfwPollEvents();
        calculateFrames();
        if (input.isKeyDown(GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(windowHandle, true);
        }

        if (input.isMouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            mouseButtonToggle[GLFW_MOUSE_BUTTON_LEFT] = true;
        }
        if (!input.isMouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && LMB_TOGGLE) {
            //TODO: Mouse Click code here code here
            System.out.println("X: " + input.getMouseX() + ", Y: " + input.getMouseY());;
            // toggles fullscreen boolean
            mouseButtonToggle[GLFW_MOUSE_BUTTON_LEFT] = false;
        }

        if (input.isKeyDown(GLFW_KEY_F11)) {
            keyToggle[GLFW_KEY_F11] = true;
        }
        if (!input.isKeyDown(GLFW_KEY_F11) && keyToggle[GLFW_KEY_F11]) {
            // Toggles the fullscreen environment
            setFullscreen(!isFullscreen());

            // toggles the F11 toggle boolean back to default
            keyToggle[GLFW_KEY_F11] = false;
        }

    }

    /**
     * Places the next frame into the buffer of the GPU to be rendered.
     */
    public void swapBuffers() {
        glfwSwapBuffers(windowHandle);
    }

    /**
     * Returns if the Window should close.
     * True if it should be closed otherwise False.
     */
    public boolean shouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }

    /**
     *
     */
    private void createCallbacks() {
        // create a method for when the window gets resized
        sizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int w, int h) {
                width = w;
                height = h;
                resized = true;
            }
        };

        glfwSetKeyCallback(windowHandle, input.getKeyboard());
        glfwSetCursorPosCallback(windowHandle, input.getMouseMove());
        glfwSetMouseButtonCallback(windowHandle, input.getMouseButton());
        glfwSetScrollCallback(windowHandle, input.getMouseScroll());
        glfwSetWindowSizeCallback(windowHandle, sizeCallback);
    }

    /**
     * Destroys all connections this window has.
     */
    public void destroy() {
        input.destroy();
        sizeCallback.free();
        // in case window is not closed close it here
        glfwSetWindowShouldClose(windowHandle, true);
        glfwDestroyWindow(windowHandle);
        glfwTerminate();
    }

    /**
     *
     */
    public void setBackgroundColor(float r, float g, float b) {
        background.set(r, g, b);
    }

    /**
     *
     */
    private void calculateFrames() {
        long currentTime = System.nanoTime(); // gets the time when updates start
        if ( (currentTime - sampleRateNano) - (SECOND_IN_NANO/SAMPLE_RATE) >= 0) {
            sampleRateNano = currentTime;
            double delta = currentTime - timeNano_OLD; // difference in time between start and end of sampling.
            frames = (int) ((SECOND_IN_NANO) / (delta)); // Second in Nano / time passed
            glfwSetWindowTitle(windowHandle, title + " | FPS: " + frames);

        }
        timeNano_OLD = currentTime;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getTitle() {
        return title;
    }

    /**
     * This method cannot be called until windowHandel has been created!
     * @param fullscreen
     */
    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
        resized = true;
        if(windowHandle == NULL) {
            System.err.println("ERROR: Invalid windowHandle");
            return;
        }
        if (this.fullscreen) {
            System.out.println("Entering Fullscreen");
            widthCopy = width;
            heightCopy = height;
            GLFW.glfwGetWindowPos(windowHandle, windowPosX, windowPosY);
            GLFW.glfwSetWindowMonitor(windowHandle, GLFW.glfwGetPrimaryMonitor(), 0, 0, videoMode.width(), videoMode.height(), 10);
        } else {
            System.out.println("Leaving Fullscreen");
            width = widthCopy;
            height = heightCopy;
            GLFW.glfwSetWindowMonitor(windowHandle, 0, windowPosX[0], windowPosY[0], width, height, 10);
        }
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public long getWindowHandle() {
        return windowHandle;
    }

}
