uniform sampler2DArray tex;

in vec2 v_texCoord;
out vec4 color;

void main() {
	vec4 s0 = texture(tex, vec3(v_texCoord, 0));
	vec4 s1 = texture(tex, vec3(v_texCoord, 1));
	vec4 s2 = texture(tex, vec3(v_texCoord, 2));
	vec4 s3 = texture(tex, vec3(v_texCoord, 3));
	vec4 s4 = texture(tex, vec3(v_texCoord, 4));
	
	vec3 worldPos = s0.xyz;
	vec3 N = s1.xyz;
	vec3 L = lightPosition.xyz - worldPos;
	L = normalize(L);
	vec3 V = normalize(eyePos - worldPos);
	vec3 R = reflect(-L, N);
	float dp = dot(L,N);
	dp = max(0.0, dp);
	float sp = 0.0;
	if (dp > 0.0) {
		sp = dot(V,R);
		sp = max(0.0, sp);
		sp = pow(sp, s4.a);
	}
	color.rgb = vec3(0,0,0);
	
	color.rgb = s2.rgb * dp * lightColor;
	color.rgb += sp * s4.rgb * lightColor;
	color.rgb += s3.rgb * blurColorScale;
	float fogDistance = distance(eyePos,worldPos);
    float fogAmt = clamp( (fogDistance - fogNear) / fogDelta, 0, 1 );
    color.rgb = mix( color.rgb, fogColor, fogAmt );
	
	
	color.a = 1;
		if( dot(N,N) == 0.0 ){
		color.rgb = s2.rgb + s3.rgb;
		color.a = 1.0;
		return;
	}
}