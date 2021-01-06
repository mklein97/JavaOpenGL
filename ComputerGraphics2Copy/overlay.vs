
in float position;
in vec2 texCoord;

out vec2 v_texCoord;

void main(){
    switch(int(position)){
        case 0:
            gl_Position = vec4(0,1,-1,1);
            v_texCoord = vec2(0,1);
            break;
        case 1:
            gl_Position = vec4(0,0,-1,1);
            v_texCoord = vec2(0,0);
            break;
        case 2:
            gl_Position = vec4(1,0,-1,1);
            v_texCoord = vec2(1,0);
            break;
        default:
            gl_Position = vec4(1,1,-1,1);
            v_texCoord = vec2(1,1);
    }
    gl_Position.xy *= size;
    gl_Position.xy += topLeft;
    gl_Position.xy = vec2(-1) + 2 * gl_Position.xy * screenSizeFactor;

}
