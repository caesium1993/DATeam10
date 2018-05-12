package client;

import java.io.DataInputStream;

import java.io.DataOutputStream;

import java.io.IOException;

import java.net.Socket;

public class Client {

	private static boolean flag = false;
	private static int value;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String ip = "127.0.0.1";

        int port = 8000;

        Thread thread = new Thread(new ScannerThread());
        thread.start();

        try(Socket socket = new Socket(ip, port)){

            // Input and ouput stream

            DataInputStream input = new DataInputStream(socket.getInputStream());

            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            // Get input from user

            // User input
			int message_send=0;
            while(true) {
            	if(flag == true){
            	message_send = value;
            	flag=false;
            	output.writeUTF(String.valueOf(message_send));
            	System.out.printf("Send %s to Server.\n",message_send);
            	output.flush();
            	 }
            	
            	if(input.available() > 0)
            	{
            	String message_rec = input.readUTF();
            	System.out.printf("client.client Receive: %s. \n", message_rec);
            	}
            }

        }catch (IOException e){

            System.out.println(e);

        }
	}

	public static void setValue(int value) {
		Client.flag = true;
		Client.value = value;
	}
}
