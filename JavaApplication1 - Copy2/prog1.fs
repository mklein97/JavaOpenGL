#version 150

#extension GL_NV_gpu_shader5: warn

#define lightColor vec3(1,1,1)

flat in int v_materialIndex;
in vec2 v_texCoord;
in vec3 v_worldPos;
in vec3 v_normal;

out vec4 color;


#define MAX_MATERIALS 16
uniform vec4 diffuseColors[MAX_MATERIALS];
uniform vec4 specularColors[MAX_MATERIALS];
uniform int diffuseTextureIndices[MAX_MATERIALS];
uniform int specularTextureIndices[MAX_MATERIALS];
uniform int emissiveTextureIndices[MAX_MATERIALS];

#define MAX_TEXTURES 4
uniform sampler2DArray allTextures[MAX_TEXTURES];
uniform float texSlice;

uniform vec3 lightPos;
uniform vec3 eyePos;

void main(){
    color = diffuseColors[v_materialIndex];
    int dti = int(diffuseTextureIndices[v_materialIndex]);
    int eti = int(emissiveTextureIndices[v_materialIndex]);
    int sti = int(specularTextureIndices[v_materialIndex]);
    

    vec4 diffuseTexcolor = vec4(1,1,1,1);
    vec4 specularTexcolor = vec4(1,1,1,1);
    vec4 emissiveTexcolor = vec4(0,0,0,1);
    vec3 texCoord = vec3(v_texCoord,texSlice);

    #ifdef GL_NV_gpu_shader5
        if( dti != -1 )
            diffuseTexcolor = texture( allTextures[dti], texCoord );
        if( sti != -1 )
            specularTexcolor = texture( allTextures[sti], texCoord );
    #else
        vec4 tempcolor[MAX_TEXTURES];
        tempcolor[0] = texture(allTextures[0], texCoord);
        tempcolor[1] = texture(allTextures[1], texCoord);
        tempcolor[2] = texture(allTextures[2], texCoord);
        tempcolor[3] = texture(allTextures[3], texCoord);
        if( dti != -1 )
            diffuseTexcolor = tempcolor[dti];
        if( sti != -1 )
            specularTexcolor = tempcolor[sti];
    #endif
    
    vec3 N = normalize(v_normal);
    vec3 L = normalize(lightPos - v_worldPos);
    vec3 V = vec3(eyePos - v_worldPos);
    vec3 R = reflect(-L,N);
    float dp = dot(N,L);
    dp = max(dp,0.0);
    float sp = dot(V,R);
    sp = max(sp,0.0);
    sp = pow( sp, 32.0); //shininess[v_materialIndex]
    color.rgb = lightColor * dp * diffuseColors[v_materialIndex].rgb * diffuseTexcolor.rgb + 
                lightColor * sp * specularColors[v_materialIndex].rgb * specularTexcolor.rgb +
                emissiveTexcolor.rgb;

    color.a = 1.0;
}
