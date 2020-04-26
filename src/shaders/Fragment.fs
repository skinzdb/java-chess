#version 400
#define MAX_BRIGHTNESS 0.7

in vec2 outTexCoord;
in vec2 outWorldPos;

out vec4 fragColour;

uniform sampler2D textureSampler;
uniform float minLight;
uniform float maxLight;
uniform vec3 light[8]; // x, y refer to position. z refers to brightness.
uniform vec3 lightAtt[8];
uniform vec3 lightCol[8];

void main()
{
    vec3 totalDiffuse = vec3(0.0);

    for (int i = 0; i < 8; i++) {
        float d = length(light[i].xy - outWorldPos); // distance     
        float a = lightAtt[i].x + (lightAtt[i].y * d) + (lightAtt[i].z * d * d); // attenuation factor
        totalDiffuse = totalDiffuse + (light[i].z * lightCol[i]) / a;
    }
	
    totalDiffuse = max(totalDiffuse, minLight);
    totalDiffuse = min(totalDiffuse, maxLight);

    fragColour = vec4(totalDiffuse, 1.0) * texture(textureSampler, outTexCoord);

    if (fragColour.w < 0.5) discard;
}