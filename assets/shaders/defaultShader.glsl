#type vertex

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 aColor;

uniform mat4 uProjMatrix;
uniform mat4 uViewMatrix;

out vec4 fColor;

void main() {
    fColor = aColor;
    gl_Position = uProjMatrix * uViewMatrix * vec4(aPos, 1.0);
}

#type fragment

uniform float uTime;

in vec4 fColor;

out vec4 color;

void main() {
    float noise = fract(sin(dot(fColor.xy, vec2(12.9898, 78.233))) * 43758.5453);
    color = fColor * noise;
}
