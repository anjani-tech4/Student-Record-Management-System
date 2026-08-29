package com.generic.server.net;
import java.io.*;
import java.net.*;
import java.util.*;
import com.generic.server.exceptions.*;

public class RequestProcessor extends Thread
{
private Socket clientSocket;
private RequestHandler requestHandler;
public RequestProcessor(Socket clientSocket,RequestHandler requestHandler)
{
this.clientSocket=clientSocket;
this.requestHandler= requestHandler;
start();
}
public void run()
{
processRequest();
}
private void processRequest()
{
try
{
InputStream inputStream;
inputStream=clientSocket.getInputStream();
InputStreamReader inputStreamReader;
inputStreamReader= new InputStreamReader(inputStream);
StringBuffer stringBuffer;
stringBuffer=new StringBuffer();
int i;
while(true)
{
i=inputStreamReader.read();
if(i==-1 || i=='#') break;
stringBuffer.append((char)i);
}
String requestString=stringBuffer.toString();
String splits[]= requestString.split(",");
String actionName=splits[0];
RequestHandlerInterface requestHandlerInterface;
requestHandlerInterface=RequestHandler.getRequestHandler(actionName);
String responseString=requestHandlerInterface.performAction(actionName, splits);
OutputStream outputStream=clientSocket.getOutputStream();
OutputStreamWriter outputStreamWriter;
outputStreamWriter=new OutputStreamWriter(outputStream);
outputStreamWriter.write(responseString);
outputStreamWriter.flush();
outputStreamWriter.close();
outputStream.close();
clientSocket.close();



}catch(IOException ioException)
{
// for debugging
System.out.println(ioException.getMessage());
}
}
}