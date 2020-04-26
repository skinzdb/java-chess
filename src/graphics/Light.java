package graphics;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Light {

    private Vector2f position;

    private float intensity;

    private Vector3f attenuation;

    private Vector3f colour;

    public Light(Vector2f position, float intensity, Vector3f attenuation, Vector3f colour) {
        this.position = position;
        this.intensity = intensity;
        this.attenuation = attenuation;
        this.colour = colour;
    }

    public Light(Vector2f position, float intensity, Vector3f colour) {
        this(position, intensity, new Vector3f(1, 0, 0), colour);
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    public Vector3f getAttenuation() {
        return attenuation;
    }

    public void setAttenuation(Vector3f attenuation) {
        this.attenuation = attenuation;
    }

    public Vector3f getColour() {
        return colour;
    }

    public void setColour(Vector3f colour) {
        this.colour = colour;
    }
}