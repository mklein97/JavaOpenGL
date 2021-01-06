#version 150

uniform samplerCube tex;

in vec3 v_objPos;

out vec4 color;

void main(){
    color = texture( tex , v_objPos );
}
