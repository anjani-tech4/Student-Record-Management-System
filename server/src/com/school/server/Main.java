package com.school.server;
import com.generic.server.*;
import com.generic.server.exceptions.*;
import java.util.*;
import com.school.net.*;

public class Main
{
public static void main(String []gg)
{
TCPApplicationServer tcpApplicationServer;
Map<String, String> requestHandlersClassNameMap;
requestHandlersClassNameMap = new HashMap<>();
requestHandlersClassNameMap.put(DataProtocol.ADD_STUDENT,"com.school.server.handlers.StudentHandler"
);
requestHandlersClassNameMap.put(DataProtocol.GET_STUDENTS,"com.school.server.handlers.StudentHandler");
try
{
tcpApplicationServer=new TCPApplicationServer(requestHandlersClassNameMap);


tcpApplicationServer.start();
}catch(TCPApplicationServerException exception)
{
System.out.println(exception);
System.exit(1);
}
}
}
