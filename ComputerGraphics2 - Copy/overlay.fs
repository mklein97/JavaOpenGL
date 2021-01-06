
uniform sampler2DArray textures;

in vec2 v_texCoord;
out vec4 color;

void main(){
    vec4 t = texture( textures, vec3(v_texCoord,texFrameNumber) );
    color = t;
    color.a *= alphaFactor;
}
