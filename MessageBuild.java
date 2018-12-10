package com.example.freemanaeswitch.myapplication.MessageCommunication;
import com.example.freemanaeswitch.myapplication.Connection;
import com.example.freemanaeswitch.myapplication.Encryption.AES;
import com.example.freemanaeswitch.myapplication.Encryption.RSAEncryption;
import com.example.freemanaeswitch.myapplication.Encryption.ResponseSpliter;
import com.example.freemanaeswitch.myapplication.Encryption.StringSpliterRSAKey;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class MessageBuild  {

    private StringSpliterRSAKey stringSpliterRSAKey=new StringSpliterRSAKey();
    private RSAEncryption rsaEncryption=new RSAEncryption();
    private ResponseSpliter responseSpliter=new ResponseSpliter();
    private AES aes=new AES();
    private String response;
    public  static String RSAKEY;
    private String encryptedString;
    private String AESKey;
    private String decryptedString;
    private String V_finalResponse;
    private Connection connection;

   public String doCom(int option, String vCommand) throws IOException, GeneralSecurityException {
       connection = new Connection();
       AESKey=getAESKEy();
       switch (option) {
           case 1:
               decryptedString=sendRSACommand();
               if(responseSpliter.response(decryptedString).equalsIgnoreCase("OK")){
                   V_finalResponse=sendAESCommand(vCommand);
                   sendConfirmMessg();
               }else if(responseSpliter.response(decryptedString).equalsIgnoreCase("ER")){
                   sendConfirmMessg();
               }
           break;
           case 2:
               decryptedString=sendRSACommand();
               if(responseSpliter.response(decryptedString).equalsIgnoreCase("OK")){
                   V_finalResponse=sendAESCommand(vCommand);
                   sendConfirmMessg();
               }else if(responseSpliter.response(decryptedString).equalsIgnoreCase("ER")){
                   sendConfirmMessg();
               }

               break;
           case 3:
               decryptedString=sendRSACommand();
               if(responseSpliter.response(decryptedString).equalsIgnoreCase("OK")){
                   V_finalResponse=sendAESCommand(vCommand);
                   sendConfirmMessg();
               }else if(responseSpliter.response(decryptedString).equalsIgnoreCase("ER")){
                   sendConfirmMessg();
               }
               break;
       }
       return V_finalResponse;
   }
    private String getAESKEy() throws NoSuchAlgorithmException {
        KeyGenerator gen = KeyGenerator.getInstance("AES");
        gen.init(128); /* 128-bit AES */
        SecretKey secret = gen.generateKey();
        byte[] binary = secret.getEncoded();
        String text = String.format("%032X", new BigInteger(+1, binary));
        System.out.println(text);
       return text;
   }
    private String sendRSACommand() throws IOException, GeneralSecurityException {

       response = connection.doConnection(0, "RSA");
       RSAKEY = stringSpliterRSAKey.splitFunction(response);
       encryptedString = rsaEncryption.encrptData(RSAKEY, "KEx 4|" + AESKey + "|");
       response = connection.doConnection(103, encryptedString);
       decryptedString = aes.decrypt(response, AESKey);
       System.out.println("Decrypted Response " + decryptedString);

       return decryptedString;
   }
    private String sendAESCommand(String vCommand) throws GeneralSecurityException, IOException {

        encryptedString = aes.encrypt(vCommand, AESKey);
        System.out.println("Encrypted message " + encryptedString);
        response = connection.doConnection(4, encryptedString);
        decryptedString = aes.decrypt(response, AESKey);
        System.out.println("Response " + decryptedString);

        return decryptedString;
    }
    private void  sendConfirmMessg() throws GeneralSecurityException, IOException {

        encryptedString = aes.encrypt("@E", AESKey);
        System.out.println("Encrypted message " + encryptedString);
        response = connection.doConnection(4, encryptedString);
        decryptedString = aes.decrypt(response, AESKey);
        System.out.println("Response " + decryptedString);

    }






}
