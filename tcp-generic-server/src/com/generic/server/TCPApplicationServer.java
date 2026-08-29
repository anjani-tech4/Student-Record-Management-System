package com.generic.server;
import com.generic.server.net.*;
import com.generic.server.exceptions.*;
import java.io.*;
import java.net.*;
import java.util.*;
public class TCPApplicationServer
{
private ServerSocket serverSocket;

private int port;
private Map<String, String> requestHandlersClassNameMap;
private RequestHandler requestHandler;

public TCPApplicationServer(Map<String, String> requestHandlersClassNameMap) throws TCPApplicationServerException
{
this.requestHandlersClassNameMap=requestHandlersClassNameMap;
try
{
this.port=ServerConfiguration.PORT;
this.serverSocket=new ServerSocket(this.port);
this.requestHandler=RequestHandler.getRequestHandlerInstance(this.requestHandlersClassNameMap);
}catch(Exception exception)
{

throw new TCPApplicationServerException(exception.getMessage());

}
}
public void start() throws TCPApplicationServerException 
{
try
{
Socket clientSocket;
RequestProcessor requestProcessor;
System.out.println("Server is ready to accept connections on port : "+this.port);
while(true)
{
clientSocket=this.serverSocket.accept();
requestProcessor=new RequestProcessor(clientSocket, this.requestHandler);
}
}
catch(Exception exception)
{
throw new TCPApplicationServerException(exception.getMessage());
}
}
}