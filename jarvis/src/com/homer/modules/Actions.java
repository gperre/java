package com.homer.modules;

import java.io.File;
import java.io.IOException;

import com.homer.actions.ActionList;

public class Actions {
	ActionList  action;
public Actions(ActionList l) {
	action=l;
}

public String process() throws IOException{
	if(action.equals(ActionList.EXTINCTION)){
		  Runtime runtime = Runtime.getRuntime();
    	  runtime.exec(new String[] { "C:\\Windows\\System32\\shutdown.exe","-s", "-t" ,"10" } );
    	  return "Je ferme tout monsieur, à bientôt!";
	}else if(action.equals(ActionList.HEURE) || action.equals(ActionList.DATE)){
		 
    	  return "Nous sommes le " + new java.util.Date().toString();
	} else if(action.equals(ActionList.VIDEO)){
		  Runtime runtime = Runtime.getRuntime();
    	  runtime.exec(new String[] { "C:\\Program Files (x86)\\VideoLAN\\VLC\\vlc.exe" } );
    	  return "C'est fait.";
	} else if(action.equals(ActionList.CMD)){
		  Runtime runtime = Runtime.getRuntime();
    	  runtime.exec(new String[] { "d:\\terminal.cmd" } );
    	  return "C'est bon.";
	}
	else if(action.equals(ActionList.EXIT)){
		
		if(new File("d:/temp/scan.flac").exists())
			new File("d:/temp/scan.flac").delete();
		if(new File("d:/temp/surv.flac").exists())
			new File("d:/temp/surv.flac").delete();
		if(new File("d:/temp/capture.flac").exists())
			new File("d:/temp/capture.flac").delete();
		if(new File("d:/temp/reponse.mp3").exists())
			new File("d:/temp/reponse.mp3").delete();
		
		
  	  return "A bientôt monsieur.";
	}
	return "Je ne comprend pas l'ordre.";
}
}
