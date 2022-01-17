#type vertex
#version 460

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 aColor;
layout (location = 2) in vec2 aTexCoords;
layout (location = 3) in float aTexId;
layout (location = 4) in float aEntityId;

uniform mat4 uProjMatrix;
uniform mat4 uViewMatrix;

out vec4 fColor;
out vec2 fTexCoords;
out float fTexId;
out float fEntityId;

void main() {
    fColor = aColor;
    fTexCoords = aTexCoords;
    fTexId = aTexId;
    fEntityId = aEntityId;
    gl_Position = uProjMatrix * uViewMatrix * vec4(aPos, 1.0);
}

#type fragment
#version 460

in vec4 fColor;
in vec2 fTexCoords;
in float fTexId;
in float fEntityId;

uniform sampler2D uTextures[8];

out vec3 color;

void main() {
    vec4 textureColor = vec4(1,1,1,1);
    if(fTexId > 0) {
        int id = int(fTexId);
        textureColor = fColor * texture(uTextures[id], fTexCoords);
    }

    if(textureColor.a < 0.5) {
        discard;
    } else {
        color = vec3(fEntityId,fEntityId,fEntityId);
    }
}
