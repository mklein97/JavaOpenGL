in vec3 position;
in vec3 normal;
in float materialIndex;
in vec2 texCoord;
in vec3 tangent;

out vec3 v_normal;
out vec3 v_tangent;
out vec3 v_worldPosition;
flat out int v_materialIndex;
out vec2 v_texCoord;
out vec3 v_localPosition;

void main(){
    vec4 p = vec4(position,1.0);
    p = p * worldMatrix;
    v_worldPosition = p.xyz;
    p = p * viewMatrix;
    p = p * projMatrix;
    gl_Position = p;
    v_normal = normal;
    v_materialIndex = int(materialIndex);
    v_texCoord = texCoord;
    v_tangent = tangent;
	v_localPosition = position;
}