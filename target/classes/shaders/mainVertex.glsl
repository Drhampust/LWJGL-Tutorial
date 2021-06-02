#version 460 core

in vec3 position; // index 0
in vec3 color; // index 1
in vec2 textureCoord; // index 2

out vec3 passColor;
out vec2 passTextureCoord;

void main() {
    gl_Position = vec4(position, 1.0);
    passColor = color;
    passTextureCoord = textureCoord;
}