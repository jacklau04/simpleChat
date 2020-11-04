// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 



import java.io.*;
import ocsf.server.*;
//import simplechat.common.Message;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   * 
   *   
  /****
   * This is the 3 c part
   * Arrange for the server to receive the #login command from the client. 
   * It should behave according to the following rules:
   *
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client)
  {
	String message = msg.toString();
	if (message.startsWith("#")){
		String[] arr = message.split(" ");
		if (arr[0].equalsIgnoreCase("#login") && arr.length > 1) {
			client.setInfo("loginId", arr[1]);
		}
	}
	else{
		if (client.getInfo("loginId") == null) {
			try {
				client.sendToClient("Please set a loginId before messaging the server!");
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Message received: " + msg + " from " + client.getInfo("loginId"));
			this.sendToAllClients(client.getInfo("username") + ">" + msg);
		}
	}    
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  

  
  // my work start here
  /**
   * prints out a nice message whenever a client connects
   * Overrriding AbstractServer
   * 
   * Documentation
   * Hook method called each time a new client connection is accepted. 
   * The default implementation does nothing. This method does not have to be 
   * synchronized since only one client can be accepted at a time.
   * 
   * You asked me to print out a message whenver a client join, but didn't say to the whom
   * so i assume both the terminal and client who is connecting 
   */
  @Override
  public synchronized void clientConnected(ConnectionToClient client) {
      String message = client.getInfo("loginId") + " has logged on.";
      this.sendToAllClients(message);
      System.out.println(message);
  }
  
  /*
   * print out a nice message whenver a client disconnects
   */
  @Override
  public synchronized void clientDisconnected(ConnectionToClient client) {
	  String message = client.getInfo("loginId")+ " logged off!!";
      this.sendToAllClients(message);
      System.out.println(message);
  }
  
  /*
   * in 1 a we did the exception as well so i assume this part i would also need to create one
   */
  
  @Override
  public synchronized void clientException(ConnectionToClient client, Throwable exception) {
      String message = client.getInfo("loginId") + " logged off!!";
      this.sendToAllClients("SERVER MSG>" + message);
      System.out.println(message);
  }
  
  
  
  /*
   * this part is 2c
   */
  
  public void handleMessageFromServerConsole(String cMessage) {
	  
	  // check if the message is from command or not by "#"
      if (cMessage.startsWith("#")) {
    	  
    	  // break the message into 2 part, first part is the command 
    	  // second part is the actual message
          String arr[] = cMessage.split(" ");
          String command = arr[0];
          
          switch (command) {
              // #quit Causes the server to quit gracefully
              case "#quit":
            	  try {
            		  this.close();
            	  } catch (IOException e1) {
            	     // TODO Auto-generated catch block
            	    System.exit(1);
            	  }
            	  System.exit(0);
            	  break;
              
              //stop Causes the server to stop listening for new clients
              case "#stop":
				  this.stopListening();
                  break;
              
              // #close Causes the server not only to stop listening for new clients, 
              //  but also to disconnect all existing clients  
              case "#close":
			  try {
				  this.close();
			  } catch (IOException e) {
		      // TODO Auto-generated catch block
				  e.printStackTrace();
			  }
                  break;
              
              // #setport <port> Calls the setPort method in the server. 
              //  Only allowed if the server is closed.
              case "#setport":
            	  if (this.isListening() == false && this.getNumberOfClients() == 0) {
                      super.setPort(Integer.parseInt(arr[1]));
                      System.out.println("Port is open and set to " + Integer.parseInt(arr[1]));
                  } else {
                	  System.out.println("the server is not closed!");
                  }
                  break;
              
              // #start Causes the server to start listening for new clients. 
              // Only valid if the server is stopped
              case "#start":
            	  if (this.isListening() == false && this.getNumberOfClients() == 0) {
                      try {
						this.listen();
	                    System.out.println("Server listening for connections on port " +this.getPort());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                  } else {
                	  System.out.println("the sever is already listening & not closed!");
                  }
                  break;
              
              // #getport Displays the current port number
              case "#getport":
                  System.out.println("Current port is number " + Integer.toString(this.getPort()));
                  break;
                  
              // when it doesn't detect any valid command
              default:
                  System.out.println("Invalid command");
                  break;
          }
      } 
      else {
    	  this.sendToAllClients(cMessage);
      }
  }


  
  //Class methods ***************************************************
  // Driver
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(DEFAULT_PORT);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
  
  
  
}
//End of EchoServer class
