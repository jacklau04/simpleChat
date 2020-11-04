import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import common.ChatIF;

public class ServerConsole implements ChatIF {
	
	//Class variables *************************************************
	  
	/**
	 * The default port to connect on.
	 */
	final public static int DEFAULT_PORT = 5555;	
	
	//Instance variables **********************************************
	  
	/**
	 * The instance of the client that created this ConsoleChat.
	 */
	EchoServer server;
	
	
	//Constructors ****************************************************

	/**
	  * Constructs an instance of the ServerConsole UI.
	  *
	  * @param host The host to connect to.
	  * @param port The port to connect on.
	  */
	  
	//Constructors ****************************************************
	public ServerConsole(int port) {
        server = new EchoServer(port);
        try {
			server.listen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public void accept() 
	  {
//		try {
			//Enter data using BufferReader 
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String cMessage;
			
			try {
				while ((cMessage = reader.readLine()) != null) {
					server.handleMessageFromServerConsole(cMessage);
					this.display(cMessage);
     }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	  }
	//Instance methods ************************************************
	  
	/**
	  * This method waits for input from the console.  Once it is 
	  * received, it sends it to all the client 
	  */

	@Override
	public void display(String message) {
		// TODO Auto-generated method stub
		if (message.startsWith("#")) {
            return;
        }
        System.out.println("SERVER MSG>" + message);
		
	}
	
	
	public static void main(String[] args) 
	  {
	    int port;
	    
	    
	    /*
	     * Similar as 
	     * Part 1 b Client Side but server this time
	     * it obtains the port number from the command line
	     * If the port is omitted from the command line, then the default value should still be used 
	     */
	    try{
	    	port = Integer.parseInt(args[0]);
	    }
	    catch(Exception e){
	    	port = DEFAULT_PORT;
	    }
	    
	    ServerConsole server= new ServerConsole(port);
	    server.accept();  //Wait for console data
	  }
	

}
