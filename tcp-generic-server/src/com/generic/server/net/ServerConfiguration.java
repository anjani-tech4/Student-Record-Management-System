package com.generic.server.net;
import java.io.*;
public class ServerConfiguration
{
final static private String configurationFile="server.cfg";
final static public int PORT;
static
{
int p=0;
String port="";

try
{
File file=new File(configurationFile);
if(file.exists()==false)
{
throw new IOException("configurationFile (server.cfg) missing");
}
RandomAccessFile randomAccessFile;
randomAccessFile= new RandomAccessFile(file, "rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new IOException("configurationFile (server.cfg) missing");

}
String line;

line=randomAccessFile.readLine().toLowerCase().trim();

randomAccessFile.close();
 if(line.startsWith("port="))
{
port=line.substring(5);
}

if( port.length()==0)
{
throw new IOException("Configuration file (server.cfg) does not contain configuration as specified in document, refer installation document.");

}
}catch(IOException  ioException)
{
System.out.println(ioException.getMessage());
System.exit(0);
}
if(port.length()==0)
{
System.out.println("Configuration file (server.cfg) does not contain configuration as specified in document, refer installation document.");
System.exit(1);
}
else
{      // else open
try
{
p=Integer.parseInt(port);
if(p<1 || p>65535)
{
System.out.println("Configuration file (server.cfg) does not contain configuration as specified in document, refer installation document.");
System.exit(1);
}
} //try (inside else) closed
catch(NumberFormatException numberFormatException)
{
System.out.println("Configuration file (server.cfg) does not contain configuration as specified in document, refer installation document.");
System.exit(1);
}
}
PORT=p;
}
}