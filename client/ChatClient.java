// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI;
private String loginID; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginID, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.loginID = loginID;
    this.clientUI = clientUI;
    
    this.openConnection();

    this.sendToServer("#login " + loginID);

    
    
  }
  

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
      sendToServer(message);
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  

  
  // my work start here
  /** 
   * Part 1 a Client Side
   * override the client responds to the shutdown of the server by printing a message
   * saying the server has shut down, and quitting
   * 
   * Documentation explain
   * Hook method called after the connection has been closed. 
   * The default implementation does nothing. 
   * The method may be overriden by subclasses to perform special processing such as cleaning up and terminating, 
   * or attempting to reconnect.
   * 
   * so no need to use quit 
   */

  @Override
  public void connectionClosed() {
     System.out.println("SERVER SHUTTING DOWN! DISCONNECTING!");
     }
  
  
  /**
   * Part 1 a Client Side
   * The client responds to error in connecting, override it so it print out a statement 
   * 
   * Docuemntation explain
   * Hook method called each time an exception is thrown by the client's thread 
   * that is reading messages from the server. The method may be overridden 
   * by subclasses. Most exceptions will cause the end of the reading 
   * thread except for ClassNotFoundException<\code>s received when an object 
   * of unknown class is received and for the RuntimeExceptions that can be thrown 
   * by the message handling method implemented by the user.
   * 
   * So I need to implement quit after the error to ensure the quitting process.
   */
  
  @Override
  public void connectionException(Exception exception) {
	  /// based on test
      System.out.println("WARNING - The server has stopped listening for connections");
      quit();
  }
  
  /**
   * Part 2 a Client Side
   * Add a mechanism so that the user of the client can type commands that perform 
   * special functions. Each command should start with the ‘#’ symbol – in fact, 
   * anything that starts with that symbol should be considered a command
   */
  public void handleMessageFromClientConsoleUI(String ccMessage) {
	  
	  // check if the message is from command or not by "#"
      if (ccMessage.startsWith("#")) {
    	  
    	  // break the message into 2 part, first part is the command 
    	  // second part is the actual message
          String[] arr = ccMessage.split(" ");
          String command = arr[0];
          
          switch (command) {
              // #quit Causes the client to terminate gracefully. 
              // Make sure the connection to the server is terminated before exiting the program
              case "#quit":
                  quit();
                  break;
              
              // #logoff Causes the client to disconnect from the server, but not quit
              case "#logoff":
			  try {
				  closeConnection();
			  } catch (IOException e1) {
		    	// TODO Auto-generated catch block
				  e1.printStackTrace();
			  }
                  break;
              
              // #sethost <host> Calls the setHost method in the client. 
              // Only allowed if the client is logged off; displays an error message otherwise.   
              case "#sethost":
                  if (this.isConnected() == true) {
                      System.out.println("Erorr, It is already connected");
                  } else {
                      this.setHost(arr[1]);
                  }
                  break;
              
              // #setport <port> Calls the setPort method in the client, 
              // with the same constraints as #sethost
              case "#setport":
            	  if (this.isConnected() == true) {
                      System.out.println("Erorr, It is already connected");
                  } else {
                      this.setPort(Integer.parseInt(arr[1]));
                  }
                  break;
              
              // #login Causes the client to connect to the server. 
              // Only allowed if the client is not already connected; 
              // displays an error message otherwise.
              case "#login":
            	  if (this.isConnected() == true) {
                      System.out.println("Erorr, It is already connected");
                  } else {
                      try {
						this.openConnection();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                  }
                  break;
              
              // #gethost Displays the current host name
              case "#gethost":
                  System.out.println("Current host name is " + this.getHost());
                  break;
                  
              // #getport Displays the current port number
              case "#getport":
                  System.out.println("Current port is number " + this.getPort());
                  break;
                  
              // when it doesn't detect any valid command
              default:
                  System.out.println("Invalid command");
                  break;
          }
      }
          else {
        	  try {
				sendToServer(ccMessage);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	        	quit();

			}
          }
     
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  
}
//End of ChatClient class
