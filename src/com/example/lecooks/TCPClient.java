package com.example.lecooks;

//TCPClient.java
// Socket de comunicacao - cliente
import java.net.*;
import java.io.*;

//import android.os.Environment;


public class TCPClient {
	
	static String ip = "localhost";
	public static int serverPort = 6880;
	public static String st;
	public static String data = "Mensagem teste de conexao SocketClient";
	public static void ClientSocket (String args[]){
		Socket s = null;
	    try{
	    	//int serverPort = 6880;
	        

	        /*File f = new File(Environment.getExternalStorageDirectory(),"new");
	        if(!f.exists())
	        {
	        	f.mkdir();
	        }
	        File file = new File("new.txt");
	        String file_name = Environment.getExternalStorageDirectory() + "/new.txt";
	        FileWriter w = new FileWriter(file_name);
	        BufferedWriter out = new BufferedWriter(w);
	        out.write(data);
	        out.flush();
	        out.close();*/
	                
	               
	        s = new Socket(ip, serverPort);
	        DataInputStream input = new DataInputStream( s.getInputStream());
	        DataOutputStream output = new DataOutputStream( s.getOutputStream());
	           
	        //Enviando tamanho da mensagem
	        output.writeInt((int) data.length());
	        
	        //Enviando mensagem
	        output.writeBytes(data); // UTF is a string encoding
	               
	        //Lendo tamanho da mensagem de resposta
	        int nb = input.readInt();
	        byte[] digit = new byte[nb];
	        //Lendo mensagem de resposta
	        for(int i = 0; i < nb; i++)
	        	digit[i] = input.readByte();
	           
	        st = new String(digit); 
	   }
	   catch (UnknownHostException e){
		   System.out.println("Sock:"+e.getMessage());}
	   catch (EOFException e){
	       System.out.println("EOF:"+e.getMessage()); }
	   catch (IOException e){
	       System.out.println("IO:"+e.getMessage());}
       finally {
    	   if(s!=null)
    		   try {s.close();
	   }
	       catch (IOException e) {/*falhou ao fechar conexão*/}
	   }
	}
}