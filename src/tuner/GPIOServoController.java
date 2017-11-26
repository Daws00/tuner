package tuner;

import java.lang.Runnable;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class GPIOServoController implements Runnable {
	private GpioController gpioFactory;
    private GpioPinDigitalOutput pin;
    
    private long period = 20000000;
    
    PitchDetector detector;
    
    int position = 90;
    
    public GPIOServoController(PitchDetector detector) {
    		this.gpioFactory = GpioFactory.getInstance();
    		this.pin = gpioFactory.provisionDigitalOutputPin(RaspiPin.GPIO_07, PinState.LOW);
    		
    		pin.setShutdownOptions(true, PinState.LOW);
    		
    		this.detector = detector;
    		
    		long start = System.currentTimeMillis();

    		while(System.currentTimeMillis() - start < 2000) {
    			long upStart = System.nanoTime();
    			long duty = angleToDuty(90);
    			pin.high();
    			while(upStart + duty > System.nanoTime()) {
    				
    			}
    			
    			long lowStart = System.nanoTime();
    			long rest = period - duty;
    			pin.low();
    			while(lowStart + rest > System.nanoTime()) {
    				
    			}
    		}
    }
    
    private long angleToDuty(int angle) {
    		return (long) ((1 + (angle/180.0)) * 1000000);
    }
    
    private void turn() {
	    	long start = System.currentTimeMillis();
	    	long end = 500;
	    	while(start + end > System.currentTimeMillis()) {
		    	long highStart = System.nanoTime();
		    	long duty = angleToDuty(position);
		   		pin.high();
		   		while(highStart + duty > System.nanoTime()) {
		   			
		   		}
	    		
		    	long lowStart = System.nanoTime();
		    	long rest = period - duty;
		   		pin.low();
		   		while(lowStart + rest > System.nanoTime()) {
		   			
		   		}
	    	}
    }
    
	public void run() {
	    	while(true) {
	    		if(detector.getPitch() != -1) {
		    		changePosition(detector.getPitch());
		    		System.out.println(position);
		    		turn();
		    	}
	    	}
	}
	
	private void reset(int pos) {
		detector.reset(true);
		long start = System.currentTimeMillis();
		long end = 750;
		while(start + end > System.currentTimeMillis()) {
			
		}
		position = pos;
		turn();
		detector.reset(false);
	}
	
    private void changePosition(float pitch) {
    	int deltaAngle = 15;
    	if(pitch != -1) {
	    	if(pitch > 410 && pitch < 470) {
	    		if(pitch < 439.5) {
	   				if(position < 180) {
	    				position += deltaAngle;
	    			} else {
	    				reset(0);
	   				}
	   			} else if (pitch > 440.5) {
	   				if(position > 0) {
	    				position -= deltaAngle;
	    			} else {
	   					reset(180);
	   				}
	    		} else {
	    			detector.inTune(true);
	    		}
	   		} else if(pitch > 263.7 && pitch < 323.7) {
	    		if(pitch < 293.2) {
	   				if(position < 180) {
	    				position += deltaAngle;
	    			} else {
	    				reset(0);
	   				}
	   			} else if (pitch > 294.2 ) {
	   				if(position > 0) {
	    				position -= deltaAngle;
	    			} else {
	   					reset(180);
	   				}
	    		} else {
	    			detector.inTune(true);
	    		}
	   		} else if(pitch > 166 && pitch < 226) {
	   			
	    		if(pitch < 195.5) {
	   				if(position < 180) {
	    				position += deltaAngle;
	    			} else {
	    				reset(0);
	   				}
	   			} else if (pitch > 196.5) {
	   				if(position > 0) {
	    				position -= deltaAngle;
	    			} else {
	   					reset(180);
	   				}
	    		} else {
	    			
	    			detector.inTune(true);
	    		}
	   		} else if(pitch > 629.3 && pitch < 689.3) {
	    		if(pitch < 658.8) {
	   				if(position < 180) {
	    				position += deltaAngle;
	    			} else {
	    				reset(0);
	   				}
	   			} else if (pitch > 659.8) {
	   				if(position > 0) {
	    				position -= deltaAngle;
	    			} else {
	   					reset(180);
	   				}
	    		} else {
	    			detector.inTune(true);
	    		}
	   		}
    	}
    }
}