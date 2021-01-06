#version 150
uniform vec3 objColor;
out vec4 color;
void main(){
    
        color = vec4(objColor.r,objColor.g,objColor.b,1);
    
}