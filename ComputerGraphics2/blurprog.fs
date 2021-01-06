uniform sampler2DArray tex;

in vec2 v_texCoord;
out vec4 color;

void main(){
    color = vec4(0);
    for(int i=1;i<=blurRadius;++i){
        color += blurWeights[i] * texture( tex, vec3( v_texCoord - i * blurDeltas , blurReadLayer ) );
        color += blurWeights[i] * texture( tex, vec3( v_texCoord + i * blurDeltas , blurReadLayer ) );
    }
    color += blurWeights[0] * texture( tex, vec3( v_texCoord , blurReadLayer ) );
    color.rgb *= blurColorScale;
    color.a=1.0;
}