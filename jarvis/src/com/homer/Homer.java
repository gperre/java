package com.homer;



import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import com.homer.actions.ActionList;
import com.homer.modules.Actions;
import com.homer.modules.CaptureModule;
import com.homer.modules.VoixModule;
import com.sun.net.httpserver.HttpServer;

public class Homer {

	public static void main(String[] args) throws Exception {
		
		System.out.print("Starting server ... ");
		
		
		 InetSocketAddress addr = new InetSocketAddress(1978);
		    HttpServer server = HttpServer.create(addr, 0);
		    Map programmes = new HashMap();
		    server.createContext("/", new ServeurHTTP(programmes));
		    server.setExecutor(Executors.newCachedThreadPool());
		    server.start();
		    
		//Thread.sleep(2000);
		//VoixModule.prononcer("Le serveur est d�marr� et �coute le port " + addr.getPort());
		System.out.println("listening port : "+addr.getPort());
		//Thread.sleep(10000);
		
		
		//VoixModule.prononcer("D�marrage du module de reconnaissance vocale.");
		//Thread.sleep(5000);
		VoixModule.prononcer("Le module JARVIS est op�rationnel monsieur !");
		//System.out.println("Waking up Homer ... Good to see you again.");
		Thread.sleep(6000);
		//capturer l'ordre
		while(true) {
			if(CaptureModule.capture(5000)){
				String phrase = VoixModule.comprendre("d:/temp/capture.flac");
				System.out.println(phrase);
				Actions a = null;
				if("extinction des feux".equals(phrase)){
					a = new Actions(ActionList.EXTINCTION);
				} else if(phrase.indexOf(" heure")!=-1 ){
					 a = new Actions(ActionList.HEURE);
				}else if(phrase.indexOf("video")!=-1 || phrase.indexOf("vid�o")!=-1){
					 a = new Actions(ActionList.VIDEO);
				} else if(phrase.indexOf("commande")!=-1 || phrase.indexOf("terminal")!=-1){
					 a = new Actions(ActionList.CMD);
				}
				else if(phrase.indexOf("deconnect")!=-1 || phrase.indexOf("d�connect")!=-1 || phrase.indexOf("bye")!=-1){
					 a = new Actions(ActionList.EXIT);
					 VoixModule.prononcer("Tr�s bien monsieur, je d�connecte le syst�me.");
					 Thread.sleep(5000);
					 server.stop(0);
					 String reponse = a.process();
					 break;
					 
				}
				
				else {
					 a = new Actions(ActionList.NONE);
				}
				
				String reponse = a.process();
				VoixModule.prononcer(reponse);
				Thread.sleep(2000);
				
			}
		}

	}

}
