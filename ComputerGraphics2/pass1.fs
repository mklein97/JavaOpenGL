//rgb = position. a=unused
layout(location=0) out vec4 slot0;
//rgb = normal. a=unused
layout(location=1) out vec4 slot1;
//rgba = diffuse tex color. 
layout(location=2) out vec4 slot2;
//rgb = emission tex color.
layout(location=3) out vec4 slot3;
//rgb = spec tex color. a=shininess
layout(location=4) out vec4 slot4;
uniform sampler2DArray textures[MAX_TEXTURES];

in vec3 v_normal;
in vec3 v_tangent;
in vec3 v_worldPosition;
in vec3 v_localPosition;
flat in int v_materialIndex;
in vec2 v_texCoord;


void main() {
	//set some defaults for diffuse, emission, specular, and bump textures
    vec4 texcolor = vec4(1,1,1,1);
    vec3 etexcolor = vec3(0,0,0);
    vec3 stexcolor = vec3(1,1,1);
    vec3 btexcolor = vec3(0.5,0.5,1.0);
	
	//get the indices of the various textures
    int mdidx = materialDiffuseTextureIndex[v_materialIndex];
    int meidx = materialEmissiveTextureIndex[v_materialIndex];
    int msidx = materialSpecularTextureIndex[v_materialIndex];
    int mbidx = materialBumpTextureIndex[v_materialIndex];
	
	vec3 texCoord = vec3(v_texCoord,texFrameNumber);
	
	vec4 allTex[MAX_TEXTURES];
    allTex[0] = texture(textures[0],texCoord);
    allTex[1] = texture(textures[1],texCoord);
    allTex[2] = texture(textures[2],texCoord);
    allTex[3] = texture(textures[3],texCoord);
    allTex[4] = texture(textures[4],texCoord);
    #if MAX_TEXTURES != 5
        #error FIX ME
    #endif

    if( mdidx != -1 )
        texcolor = allTex[mdidx];
    if( meidx != -1 )
        etexcolor = allTex[meidx].rgb;
    if( msidx != -1 )
        stexcolor = allTex[msidx].rgb;
    if( mbidx != -1 )
        btexcolor = allTex[mbidx].rgb;
	
	//perform bump mapping
    vec3 b = 2.0 * (btexcolor - vec3(0.5));
    b = normalize(b);
    vec3 N = normalize(v_normal);
    vec3 T = normalize(v_tangent);
    vec3 B = cross(N,T);
    N = vec3(   dot( b, vec3(T.x,B.x,N.x) ),
                dot( b, vec3(T.y,B.y,N.y) ),
                dot( b, vec3(T.z,B.z,N.z) ) );
    N = normalize(vec4(N,0)*worldMatrix).xyz;
	
	slot0.rgb = v_worldPosition;
	slot0.a = v_localPosition.x;
	slot1.rgb = N;
	slot1.a = v_localPosition.y;
	slot2.rgb = texcolor.rgb * diffuse[v_materialIndex];
	slot2.a = noiseOn;
	slot3.rgb = etexcolor;
	slot3.a = v_localPosition.z;
	slot4.rgb = stexcolor * specular[v_materialIndex];
	slot4.a = shininess[v_materialIndex];
}