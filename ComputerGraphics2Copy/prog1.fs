
//all the textures we might be using for the meshes
uniform sampler2DArray textures[MAX_TEXTURES];

//texture of skybox (for fog)
uniform samplerCube skyTexture;

//data from the vertex shader
in vec3 v_normal;
in vec3 v_worldPosition;
flat in int v_materialIndex;
in vec2 v_texCoord;
in vec3 v_tangent;

out vec4 color;

void main(){

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
    

    //if the object is mostly transparent, discard the fragment.
    //We do this early so we can bail out quickly for transparent
    //fragments.
    color.a = texcolor.a * alphaFactor;
    if( color.a < 0.02 )
        discard;


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


    //apply lighting calculations

    //vector from surface to the light source
    vec3 L = normalize(lightPosition.xyz - v_worldPosition*lightPosition.w);

    //vector from surface to viewer
    vec3 V = normalize(eyePos - v_worldPosition);

    //direction of maximum reflected (specular) light
    vec3 R = reflect(-L,N);

    //amount of diffuse light
    float dp = dot(L,N);
    dp = max(0.0,dp);

    //amount of specular light
    float sp=0.0;
    if( dp > 0.0 ){
        sp = dot(V,R);
        sp = max(0.0,sp);
        sp = pow(sp,shininess[v_materialIndex]);
    }

    //compute object color
	color.rgb = vec3(0,0,0);
		color.rgb =  dp * diffuse[v_materialIndex] * lightColor * texcolor.rgb;
	color.rgb += sp * specular[v_materialIndex] * lightColor * stexcolor;
	color.rgb += etexcolor * 6;
	

    //apply fog
    float fogDistance = distance(eyePos,v_worldPosition);
    float fogAmt = clamp( (fogDistance - fogNear) / fogDelta, 0, 1 );
    color.rgb = mix( color.rgb, fogColor, fogAmt );

}
