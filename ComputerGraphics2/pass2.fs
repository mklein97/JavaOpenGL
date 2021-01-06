uniform sampler2DArray tex;
uniform sampler2DArray shadowBuffer;
uniform sampler2DArray noiseP;
uniform sampler2DArray ramp;

in vec2 v_texCoord;
out vec4 color;

int P(int idx){
	float f = texelFetch(noiseP,ivec3(idx,0,0),0).r;
	return int(f);
}
float fade(float x){
	return (10.0 + (-15 + 6*x)*x)*x*x*x;
}
float gradient(float h, vec3 v){
	h = mod(h,16);
	switch(int(h)){
		case 0: return v.x+v.y;
		case 1: return -v.x-v.y;
		case 2: return v.x-v.y;
		case 3: return -v.x-v.y;
		case 4: return v.x+v.z;
		case 5: return -v.x-v.z;
		case 6: return v.x-v.z;
		case 7: return -v.x-v.z;
		case 8: return v.y+v.z;
		case 9: return -v.y-v.z;
		case 10: return v.y-v.z;
		case 11: return -v.y-v.z;
		case 12: return v.y+v.x;
		case 13: return -v.y-v.z;
		case 14: return v.y-v.x;
		case 15: return -v.y-v.z;
	}
}
float noise(vec3 p){
	int rho = 256;
	ivec3 L = ivec3(floor(p));
	L.x %= rho;
	L.y %= rho;
	L.z %= rho;
	p = fract(p);
	int H1,H2,H3,H4,H5,H6;
	H1 = P(L.x)+L.y;
	H2 = P(H1)+L.z;
	H3 = P(H1+1)+L.z;
	H4 = P(L.x+1)+L.y;
	H5 = P(H4)+L.z;
	H6 = P(H4+1)+L.z;
	vec3 v0,v1,v2,v3,v4,v5,v6,v7;
	v0 = p;
	v1 = p-vec3(1,0,0);
	v2 = p-vec3(0,1,0);
	v3 = p-vec3(1,1,0);
	v4 = p-vec3(0,0,1);
	v5 = p-vec3(1,0,1);
	v6 = p-vec3(0,1,1);
	v7 = p-vec3(1,1,1);
	float g0,g1,g2,g3,g4,g5,g6,g7;
	g0 = gradient(H2,v0);
	g1 = gradient(H5,v1);
	g2 = gradient(H3,v2);
	g3 = gradient(H6,v3);
	g4 = gradient(H2+1,v4);
	g5 = gradient(H5+1,v5);
	g6 = gradient(H3+1,v6);
	g7 = gradient(H6+1,v7);
	float f = fade(p.x);
	float tmp1 = mix( g0,g1, f );
	float tmp2 = mix( g2,g3, f );
	float tmp3 = mix( g4,g5, f );
	float tmp4 = mix( g6,g7, f );
	f = fade(p.y);
	float tmp5 = mix( tmp1, tmp2, f );
	float tmp6 = mix( tmp3, tmp4, f );
	f = fade(p.z);
	return mix( tmp5, tmp6, f );
}

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
	
	if(dot(N,N) == 0.0) {
		color.rgb = s2.rgb + s3.rgb;
		color.a = 1.0;
		return;
	}
	
	//noise
	if (s2.a == 1){
		vec3 po = vec3(s0.a, s1.a, s3.a);
		float val = noise(noiseScale * po) + 0.5*noise(noiseScale *(po * 2)) + 0.25*noise(noiseScale *(po * 4));
		float tc = 1.0 + sin(po.x * 0.06 + val) * 0.5;
		s2.rgb = texture(ramp, vec3(tc, 0.0, 0.0)).rgb;
	}
	
	color.rgb += s2.rgb * dp * lightColor;
	color.rgb += sp * s4.rgb * lightColor;
	color.rgb += s3.rgb * blurColorScale;
	float fogDistance = distance(eyePos,worldPos);
    float fogAmt = clamp( (fogDistance - fogNear) / fogDelta, 0, 1 );
    color.rgb = mix( color.rgb, fogColor, fogAmt );
	
	//shadow
	vec4 p=vec4(worldPos,1);
	p*=lightViewMatrix;//set by draw2()
	float dist1=(-p.z - lightHitherYon[0]) / lightHitherYon[2];
	p*=lightProjMatrix;
	p/=p.w;
	p.xy+=vec2(1,1);
	p.xy*=0.5;
	float dist2=texture(shadowBuffer,vec3(p.xy,0)).r;
	if(p.x<0||p.x>1||p.y<0||p.y>1||dist1>dist2+bias)
		color.rgb *= 0.35;
	
	color.a = 1.0;
	
}