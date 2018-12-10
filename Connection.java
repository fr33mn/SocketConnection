package com.example.freemanaeswitch.myapplication;

import com.example.freemanaeswitch.myapplication.MessageCommunication.MessageCommunication;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.security.GeneralSecurityException;

public  class Connection {

    //private Data members variables
    private MessageCommunication messageCommunication=new MessageCommunication();
    private DataInputStream is;
    private OutputStream os;
    private static  Socket socketConnection;
    private String vResponseServer;

    public Connection() throws IOException {
        //Initialize connection
        socketConnection = new Socket("xxx.xxx.xxx", 0000);
        is = new DataInputStream(socketConnection.getInputStream());
        os = socketConnection.getOutputStream();
    }


    public  String  doConnection( int manByte,  String vCommand) throws IOException, GeneralSecurityException {
        //Writes message to the server
        os.write(messageCommunication.messageSendToServer(manByte, vCommand.length(), vCommand.getBytes()));
        os.flush();
        //Response message from the server
        vResponseServer = messageCommunication.messageRecievedFromServer(is, socketConnection);
        //returns the response from the server
        return vResponseServer;
}

}



