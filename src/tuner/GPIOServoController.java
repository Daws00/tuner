package tuner;

import java.lang.Runnable;

/*import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;*/

public class GPIOServoController implements Runnable {
	//private GpioController gpioFactory;
    //private GpioPinDigitalOutput pin;
    
    private long period = 20000000;
    
    PitchDetector detector;
    
    int position = 90;
    
    public GPIOServoController(PitchDetector detector) {
    		//this.gpioFactory = GpioFactory.getInstance();
    		//this.pin = gpioFactory.provisionDigitalOutputPin(RaspiPin.GPIO_07, PinState.LOW);
    		
    		//pin.setShutdownOptions(true, PinState.LOW);
    		
    		this.detector = detector;
    		
    		long start = System.currentTimeMillis();

    		while(System.currentTimeMillis() - start < 2000) {
    			long upStart = System.nanoTime();
    			long duty = angleToDuty(90);
    			while(upStart + duty > System.nanoTime()) {
    				//pin.high();
    			}
    			
    			long downStart = System.nanoTime();
    			long rest = period - duty;
    			while(downStart + rest > System.nanoTime()) {
    				//pin.low();
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
	    		long upStart = System.nanoTime();
	    		long duty = angleToDuty(position);
	    		while(upStart + duty > System.nanoTime()) {
	    			//pin.high();
	    		}
	    		
	    		long downStart = System.nanoTime();
	    		long rest = period - duty;
	    		while(downStart + rest > System.nanoTime()) {
	    			//pin.low();
	    		}
	    		
	    	}
    }
    
    @Override
	public void run() {
    		while(true) {
    			changePosition(detector.getPitch());
    			
    			turn();
    		}
	}
	
    private void changePosition(float pitch) {
    		if(pitch > 410 && pitch < 470) {
    			if(pitch < 439) {
    				position += 2;
    			} else if (pitch > 441) {
    				position -= 2;
    			} else {
    				detector.inTune(true);
    			}
    		}
    }
}