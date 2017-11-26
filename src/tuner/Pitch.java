package tuner;

public class Pitch {
	private Double time;
	private Float pitch;
	
	public Pitch(double time, float pitch) {
		this.time = time;
		this.pitch = pitch;
	}
	
	public Pitch() {
		time = null;
		pitch = null;
	}
	
	public Double getTime() {
		return time;
	}
	
	public void setTime(double time) {
		this.time = time;
	}
	
	public Float getPitch() {
		return pitch;
	}
	
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	
	public String toString() {
		return "Pitch [time=" + time + ", pitch=" + pitch + "]";
	}
	
}
