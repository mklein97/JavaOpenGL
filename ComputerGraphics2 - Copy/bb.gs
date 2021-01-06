//Billboard geometry shader
layout(points) in;
layout(triangle_strip, max_vertices = 4) out;
out  vec2 g_texCoord;

void main() {
	vec4 c = gl_in[0].gl_Position;
	c = c * viewMatrix;
	vec4 p;
	p = c;
	p.x += -bbSize.x;
	p.y += bbSize.y;
	p = p * projMatrix;
	gl_Position = p;
	g_texCoord = vec2(0,1);
	EmitVertex();
	p = c;
	p.x -= bbSize.x;
	p.y -= bbSize.y;
	p = p * projMatrix;
	gl_Position = p;
	g_texCoord = vec2(0,0);
	EmitVertex();
	p = c;
	p.x += bbSize.x;
	p.y += bbSize.y;
	p = p * projMatrix;
	gl_Position = p;
	g_texCoord = vec2(1,1);
	EmitVertex();
	p = c;
	p.x += bbSize.x;
	p.y -= bbSize.y;
	p = p * projMatrix;
	gl_Position = p;
	g_texCoord = vec2(1,0);
	EmitVertex();
}