package com.school.net;
import java.io.*;
public class ServerConfiguration
{
final static private String configurationFile="server.cfg";
final static public String IP;
final static public int PORT;
static
{
String ip="";
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
String lines[]=new String[2];
int  i=0;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
lines[i]=randomAccessFile.readLine().toLowerCase().trim();
lines[i]=lines[i].replaceAll(" ","");
i++;
}
randomAccessFile.close();
if(lines[1]==null)
{
throw new IOException("Configuration file (server.cfg) does not contain configuration as specified in document, refer installation document.");
}
for(i=0;i<=1;i++)
{
if(lines[i].startsWith("ip="))
{
ip=lines[i].substring(3);
}else if(lines[i].startsWith("port="))
{
port=lines[i].substring(5);
}
}// for ends
if(ip.length()==0 || port.length()==0)
{
throw new IOException("Configuration file (server.cfg) does not contain configuration as specified in document, refer installation document.");
}
}catch(IOException ioException)
{
System.out.println(ioException.getMessage());
System.exit(0);
}
IP=ip;
PORT=Integer.parseInt(port);
}
}