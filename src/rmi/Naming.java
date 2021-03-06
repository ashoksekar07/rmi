package rmi;

import java.io.IOException;

import rmi.registry.Registry;
import rmi.registry.RegistryInterface;
import rmi.registry.RegistryStub;
import rmi.server.Server;


public class Naming {
	public static void bind(String serverName, Object obj, String ip, int port){
		
		Server server = new Server(serverName, obj);
		if(!server.registServer(ip, port)){
			System.err.println("error in registering server:"+serverName);
			return;
		}
		
		new Thread(server).start();
		System.out.println("successfully registerd \""+serverName+"\" in registry");
	}
	
	public static Object lookup(String serverURL, int port){
		try {
			rmiParser parser = new rmiParser(serverURL);
			String name = parser.getName();
			String ip = parser.getIp();
			
			RemoteRef remoteRegistry = new RemoteRef(ip, port);
			
			RegistryInterface reg  = new RegistryStub(remoteRegistry);
			
			try {
				return reg.lookup(name);
			} catch (Remote440Exception e) {
				e.printStackTrace();
				return null;
			}
			
		} catch (IOException e) {
			// if parse the url fails, return false.
			System.err.println("cannot parse the url");
			return null;
		}
	}
	
	

}
