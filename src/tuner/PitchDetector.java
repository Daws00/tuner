package tuner;

import java.awt.Color;
import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;

public class PitchDetector implements PitchDetectionHandler {

	/**
	 * E5 - 659.3
	 * A4 - 440
	 * D4 - 293.7
	 * G3 - 196
	 */

	private AudioDispatcher dispatcher;
	private PitchEstimationAlgorithm algo;
	
	private static DataHandler dataHandler;
	
	private JFrame frame;
	
	private float pitch;
	private boolean test = false;

	public PitchDetector() {
		algo = PitchEstimationAlgorithm.MPM;
		
		try {
			setNewMixer(AudioSystem.getMixer((Info) Shared.getMixerInfo(false, true).firstElement()));
		} catch(LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setPreferredSize(new Dimension(500, 500));
		frame.getContentPane().setBackground(Color.RED);
		frame.pack();
		frame.setVisible(true);
	}

	
	private void setNewMixer(Mixer mixer) throws LineUnavailableException,
			UnsupportedAudioFileException {
		
		if(dispatcher!= null){
			dispatcher.stop();
		}
		
		float sampleRate = 44100;
		int bufferSize = 1024;
		int overlap = 0;
		
		System.out.print("Started listening with " + Shared.toLocalString(mixer.getMixerInfo().getName()) + "\n");

		final AudioFormat format = new AudioFormat(sampleRate, 16, 1, true,
				true);
		final DataLine.Info dataLineInfo = new DataLine.Info(
				TargetDataLine.class, format);
		TargetDataLine line;
		line = (TargetDataLine) mixer.getLine(dataLineInfo);
		final int numberOfSamples = bufferSize;
		line.open(format, numberOfSamples);
		line.start();
		final AudioInputStream stream = new AudioInputStream(line);

		JVMAudioInputStream audioStream = new JVMAudioInputStream(stream);
		// create a new dispatcher
		dispatcher = new AudioDispatcher(audioStream, bufferSize,
				overlap);

		// add a processor
		dispatcher.addAudioProcessor(new PitchProcessor(algo, sampleRate, bufferSize, this));
		
		new Thread(dispatcher,"Audio dispatching").start();
	}

	public static void main(String... strings) throws InterruptedException, InvocationTargetException {
		System.out.println("wubba lubba dub dub!");
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				dataHandler = new DataHandler("data.csv");
				dataHandler.write(new Pitch());
				PitchDetector detector = new PitchDetector();
				GPIOServoController controller = new GPIOServoController(detector);
				Thread thread = new Thread(controller);
				thread.start();
			}
		});
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public void inTune(boolean inTune) {
		if(inTune) {
			this.frame.getContentPane().setBackground(Color.GREEN);
			this.frame.revalidate();
			this.frame.repaint();
		}
	}
	
	public void reset(boolean shouldReset) {
		if(shouldReset) {
			setFrameColor(Color.BLUE);
		} else {
			setFrameColor(Color.RED);
		}
	}
	
	private void setFrameColor(Color color) {
		this.frame.getContentPane().setBackground(color);
		this.frame.revalidate();
		this.frame.repaint();
	}
	
	public void handlePitch(PitchDetectionResult pitchDetectionResult,AudioEvent audioEvent) {
		float probability = pitchDetectionResult.getProbability();
		if(probability >= 0.99) {
			float pitch = pitchDetectionResult.getPitch();
			this.pitch = pitch;
			if(pitch != -1){
				double timeStamp = audioEvent.getTimeStamp();
				
				if(!test) {
					dataHandler.write(new Pitch(timeStamp, pitch));
				}
				
				double rms = audioEvent.getRMS() * 100;
				System.out.printf("Pitch detected at %.2fs: %.2fHz ( %.2f probability, RMS: %.5f )\n", timeStamp,pitch,probability,rms);
			}
		}
	}
}