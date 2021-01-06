out float v_eyeDist;
void main() {
	vec4 p = vec4(position, 1);
	p *= worldMatrix;
	p *= viewMatrix;
	v_eyeDist = -p.z;
	p *= projMatrix;
	gl_Position = p;
}
