//Fragment Shader
in vec2 g_texCoord;
uniform sampler2DArray textures;
layout(location=0) out vec4 slot0;
layout(location=1) out vec4 slot1;
layout(location=2) out vec4 slot2;
layout(location=3) out vec4 slot3;
layout(location=4) out vec4 slot4;

void main() {
	vec4 c = texture(textures, vec3(g_texCoord, texFrameNumber));
	if (c.a < 0.01)
		discard;
	slot0 = vec4(0);
    slot1 = vec4(0);
    slot2 = c;
    slot3 = c;
    slot4 = vec4(0);
}