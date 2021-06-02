#version 460 core

in vec3 passColor;
in vec2 passTextureCoord;

out vec4 outColor;

uniform sampler2D tex; // texture pointer

void main() {
    outColor = texture(tex, passTextureCoord);

}
