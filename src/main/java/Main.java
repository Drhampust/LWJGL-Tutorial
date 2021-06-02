import engine.graphics.*;
import engine.io.Window;
import engine.maths.Vector2f;
import engine.maths.Vector3f;

/**
 * this was made using the tutorial from URL:
 * https://www.youtube.com/watch?v=fW19iG9Hkrk&list=PLaWuTOi9sDeomi2umQ7N8Lqs-GtE1H4-b
 */
public class Main implements Runnable{

    public Thread gameThread;
    public Window window;

    public final int WIDTH = 1000, HEIGHT = 600;

    public Renderer renderer;
    public Shader shader;
    public Mesh mesh = new Mesh(new Vertex[] {
            new Vertex(new Vector3f(-0.5f,0.5f, 0.0f), new Vector3f(1.0f,0.0f, 0.0f), new Vector2f(0.0f,0.0f)),
            new Vertex(new Vector3f(-0.5f,-0.5f,0.0f), new Vector3f(0.0f,1.0f, 0.0f), new Vector2f(0.0f,1.0f)),
            new Vertex(new Vector3f(0.5f, -0.5f,0.0f), new Vector3f(0.0f,0.0f, 1.0f), new Vector2f(1.0f,1.0f)),
            new Vertex(new Vector3f(0.5f, 0.5f, 0.0f), new Vector3f(1.0f,1.0f, 0.0f), new Vector2f(1.0f,0.0f))
    }, new int[] {
            0, 1, 2,
            0, 3, 2
    }, new Material("/textures/image.png"));


    public static void main(String[] args) {
        new Main().start();
    }

    public void start() {
        gameThread = new Thread(this,"game");
        gameThread.start();
    }

    /**
     * initializes the game.
     */
    public void init() {
        System.out.println("Initializing the Game Thread...");
        window = new Window(WIDTH, HEIGHT, "Game");
        shader = new Shader("/shaders/mainVertex.glsl","/shaders/mainFragment.glsl");
        renderer = new Renderer(shader);
        window.setBackgroundColor(0.2f, 0.2f, 0.2f);
        window.create();
        mesh.create();
        shader.create();
        System.out.println("Game Thread Initialized");
    }

    /**
     * This Threads run method.
     * Overrides the default Runnable run method.
     */
    public  void run() {
        init();
        while(!window.shouldClose()) {
            update();
            render();
        }
        close();
        System.out.println("Window has been closed!");
    }

    /**
     * Updates the game state of the game.
     */
    private void update() {
        window.update();
    }

    /**
     * Renders the game in its current state.
     */
    private void render() {
        renderer.renderMesh(mesh);
        window.swapBuffers();
    }

    private void close(){
        window.destroy();
        mesh.destroy();
        shader.destroy();
    }
}
