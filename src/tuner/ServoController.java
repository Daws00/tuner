package tuner;

class ServoController {
	private int currentPos;
	private Runtime runTime = Runtime.getRuntime();;
	private int change = 10;
	
	public void turn(Runtime runTime, boolean turning, boolean direction) {
		while(turning) {
			try {
				if(direction) {
					runTime.exec("gpio pwm 1 " + currentPos + change);
				}
				if(direction) {
					runTime.exec("gpio pwm 1 " + currentPos + change);
				}
			} catch (Exception e) {
		        System.out.println("Exception occured: " + e.getMessage());
		    }
		}
	}
	
	public ServoController() {
		try {
			runTime.exec("gpio mode 1 pwm");
			runTime.exec("gpio pwm-ms");
			runTime.exec("gpio pwmc 192"); 
			runTime.exec("gpio pwmr 2000"); 
			runTime.exec("gpio pwm 1 152"); // ~center
		} catch (Exception e) {
	        System.out.println("Exception occured: " + e.getMessage());
	    }
		
		currentPos = 152;
	}
	
    public static void main(String args[]) {
        try {			
            Runtime runTime = Runtime.getRuntime();
            runTime.exec("gpio mode 1 pwm");
            runTime.exec("gpio pwm-ms");
            runTime.exec("gpio pwmc 192"); 
            runTime.exec("gpio pwmr 2000"); 
            runTime.exec("gpio pwm 1 152"); // ~center
            
            int i = 100;
            boolean turningLeft = true;
            while(true) {
                runTime.exec("gpio pwm 1 " + i);
                Thread.sleep(10);
                if (turningLeft) {
                    i++;
                } else {
                    i--;
                }
                if (i > 200) { turningLeft = false; }
                if (i < 100) { turningLeft = true; }
            }
			
        } catch (Exception e) {
            System.out.println("Exception occured: " + e.getMessage());
        }
    }
}