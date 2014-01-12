package com.homer.modules;
/*
        Jaivox version 0.5 August 2013
        Copyright 2010-2013 by Bits and Pixels, Inc.

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

            http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing, software
       distributed under the License is distributed on an "AS IS" BASIS,
       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
       See the License for the specific language governing permissions and
       limitations under the License.
 */

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



public class SpeechInput {

	String address1 = "http://www.google.com/speech-api/v1/recognize?lang=";
	// put en-us in between these
	String address2 = "&client=me";
	String agent = "Mozilla/5.0";
	String type = "audio/x-flac; rate=16000";

	String utt = "utterance";
	String first = "\":\"";
	String second = "\",\"";

	public SpeechInput () {
	}

	public String recognize (String flacfile, String lang) {
		try {
			String address = address1 + lang + address2;
			URL url = new URL (address);
			URLConnection urlConnection = url.openConnection ();
			urlConnection.setUseCaches(false);
			HttpURLConnection link = (HttpURLConnection) urlConnection;
			link.setInstanceFollowRedirects (false);
			link.setRequestMethod ("POST");
			urlConnection.setDoOutput (true);
			link.setRequestProperty ("User-Agent", "me" );
			link.setRequestProperty ("Content-Type", type);
			DataInputStream inStream = new DataInputStream (
					new FileInputStream (flacfile));
			DataOutputStream outStream = new DataOutputStream (link.getOutputStream());
			
			
			byte buffer [] = new byte[4096];
			int len;
			while ((len = inStream.read (buffer)) > 0) {
				outStream.write(buffer, 0, len);
			}
			outStream.close ();
			inStream.close ();
			Thread.sleep (100);
			String recognized = "";

			int responseCode = link.getResponseCode ();
			if (responseCode == 200) {
				InputStream resultStream = link.getInputStream ();
				BufferedReader in = new BufferedReader (
						new InputStreamReader (resultStream,"UTF-8"));
				StringBuffer sb = new StringBuffer ();
				String line = null;
				while ((line = in.readLine ()) != null) {
					sb.append (line);
					sb.append ("\n");
				}
				in.close ();
				String result = new String (sb);
				int pos = result.indexOf (utt);
				if (pos == -1) {
					//Log.severe (flacfile+"\tNo utt result");
					return "error 1";
				}
				int qos = result.indexOf (first, pos+1);
				if (qos == -1) {
					//Log.severe (flacfile+"\tNo first result");
					return "error 2";
				}
				int ros = result.indexOf (second, qos+1);
				if (ros == -1) {
					//Log.severe (flacfile+"\tNo second result");
					return "error 3";
				}
				recognized = result.substring (qos+3, ros);
				//Log.info (flacfile+"\t"+recognized);
			}
			return recognized;
		}
		catch (Exception e) {
			e.printStackTrace ();
			return "error 4";
		}
	}
	
	public static void main(String args[]) throws Exception {
	/*	SpeechInput s = new SpeechInput();
		String reponse = s.recognize("d:/toto.flac","fr-FR");
		System.out.println(reponse);*/
		
		
		try{
            //String word="Très bien monsieur, je procède à l'extinction des feux ! Bonne nuit.";
            String word="Très bien monsieur ! Je redémarre le système, à tout de suite.";
            word=java.net.URLEncoder.encode(word, "UTF-8");
            System.out.println("encoding :" + word);
            URL url = new URL("http://translate.google.com/translate_tts?ie=UTF-8&tl=fr&q="+word);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.addRequestProperty("User-Agent", "Mozilla/4.76");
            
           InputStream audioSrc = urlConn.getInputStream();
            
            DataInputStream read = new DataInputStream(audioSrc);
            OutputStream outstream = new FileOutputStream(new File("d:/reponse.mp3"));
            byte[] buffer = new byte[1024];
            int len;
            while ((len = read.read(buffer)) > 0) {
                    outstream.write(buffer, 0, len);                    
            }
            outstream.close();            
}catch(Exception e){
           System.out.println(e.getMessage());
}
		
	/*	 Thread audioRecorderThread;	
    	 AudioPlayer p = new AudioPlayer();
 		p.setFile(new File("d:/reponse.mp3"));
        p.init();
		audioRecorderThread = new Thread(p);
		audioRecorderThread.start();*/
		
		SoundJLayer soundToPlay = new SoundJLayer(new File("d:/reponse.mp3").getAbsolutePath());
	        soundToPlay.play();
		
	/*	Path path = Paths.get("d:/out.flac");
	       byte[] data = Files.readAllBytes(path);
	        
	       String request = "https://www.google.com/"+ 
	                "speech-api/v1/recognize?"+ 
	                "xjerr=1&client=speech2text&lang=en-US&maxresults=10";
	       URL url = new URL(request); 
	       HttpURLConnection connection = (HttpURLConnection) url.openConnection();           
	       connection.setDoOutput(true);
	       connection.setDoInput(true);
	       connection.setInstanceFollowRedirects(false); 
	       connection.setRequestMethod("POST"); 
	       connection.setRequestProperty("Content-Type", "audio/x-flac; rate=16000"); 
	       connection.setRequestProperty("User-Agent", "speech2text"); 
	       connection.setConnectTimeout(60000);
	       connection.setUseCaches (false);
	       	
	       DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
	       wr.write(data);
	       wr.flush();
	       wr.close();
	       connection.disconnect();
	       
	       System.out.println("Done");
	       
	       BufferedReader in = new BufferedReader(
	               new InputStreamReader(
	               connection.getInputStream()));
			String decodedString;
			while ((decodedString = in.readLine()) != null) {
			System.out.println(decodedString);
			}*/
	
	
	
	}
};
