//example of factory class
package com.generic.server.net;
import java.util.*;
import java.io.*;
import com.generic.server.exceptions.*;

public class RequestHandler
{
private RequestHandler(){}
private static RequestHandler requestHandler;

private static Map<String,String> requestHandlersClassNameMap;


private static Map<String, RequestHandlerInterface> requestHandlersMap;


//following is a factory method
public static RequestHandler getRequestHandlerInstance(Map<String, String> requestHandlersClassNameMap) throws TCPApplicationServerException
{
if(requestHandlersClassNameMap==null) throw new TCPApplicationServerException("RequestHandlersClassNameMap cannot be null");
if(requestHandler==null)
{
requestHandler=new RequestHandler();
requestHandler.requestHandlersClassNameMap=requestHandlersClassNameMap;
}
return requestHandler;
}



public static RequestHandlerInterface getRequestHandler(String actionName)
{
RequestHandlerInterface requestHandlerInterface;
requestHandlerInterface=requestHandlersMap.get(actionName);
if(requestHandlerInterface==null)
{
String className=requestHandlersClassNameMap.get(actionName);
if(className!=null)
{
try
{
Class c=Class.forName(className);
requestHandlerInterface=(RequestHandlerInterface)c.newInstance();
requestHandlersMap.put(actionName,requestHandlerInterface);
}catch(ClassNotFoundException classNotFoundException)
{
// for debugging
System.out.println(classNotFoundException.getMessage());
}
catch(Exception exception)
{
System.out.println(exception.getMessage());
}
}
}
return requestHandlerInterface;
}
}
