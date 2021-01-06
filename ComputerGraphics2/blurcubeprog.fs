
//px, nx, py, ny, pz, nz
uniform sampler2DArray tex;

in vec2 v_texCoord;

layout(location=0) out vec4 slot0;   //px
layout(location=1) out vec4 slot1;   //nx
layout(location=2) out vec4 slot2;   //py
layout(location=3) out vec4 slot3;   //ny
layout(location=4) out vec4 slot4;   //pz
layout(location=5) out vec4 slot5;   //nz

void main(){
    vec4 output[6];
    for(int s=0;s<6;++s){
        float sl = float(s);
        output[s]=vec4(0,0,0,1);
        for(int i=1;i<=blurRadius;++i){
            for(float sign=-1;sign<=1;sign+=2){
                vec2 tc =  v_texCoord + sign*i*blurDeltas;
                output[s].rgb += blurWeights[i] * texture( tex, vec3( tc, sl ) ).rgb;
            }
        }
        output[s].rgb += blurWeights[0] * texture( tex, vec3(v_texCoord,s) ).rgb;
        output[s].rgb *= blurColorScale;
    }
    slot0 = output[0];
    slot1 = output[1];
    slot2 = output[2];
    slot3 = output[3];
    slot4 = output[4];
    slot5 = output[5];
}
    
    