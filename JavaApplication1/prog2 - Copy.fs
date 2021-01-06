#version 150

#extension GL_NV_gpu_shader5: warn

flat in int v_materialIndex;
in vec2 v_texCoord;
in vec3 v_normal;
in vec3 v_worldPos;

out vec4 color;


#define MAX_MATERIALS 16
uniform vec4 diffuseColors[MAX_MATERIALS];
uniform vec4 specularColors[MAX_MATERIALS];
uniform int diffuseTextureIndices[MAX_MATERIALS];
uniform int specularTextureIndices[MAX_MATERIALS];
uniform int emissiveTextureIndices[MAX_MATERIALS];
uniform vec3 eye_pos;
uniform float shiny;

#define MAX_TEXTURES 8
uniform sampler2DArray allTextures[MAX_TEXTURES];
uniform float texSlice;

struct Light {
	vec3 position;
	vec3 color;
	float positional;
	float A0, A1, A2;
};

#define NUM_LIGHTS 1
uniform Light lights[NUM_LIGHTS];

void main(){
	vec3 L, R;
    vec3 N = normalize(v_normal);
    vec3 V = normalize(eye_pos-v_worldPos);
	vec3 totald = vec3(0,0,0);
	vec3 totals = vec3(0,0,0);
	for (int i=0; i < NUM_LIGHTS; ++i){
		L = normalize(lights[i].position - lights[i].positional * v_worldPos);
		R = reflect(-L,N);
		float dp = clamp(dot(N,L),0.0,1.0);
		dp = max(dp,0.0);
		float sp = 0;
		sp = pow(max(dot(V,R),0.0), shiny);
		totald = totald + dp * lights[i].color;
		totals = totals + sp * lights[i].color;
	}
	
    int dti = int(diffuseTextureIndices[v_materialIndex]);
    int eti = int(emissiveTextureIndices[v_materialIndex]);
    int sti = int(specularTextureIndices[v_materialIndex]);
    
    vec4 diffuseTexcolor = vec4(1,1,1,1);
    vec4 specularTexcolor = vec4(1,1,1,1);
    vec4 emissiveTexcolor = vec4(1,0,0,1);
    vec3 texCoord = vec3(v_texCoord,texSlice);
	
    //#ifdef GL_NV_gpu_shader5
		//if( dti != -1 )
           // diffuseTexcolor = texture( allTextures[dti], texCoord );
        //if( sti != -1 )
           // specularTexcolor = texture( allTextures[sti], texCoord );
		//if( eti != -1 )
           // specularTexcolor = texture( allTextures[eti], texCoord );
   
        vec4 tempcolor[MAX_TEXTURES];
        tempcolor[0] = texture(allTextures[0], texCoord);
        tempcolor[1] = texture(allTextures[1], texCoord);
        tempcolor[2] = texture(allTextures[2], texCoord);
        tempcolor[3] = texture(allTextures[3], texCoord);
		tempcolor[4] = texture(allTextures[4], texCoord);
        tempcolor[5] = texture(allTextures[5], texCoord);
        tempcolor[6] = texture(allTextures[6], texCoord);
        tempcolor[7] = texture(allTextures[7], texCoord);
//        for(int i=0;i<MAX_TEXTURES;i++)
//            tempcolor[i] = texture(allTextures[ti], v_texCoord);
		if( dti != -1 )
            diffuseTexcolor = tempcolor[dti];
        if( sti != -1 )
            specularTexcolor = tempcolor[sti];
		if( eti != -1 )
            emissiveTexcolor = tempcolor[eti];

	
	totald = min(vec3(1), totald);
	totals = min(vec3(1), totals);
	
	vec3 dc = (diffuseColors[v_materialIndex].rgb) * totald * diffuseTexcolor.rgb;
	vec3 sc = (specularColors[v_materialIndex].rgb) * totals * specularTexcolor.rgb;
	color.rgb = dc + sc + emissiveTexcolor.rgb;
	if (color.rgb == vec3(0,0,0))
		color.a = 0.0;
	else
		color.a = 1.0;
}
