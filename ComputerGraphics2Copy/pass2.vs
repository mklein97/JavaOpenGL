in vec3 position;
in vec2 texCoord;

out vec2 v_texCoord;

void main(){
    vec4 p = vec4(position,1.0);
    gl_Position = p;
    v_texCoord = texCoord;
}
