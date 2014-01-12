package com.homer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
 




import com.homer.actions.ActionList;
import com.homer.modules.Actions;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
 
public class ServeurHTTP implements HttpHandler {
  public static void main(String[] args) throws IOException {
    InetSocketAddress addr = new InetSocketAddress(1978);
    HttpServer server = HttpServer.create(addr, 0);
    Map programmes = new HashMap();
    server.createContext("/", new ServeurHTTP(programmes));
    server.setExecutor(Executors.newCachedThreadPool());
    server.start();
    System.out.println("Le serveur en ecoute sur le port: "+addr.getPort());
  }
//}
 
//class Gestionnaire implements HttpHandler {
 
	Map progs = null;
	public ServeurHTTP(Map progs){
		this.progs=progs;
	}
  /* (non-Javadoc)
 * @see com.sun.net.httpserver.HttpHandler#handle(com.sun.net.httpserver.HttpExchange)
 */
public void handle(HttpExchange exchange) throws IOException {
    String methodeRequete = exchange.getRequestMethod();
    System.out.println(exchange.getRequestURI().getPath());
    if (methodeRequete.equalsIgnoreCase("GET")) {
      Headers reponseEntete = exchange.getResponseHeaders();
      reponseEntete.set("Content-Type", "text/plain");
      exchange.sendResponseHeaders(200, 0);
 
      OutputStream reponse = exchange.getResponseBody();
      /*Headers requeteEntete = exchange.getRequestHeaders();
      Set<String> keySet = requeteEntete.keySet();
      Iterator<String> iter = keySet.iterator();
      while (iter.hasNext()) {
        String key = iter.next();
        List values = requeteEntete.get(key);
        String s = key + " = " + values.toString() + "";
        reponse.write(s.getBytes());
      }*/
      String path = exchange.getRequestURI().getPath();
      
      if("/jarvis/extinction".equals(path)){
    	  Actions a = new Actions(ActionList.EXTINCTION);
    	  reponse.write(a.process().getBytes());
    	 
      }
      
      /*else if("/jarvis/musique".equals(path)){
    	 
    	  try {
    		  boolean marche = (progs.get(path)!=null);
    		  
    		  if(!marche){
    			  Actions a = new Actions(ActionList.MUSIQUE);
    	    	  reponse.write(a.process().getBytes());
    			  //reponse.write("Jarvis: Un peu de musique monsieur.".getBytes());
    			 /* Runtime runtime=Runtime.getRuntime();
    			  Process p = runtime.exec(new String[] { "C:\\Users\\greg\\AppData\\Roaming\\Spotify\\spotify.exe" } );
    			  progs.put(path, p);* /
    		  } else {
    			  reponse.write("Jarvis: J'arrete la musique monsieur.".getBytes());
    			  Process p = (Process)progs.get(path);
    			  progs.remove(path);
    			 
    			  p.destroy();
    			 
    			 
    		  }
    	  
    	  
    	  }catch(Exception ex){
    		  ex.printStackTrace();
    	  }
    	  
      }*/
    	  
      
      reponse.close();
    }
  }

}