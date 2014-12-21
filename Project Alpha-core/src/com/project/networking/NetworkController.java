package com.project.networking;

import java.io.IOException;
/*
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
*/

public class NetworkController {
	
	private boolean isHostingSetUp;
	private boolean isDiscoverySetUp;
	
	private NetworkDiscoveryListener listener = null;
	
	//private JmDNS dns = null;
	
	
	/*public MultiplayerServer serverWithEvent(ServiceEvent event)
	{
		MultiplayerServer s = new MultiplayerServer();
		s.address = event.getInfo().getHostAddress();
		s.name = event.getInfo().getNiceTextString();
		s.adminName = "Unknown";
		
		return s;
	}
	
	public boolean setUpHosting()
	{
		if (!isHostingSetUp)
		{
			try {
				dns = JmDNS.create();
			} catch (IOException e1) {
				e1.printStackTrace();
				return false;
			}
		    ServiceInfo info = ServiceInfo.create("projectalpha","Messenger",80,"Messenger chat thingy");
		    try {
				dns.registerService(info);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		    
		    System.out.println("Set up DNS server: " + info.toString());
		    
		    
		    
		    //TODO: Set up socket server
		    
		    isHostingSetUp = true;
		    
		    return true;
		}
		else
			return false;
	}
	
	public NetworkDiscoveryListener getListener(){ return listener; }
	
	
	public boolean setUpListeningWithListener(NetworkDiscoveryListener listener)
	{
		this.listener = listener;
		
		if (!isDiscoverySetUp)
		{
			
			dns.addServiceListener("_projectalpha._tcp.local.", new ServiceListener(){
		    	@Override
		    	public void serviceAdded(ServiceEvent event){
		    		System.out.println("Service added: " + event.toString());
		    	}

				@Override
				public void serviceRemoved(ServiceEvent event) {
					System.out.println("Service removed: " + event.toString());
					
					getListener().lostServer(serverWithEvent(event));
				}

				@Override
				public void serviceResolved(ServiceEvent event) {
					System.out.println("Service resolved: " + event.toString());
					
					getListener().foundServer(serverWithEvent(event));
				}
		    });
			
			isDiscoverySetUp = true;
		}
		
		return isDiscoverySetUp;
	}*/
}
