#version 400

layout (location =0) in vec3 position;
layout (location =1) in vec2 texCoords;

out vec2 outTexCoord;
out vec2 outWorldPos;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;

uniform int atlasSize;
uniform vec2 offset;

void main()
{
    vec4 worldPos = transformationMatrix * vec4(position, 1.0);
	gl_Position = projectionMatrix * viewMatrix * worldPos;

    outTexCoord = (texCoords / atlasSize) + offset;
    outWorldPos = worldPos.xy;
}