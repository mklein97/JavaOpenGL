
in vec3 position;       //pixel coordinate
in vec2 texCoord;       //pixel size

out vec2 v_texCoord;
flat out int v_sliceNumber;

//1/w  and 1/h
//uniform vec2 screenSizeFactor;

void main(){
    vec4 p = vec4(position.xy,-1.0,1.0);
    
    //NDC's -> normalized device coordinates
    //convert from 0...w-1 -> -1...1
    //convert from 0...h-1 -> -1...1
    p.xy *= screenSizeFactor;
    p.xy -= vec2(0.5);
    p.xy *= 2.0;
    gl_Position = p;
    v_texCoord = texCoord;
    v_sliceNumber = int(position.z);
}
