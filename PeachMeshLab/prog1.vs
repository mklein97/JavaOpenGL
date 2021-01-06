#version 150
in vec3 position;
in float materialIndex;

flat out int v_materialIndex;

uniform mat4 worldMatrix;

void main(){
    vec4 p = vec4(position,1.0);
    p = p * worldMatrix;
    gl_Position = p;
    v_materialIndex = int(materialIndex);
}
