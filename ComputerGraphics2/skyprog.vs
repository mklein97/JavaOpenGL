
in vec3 position;

out vec3 v_objPosition;

void main(){
    v_objPosition = position;
    vec4 p = vec4(position,1.0);    
    p.xyz += eyePos;
    p = p * viewMatrix;
    p = p * projMatrix;
    p.z=p.w;
    gl_Position = p;
}
