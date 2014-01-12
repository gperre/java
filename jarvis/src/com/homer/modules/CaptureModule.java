package com.homer.modules;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javaFlacEncoder.FLACFileWriter;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.AudioFormat.Encoding;

public class CaptureModule implements Runnable {

	static final long SEUIL_MIN = 12000; // en dessous de ce seuil on n'écoute pas
	

	private TargetDataLine line;
	private File file;
	private ByteArrayOutputStream outputStream;
	private AudioFormat audioFormat;
	private static final AudioFileFormat.Type targetType = AudioFileFormat.Type.WAVE;

	public void init() {


		Encoding encoding = Encoding.PCM_SIGNED;
		float frameRate = 16000;
		int channels = 1;
		int sampleSizeInBits = 16;
		boolean bigEndian = false;
		int sampleBytes = sampleSizeInBits / 8;
		int frameBytes = sampleBytes * channels;
		float sampleRate = frameRate;
		audioFormat = new AudioFormat(encoding, sampleRate, sampleSizeInBits, channels, frameBytes, frameRate, bigEndian);

		DataLine.Info info = new DataLine.Info(TargetDataLine.class,				audioFormat);

		if (!AudioSystem.isLineSupported(info)) {
			System.err.println("Audio Format specified is not supported");
			return;
		}

		// On récupère le DataLine adéquat
		try {
			line = (TargetDataLine) AudioSystem.getLine(info);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			return;
		}

	}

	public void run() {

		// on ouvre le Dataline
		try {
			line.open(audioFormat);
		} catch (LineUnavailableException e1) {
			e1.printStackTrace();
			return;
		}

		// pour que le flux soit effectivement redirigé sur la carte son il
		// faut
		// demarrer la ligne
		line.start();
	if(record){
		AudioInputStream audioInputStream = new AudioInputStream(line);

		try {
			//System.out.println("eh!");
			AudioSystem.write(audioInputStream, FLACFileWriter.FLAC, file);
			//System.out.println("oh!");
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			line.close();
			try {
				audioInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		line.stop();
		line.close();
	}
	}
	public void stop(){
		line.stop();
		line.close();

	}
	public void setFile(File file) {
		this.file = file;
	}

	public TargetDataLine getLine() {
		return line;
	}


	public static boolean capture(long duree) throws Exception {
		//System.out.println("Start recording");
		if(new File("d:/temp/scan.flac").exists())
			new File("d:/temp/scan.flac").delete();
		//scanner surveillance à l'écoute d'un son supérieur au seuil mmin
		Thread captureThread;	
    	CaptureModule capMod = new CaptureModule();
    	capMod.setFile(new File("d:/temp/scan.flac"));
    	capMod.init();
    	//capMod.setRecord(false);//le scannner n'eregistre rien
    	captureThread = new Thread(capMod);
		captureThread.start();
		
		boolean enregistre = false;
		
		File f = new File("d:/temp/scan.flac");
		long taillefichier = 0;
		long volumeAmbiant = 0;
		long cut = 0;
		long maxInc = 0;
		System.out.println("Scanner à l'affut ... ");
		while(true){
			//System.out.println(" :" + volumeAmbiant + "/" + maxInc + "/"+ taillefichier +"/" + f.length());
			
			if(taillefichier != f.length()) {
				volumeAmbiant = f.length() - taillefichier;
				taillefichier = f.length();
				
			} 
			if(volumeAmbiant >= SEUIL_MIN ){
				enregistre = true;
				maxInc=volumeAmbiant;
				System.out.println("Capture d'un son volume (scanner) :" + maxInc );
				break;
			}
			
			if(taillefichier >= 1000000){
				System.out.println("scan file rotate : " + taillefichier);
				//on supprime le fichier de scan, et on le reinitialise
				captureThread.stop();
				return false;
			}
			Thread.sleep(1000);
		}
		
		/*while( cut < 5 ){
						
			Thread.sleep(1000);
			//System.out.println(taillefichier + "/" + increment + "/" + cut);
			if(taillefichier != f.length()) {
				volumeAmbiant = f.length() - taillefichier;
				taillefichier = f.length();
				cut++;
			} 
			if(volumeAmbiant >= SEUIL_MIN ){
				enregistre = true;
				cut=5;
				maxInc=volumeAmbiant;
				System.out.println("Capture d'un son volume (scanner) :" + maxInc );
			}
		}*/
		
		captureThread.stop();
		
		//on commence a enregistrer
		Thread captureThread3;	
    	CaptureModule capMod3 = new CaptureModule();
    	capMod3.setFile(new File("d:/temp/surv.flac"));
    	capMod3.init();
    	//capMod3.setRecord(false);//le scannner n'eregistre rien
    	captureThread3 = new Thread(capMod3);
		captureThread3.start();
		System.out.print("Capture du mot clé , vous avez 5 secondes pour agir : ");
		
		 enregistre = false;
		
		File f3 = new File("d:/temp/surv.flac");
		long taillefichier3 = 0;
		long volumeAmbiant3 = 0;
		long cut3 = 0;
		long maxInc3 = 0;
		while( cut3 < 10 ){
						
			Thread.sleep(1000);
			System.out.print(cut3 + "...");
			//System.out.println(taillefichier + "/" + increment + "/" + cut);
			if(taillefichier3 != f3.length()) {
				volumeAmbiant3 = f3.length() - taillefichier3;
				taillefichier3 = f3.length();
				cut3++;
			} 
			if(volumeAmbiant3 >= SEUIL_MIN ){
				enregistre = true;
				cut3=10;
				maxInc3=volumeAmbiant3;
				System.out.println("\nCapture d'un son volume (ordre) :" + maxInc3 );
			}
			
		}
		
		captureThread3.stop();
		
		
		//on analyse l'ordre enregistré - si on a capter quelquechose
		
		
		
		
		
		
		
		if(enregistre){
			//repondre :
			//System.out.println("maxInc=" +maxInc);
			
			//if(maxInc < 13500) {
				String phrase = VoixModule.comprendre("d:/temp/surv.flac");
				System.out.println("Son inferieur au seuil de surete, analyse : "+phrase);
				if(phrase.indexOf("écoute-moi")!=-1 || phrase.indexOf("coute")!=-1 || phrase.indexOf("là")!=-1 || phrase.indexOf("avec moi")!=-1 || phrase.indexOf("jarvis")!=-1  ){
					VoixModule.prononcer("Oui ?");	
				} else {
					return false;
				}
			
			//}else {
			//	VoixModule.prononcer("Oui monsieur");
			//}
			Thread.sleep(2000);
			Thread captureThread2;	
	    	CaptureModule capMod2 = new CaptureModule();
	    	capMod2.setFile(new File("d:/temp/capture.flac"));
	    	capMod2.init();
	    	captureThread2 = new Thread(capMod2);
			captureThread2.start();
			
			 enregistre = false;
			
			File f2 = new File("d:/temp/capture.flac");
			long taillefichier2 = 0;
			long increment2 = 0;
			long cut2 = 0;
			
			System.out.print("Vous avez 5 secondes pour donner votre ordre : " );
			
			while( cut2 < 5){
				
				
				System.out.print(cut2 +"..." );	
				Thread.sleep(1000);
				//System.out.println(taillefichier2 + "/" + increment2 + "/" + cut2);
				if(taillefichier2 != f2.length()){
					increment2 = f2.length() - taillefichier2;
					taillefichier2 = f2.length();
					if(increment2 < 18000 && increment2 >1 ) {
						cut2++;
					} else {
						cut2=0;
					}
					
				} 
				if(increment2 >=11000 ){
					enregistre = true;
				}
			}
			
			captureThread2.stop();
			if(!enregistre){
				VoixModule.prononcer("J'abandonne.");
				System.out.println("\nabandon");
			} else {
				VoixModule.prononcer("Bien reçu !");
				System.out.println("\nc'est parti");
			}
		}
		
		//captureThread.join();
		//System.out.println("Stop recording");
		return enregistre;
	}

		boolean record = true;
	private void setRecord(boolean b) {
		// TODO Auto-generated method stub
		record = b;
	}
}
