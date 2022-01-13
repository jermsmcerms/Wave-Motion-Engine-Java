#type vertex

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 aColor;
layout (location = 2) in vec2 aTexCoords;
layout (location = 3) in float aTexId;

uniform mat4 uProjMatrix;
uniform mat4 uViewMatrix;

out vec4 fColor;
out vec2 fTexCoords;
out float fTexId;

void main() {
    fColor = aColor;
    fTexCoords = aTexCoords;
    fTexId = aTexId;
    gl_Position = uProjMatrix * uViewMatrix * vec4(aPos, 1.0);
}

#type fragment

in vec4 fColor;
in vec2 fTexCoords;
in float fTexId;

uniform sampler2D uTextures[8];

out vec4 color;

void main() {
    if(fTexId > 0) {
        int id = int(fTexId);
        color = fColor * texture(uTextures[id], fTexCoords);
//        color = vec4(fTexCoords, 0, 1);
    } else {
        color = fColor;
    }
}
