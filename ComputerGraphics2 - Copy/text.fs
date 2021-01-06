

in vec2 v_texCoord;
flat in int v_sliceNumber;

out vec4 color;

uniform sampler2DArray fontTexture;
//uniform vec3 fontColor;

void main(){
    vec4 c = texelFetch(fontTexture, ivec3(v_texCoord, v_sliceNumber), 0) ;
    color = vec4( fontColor, c.r );
}
