#version 150

#moj_import <matrix.glsl>

uniform sampler2D Sampler0;

uniform float GameTime;
uniform int EndPortalLayers;

in vec4 texProj0;

out vec4 fragColor;

const vec3[] COLORS = vec3[](
vec3(0.02, 0.30, 0.45),
vec3(0.03, 0.45, 0.70),
vec3(0.05, 0.65, 1.00),
vec3(0.10, 0.85, 1.00),
vec3(0.20, 1.00, 1.00),
vec3(0.05, 0.55, 0.95),
vec3(0.08, 0.75, 1.00),
vec3(0.12, 0.95, 1.00),
vec3(0.03, 0.35, 0.65),
vec3(0.08, 0.60, 0.90),
vec3(0.15, 0.90, 1.00),
vec3(0.25, 1.00, 1.00),
vec3(0.05, 0.40, 0.75),
vec3(0.10, 0.70, 1.00),
vec3(0.30, 1.00, 1.00),
vec3(0.60, 1.00, 1.00)
);

const mat4 SCALE_TRANSLATE = mat4(
0.5, 0.0, 0.0, 0.25,
0.0, 0.5, 0.0, 0.25,
0.0, 0.0, 1.0, 0.0,
0.0, 0.0, 0.0, 1.0
);

mat4 end_portal_layer(float layer) {
    mat4 translate = mat4(
    1.0, 0.0, 0.0, 17.0 / layer,
    0.0, 1.0, 0.0, (2.0 + layer / 1.5) * (GameTime * 1.5),
    0.0, 0.0, 1.0, 0.0,
    0.0, 0.0, 0.0, 1.0
    );

    mat2 rotate = mat2_rotate_z(radians((layer * layer * 4321.0 + layer * 9.0) * 2.0));
    mat2 scale = mat2((4.5 - layer / 4.0) * 2.0); // smaller number = bigger stars

    return mat4(scale * rotate) * translate * SCALE_TRANSLATE;
}

void main() {
    vec3 color = vec3(0.0);
    float alpha = 0.0;

    for (int i = 0; i < EndPortalLayers; i++) {
        vec4 star = textureProj(Sampler0, texProj0 * end_portal_layer(float(i + 12)));

        color += star.rgb * COLORS[i];
        alpha += star.r * 0.7;
    }

    alpha = clamp(alpha, 0.0, 0.75);

    fragColor = vec4(color, alpha);
}
