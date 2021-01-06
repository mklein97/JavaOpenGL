#version 150

in vec3 position;

//in vec3 scaler;
in float materialIndex;

in vec2 texCoord;
in vec3 normalIndex;
in vec3 normal;
in vec3 tangent;

flat out int v_materialIndex;

out vec3 v_normal;
out vec3 v_posWorld;
out vec2 v_texCoord;
out vec3 v_tangent;

uniform mat4 worldMatrix;

uniform mat4 viewMatrix;
uniform mat4 projMatrix;
uniform mat4 scaleMatrix;


void main(){

    vec4 p = vec4(position,1.0);

    p = p * scaleMatrix;
    p = p * worldMatrix;

    v_posWorld = p.xyz;
    p = p * viewMatrix;
    p = p * projMatrix;
    gl_Position = p;

    v_materialIndex = int(materialIndex);

    v_normal = (vec4(normalIndex,0)* worldMatrix).xyz;
    v_tangent = tangent;
    v_texCoord = texCoord;

}