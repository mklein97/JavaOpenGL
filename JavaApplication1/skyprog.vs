#version 150
in vec3 position;

uniform mat4 viewMatrix;
uniform mat4 projMatrix;
uniform vec3 eye_pos;

out vec3 v_objPos;

void main(){
    vec4 p = vec4(position,1.0);
    v_objPos = position;
    p.xyz += eye_pos;
    p = p * viewMatrix;
    p = p * projMatrix;
    p.z = p.w;
    gl_Position = p;
}
