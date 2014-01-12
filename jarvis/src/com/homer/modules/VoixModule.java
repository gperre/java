package com.homer.modules;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import t2s.son.LecteurTexte;

public class VoixModule {
	
	public static String comprendre(String file){
		//System.out.println(new File(file).length());
		
		//System.out.println("Start speech-to-text");
		SpeechInput s = new SpeechInput();
		String reponse = s.recognize(file,"fr-FR");
		//System.out.println("Stop speech-to-text");
		return reponse;
	}
	
	public static void prononcer(String phrase){
		
		/*
		LecteurTexte lt = new LecteurTexte(phrase);
		lt.playAll();
		*/
		
		//synthèse vocal google plus sexy ...
		try{

			phrase=java.net.URLEncoder.encode(phrase, "UTF-8");

			URL url = new URL("http://translate.google.com/translate_tts?ie=UTF-8&tl=fr&q="+phrase);
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			urlConn.addRequestProperty("User-Agent", "Mozilla/4.76");

			InputStream audioSrc = urlConn.getInputStream();

			DataInputStream read = new DataInputStream(audioSrc);
			OutputStream outstream = new FileOutputStream(new File("d:/temp/reponse.mp3"));
			byte[] buffer = new byte[1024];
			int len;
			while ((len = read.read(buffer)) > 0) {
				outstream.write(buffer, 0, len);                    
			}
			outstream.close();
			
			SoundJLayer soundToPlay = new SoundJLayer(new File("d:/temp/reponse.mp3").getAbsolutePath());
	        soundToPlay.play();
			
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}
