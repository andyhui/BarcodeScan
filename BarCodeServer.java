package com.BarCodeServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class BarCodeServer {
	private final static int PORT = 2224;

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String clientSentence;
		String capitalizedSentence;
		ServerSocket barCodeServerSocket = new ServerSocket(PORT);
		while(true){
			Socket connSocket = barCodeServerSocket.accept();
			BufferedReader inFromclient = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connSocket.getOutputStream());
			clientSentence = inFromclient.readLine();
			System.out.println("Received " + clientSentence);
			capitalizedSentence = clientSentence.toUpperCase() + '\n';
			outToClient.writeBytes(capitalizedSentence);
		}

	}

}
