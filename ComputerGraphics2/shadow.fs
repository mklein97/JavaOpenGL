in float v_eyeDist;
out vec4 color;
void main(){
    float f=(v_eyeDist-hitherYon[0])/hitherYon[2];
    color=vec4(f,f,f,1.0);
}