import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;


public class Main {
	
	public static Main instance = null;
	
	public static Main getInstance()
	{
		return instance;
	}
	 
	  
	  // java.lang.Object shell size in bytes:
	  public static final int OBJECT_SHELL_SIZE   = 8;
	  public static final int OBJREF_SIZE         = 4;
	  public static final int LONG_FIELD_SIZE     = 8;
	  public static final int INT_FIELD_SIZE      = 4;
	  public static final int SHORT_FIELD_SIZE    = 2;
	  public static final int CHAR_FIELD_SIZE     = 2;
	  public static final int BYTE_FIELD_SIZE     = 1;
	  public static final int BOOLEAN_FIELD_SIZE  = 1;
	  public static final int DOUBLE_FIELD_SIZE   = 8;
	  public static final int FLOAT_FIELD_SIZE    = 4;
	  
	  public static void main(String[] args)
	  {
		  
		  Runtime.getRuntime().addShutdownHook(new Thread()
		  {
			  public void run() {
				  System.out.println("Shutting down...:" + Main.getInstance() + " and variable:" + Main.instance);
				  Main.getInstance().tearDown();
			  }
		  });
		  
		   new Main();
	  }
	  
	  /*CONSTANTS*/
	  private final int port = 1044;
	  
	  /*IVARS*/
	  private ServerSocket socket;
	  private Socket clientSocket = null;
	  private JmDNS dns;
	  
	  /*METHODS*/
	  public Main()
	  {
		  super();
		  instance = this;
	    //Hello
	    System.out.println("Project-alpha Server by Justus & Benno");
	    System.out.println("+------------------------------------+");
	    
	    while (true) {
	      startUp();
	      listen();
	      tearDown(); 
	    }
	  }
	  
	  private void startUp()
	  {
	    //Open socket
	    try { 
	      socket = new ServerSocket(port);
	    } catch(Exception e) {
	      System.out.println("Error opening socket: " + e.toString());
	    }
	    try { 
	      //Get inet address
	      System.out.println("Server address: " + socket.getInetAddress()+  " Server IP: " + InetAddress.getLocalHost().getHostAddress());    
	    } catch(Exception e) {
	      System.out.println("Error getting local IP address: " + e.toString());
	    }
	    
	    ServiceInfo info = ServiceInfo.create("projectalpha","PAServer",port,"GG Server");
	    try {
	    	dns = JmDNS.create();
			dns.registerService(info);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    System.out.println("Set up DNS server: " + info.toString());
	    
	    //Accept connections
	    try { 
	      clientSocket = socket.accept();
	    } catch(Exception e) {
	      System.out.println("Error accepting from socket: " + e.toString());
	    }
	    
	    //Check socket
	    if (clientSocket != null) {
	      System.out.println("Client connected: " + clientSocket.toString());
	    } // end of if
	    
	  }
	  
	  private void tearDown()
	  {
	    //Close socket
	    try { 
	      socket.close();
	      dns.unregisterAllServices();
	      System.out.println("Unregistered services, waiting...");
	      Thread.sleep(10*1000);
	      dns.close();
	      Thread.sleep(2*1000);
	    } catch(Exception e) {
	      
	    } finally {
	      
	    } // end of try
	  }
	  
	  private void listen()
	  {
	    try { 
	      PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
	      BufferedReader in = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()) );
	      
	      char[] input = new char[1];
	      
	      System.out.println("Listening to client messages!");
	      
	      while(!in.ready())
	      {
	        
	      }
	      System.out.println("Now ready!");
	      while (true)
	      {
	        in.read(input,0,1);
	        System.out.println("received: " + input[0]);
	        
	      } //end of for loop
	    } catch(Exception e){
	      System.out.println("Error while reading client socket " + e.toString());
	      //in.close();
	    }
	  }
}
