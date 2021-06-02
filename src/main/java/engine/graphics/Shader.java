package engine.graphics;

import engine.utils.FileUtils;

import static org.lwjgl.opengl.GL46.*;

public class Shader {
    private String vertexFile, fragmentFile;
    private int vertexID, fragmentID, programID;

    public Shader(String vertexPath, String fragmentPath) {
        vertexFile = FileUtils.loadAsString(vertexPath);
        fragmentFile = FileUtils.loadAsString(fragmentPath);
    }

    public void create() {
        programID = glCreateProgram();
        vertexID = glCreateShader(GL_VERTEX_SHADER);

        glShaderSource(vertexID, vertexFile);
        glCompileShader(vertexID);

        if (glGetShaderi(vertexID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("ERROR: Vertex Shader: " + glGetShaderInfoLog(vertexID));
            return;
        }

        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(fragmentID, fragmentFile);
        glCompileShader(fragmentID);

        if (glGetShaderi(fragmentID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("ERROR: Fragment Shader: " + glGetShaderInfoLog(fragmentID));
            return;
        }

        glAttachShader(programID, vertexID);
        glAttachShader(programID, fragmentID);

        glLinkProgram(programID);
        if(glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE) {
            System.err.println("ERROR: Program Linking: " + glGetProgramInfoLog(programID));
        }

        glValidateProgram(programID);
        if(glGetProgrami(programID, GL_VALIDATE_STATUS) == GL_FALSE) {
            System.err.println("ERROR: Program Validation: " + glGetProgramInfoLog(programID));
        }

        glDeleteShader(vertexID);
        glDeleteShader(fragmentID);
    }

    public void bind(){
        glUseProgram(programID);
    }

    public void unbind(){
        glUseProgram(0);
    }

    public void destroy(){
        glDeleteProgram(programID);
    }
}
