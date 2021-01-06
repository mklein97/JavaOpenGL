#version 150

flat in int v_materialIndex;
out vec4 color;

#define MAX_MATERIALS 16
uniform vec4 diffuseColors[MAX_MATERIALS];

void main(){
    color = diffuseColors[v_materialIndex];
}
