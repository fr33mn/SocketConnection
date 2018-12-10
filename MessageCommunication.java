package com.example.freemanaeswitch.myapplication.MessageCommunication;



import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MessageCommunication {

   //Function to Build the message to be sent
    public byte [] messageSendToServer(int intManBte,int msgLeangth,byte [] mmsgByte ) {
        System.out.println("Initializing the 4 Byte process");
        byte[] vTestArray = new byte[4];
        System.out.println("Outgoing message from the client ");
        System.out.println("Message length " +msgLeangth );
        if(msgLeangth>255){
            //Executed if the data to be sent >256
            System.out.println("If length of the message is > 256 and execute ");
            vTestArray[0]=Byte.parseByte(Integer.toString(msgLeangth/256),16);
            vTestArray[1] = (byte)((msgLeangth-((msgLeangth/256)*256))+2);
            vTestArray[2] = (byte)(intManBte);
            vTestArray[3] = Byte.parseByte(Integer.toString(0),16);
        }else{
            //Executed if the data to be sent <256
            System.out.println("If length of the message is < 256 and execute ");
            vTestArray[0] = Byte.parseByte(Integer.toHexString(0), 16);
            vTestArray[1] = Byte.parseByte(Integer.toHexString(msgLeangth + 2), 16);
            vTestArray[2] = (byte)(intManBte);
            vTestArray[3] = Byte.parseByte(Integer.toHexString(0), 16);
        }
        /*Combines the four byte array with the message array bytes
        *of the message to make it one array byte to be sent to the server */
        byte[] vSend = new byte[vTestArray.length + mmsgByte.length];
        System.arraycopy(vTestArray, 0, vSend, 0, vTestArray.length);
        System.arraycopy(mmsgByte, 0, vSend, vTestArray.length, mmsgByte.length);
        System.out.println("Actual Bytes to be sent " + vSend);
        return vSend;

    }

    //Function to interpret the message being receive from srver
    public String messageRecievedFromServer(DataInputStream is, Socket socket) throws IOException {

        System.out.println("Incoming message from the Server ");
        byte[] data = new byte[4];
        is.read(data, 0, data.length);

        /// Use this later to make your code more dynamic
        int vByte1 = data[0];// Length Byte
        int vByte2 = data[1];// Length Byte
        int vByte3 = data[2];// Encryption Byte
        int vByte4 = data[3];// Management Byte

         //Checks length of message
        if(vByte1<0){
            vByte1=vByte1+256;
        }
        if(vByte2<1){
            vByte2=vByte2+256;
        }
        vByte1=vByte1*256;
        vByte2 += vByte1;

        byte[] vMessageDecode = new byte[vByte2 - 2];
        System.out.println("Reading the message from the server ");
        is.read(vMessageDecode, 0, vMessageDecode.length);
        String vEncStringFromServer = new String(vMessageDecode, StandardCharsets.UTF_8);
        System.out.println("Actual message from the server "+vEncStringFromServer);
        return vEncStringFromServer;
    }

}
