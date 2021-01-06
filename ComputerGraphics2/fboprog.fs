uniform sampler2DArray tex;

in vec2 v_texCoord;
out vec4 color;


void main(){
    vec3 texc = vec3(v_texCoord,0);
 //   texc.y += 0.01 * sin(texc.x);
//    texc.x += 0.05 * cos(v_texCoord.x);
    color = texture(tex,texc);
}
    
    
