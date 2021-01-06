in float v_eyeDist;
out vec4 color;
void main(){
    float f=(v_eyeDist-hitherYon[0])/hitherYon[2];
    color=vec4(v_eyeDist,1.1,1);
}