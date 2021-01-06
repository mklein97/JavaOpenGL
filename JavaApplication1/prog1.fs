#version 150

flat in int v_materialIndex;
in vec3 v_normal;
in vec3 v_posWorld;
in vec2 v_texCoord;
in vec3 v_tangent;
out vec4 color;
#define MAX_MATERIALS 16
uniform vec4 diffuseColors[MAX_MATERIALS];
uniform vec4 specularColors[MAX_MATERIALS];
//uniform int textureIndices[MAX_MATERIALS];
uniform int diffuseTextureIndices[MAX_MATERIALS];
uniform int specularTextureIndices[MAX_MATERIALS];
uniform int emissiveTextureIndices[MAX_MATERIALS];
uniform int bumpTextureIndices[MAX_MATERIALS];
uniform vec3 eyepos;
uniform float shiny;
#define MAX_TEXTURES 8
uniform sampler2DArray allTextures[MAX_TEXTURES];
uniform float texSlice;
uniform mat4 worldMatrix;

struct Light {
    vec3 position;
    vec3 color;
    float positional;
    float A0,A1,A2;
};

#define NUM_LIGHTS 1
uniform Light lights[NUM_LIGHTS];

//uniform vec3 lightPosition[NUM_LIGHTS];
//uniform vec3 lightColor[NUM_LIGHTS];

void main(){
    vec3 totald = vec3(0,0,0);
    vec3 totals = vec3(0,0,0);

    int dti = int(diffuseTextureIndices[v_materialIndex]);
    int sti = int(specularTextureIndices[v_materialIndex]);
    int eti = int(emissiveTextureIndices[v_materialIndex]);
    int bti = int(bumpTextureIndices[v_materialIndex]);
    vec4 diffuseTexcolor = vec4(1,1,1,1);
    vec4 specularTexcolor = vec4(1,1,1,1);
    vec4 emissiveTexcolor = vec4(0,0,0,1);
    vec3 bumpTexcolor = vec3(0.5,0.5,1);
    vec3 texCoord = vec3(v_texCoord, texSlice);

    vec4 tempcolor[MAX_TEXTURES];
    tempcolor[0] = texture(allTextures[0], texCoord);

    tempcolor[1] = texture(allTextures[1], texCoord);

    tempcolor[2] = texture(allTextures[2], texCoord);
    tempcolor[3] = texture(allTextures[3], texCoord);
    tempcolor[4] = texture(allTextures[4], texCoord);

    tempcolor[5] = texture(allTextures[5], texCoord);

    tempcolor[6] = texture(allTextures[6], texCoord);

    tempcolor[7] = texture(allTextures[7], texCoord);
    if( dti != -1 )
        diffuseTexcolor = tempcolor[dti];
    if( sti != -1 )
        specularTexcolor = tempcolor[sti];
    if(eti != -1)
        emissiveTexcolor = tempcolor[eti];
    if( bti != -1 )
        bumpTexcolor = tempcolor[bti].rgb;


    bumpTexcolor = (2*bumpTexcolor) - vec3(1);

    vec3 N = normalize(v_normal);   //object space
    vec3 T = normalize(v_tangent);
    vec3 B = normalize(cross(N,T));

    N = vec3(
            dot( bumpTexcolor, vec3(T.x,B.x,N.x) ),
            dot( bumpTexcolor, vec3(T.y,B.y,N.y) ),
            dot( bumpTexcolor, vec3(T.z,B.z,N.z) ) );
    
    
    N = (vec4(N,0) * worldMatrix).xyz;


    N = normalize(N);

    vec3 V = normalize(eyepos - v_posWorld);
    vec3 L, R;
    for(int i=0;i<NUM_LIGHTS;++i){
        L = normalize(lights[i].position- lights[i].positional * v_posWorld);
        R = reflect(-L,N);
	float dp = clamp(dot(N,L),0.0,1.0);
	dp = max(dp,0.0);
	float sp = 0;
	sp = pow(max(dot(V,R),0.0), shiny);
        //if(dp > 0)
	    //sp = dot(V,R);
	//float d = distance(lights[i].position, v_posWorld);
	//float f = 1/(lights[i].A2*d*d + lights[i].A1*d + lights[i].A0);
	//f = min(1.0f,f);
	//dp = f*dp;
	totald = totald + dp * lights[i].color;
	totals = totals + sp * lights[i].color;
    }

    totald = min(vec3(1), totald);
    totals = min(vec3(1), totals);
    
    //float diffuse = max(dot(N,L),0.0);
    //float specular = max(pow( dot(V,R), 16),0.0);
    vec3 dc = (diffuseColors[v_materialIndex].rgb) * totald* diffuseTexcolor.rgb;
    vec3 sc = (specularColors[v_materialIndex].rgb) * totals* specularTexcolor.rgb;
    color.rgb = dc + sc + emissiveTexcolor.rgb;
    color.a = 1.0;
    //color.rgb = v_tangent;
    //color= diffuseColors[v_materialIndex];
    
}