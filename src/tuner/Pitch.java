package tuner;

public class Pitch {
	private double time;
	private float pitch;
	
	public Pitch(double time, float pitch) {
		this.time = time;
		this.pitch = pitch;
	}
	
	public double getTime() {
		return time;
	}
	
	public void setTime(double time) {
		this.time = time;
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	
	public String toString() {
		return "Pitch [time=" + time + ", pitch=" + pitch + "]";
	}
	
}
