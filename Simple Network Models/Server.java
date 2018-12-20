/*
Richard Beech - Mid Term
ITCS - 3166-051 - Spring 2017
2/23/17
*/

import java.util.Scanner;
import java.util.Random;
import java.io.PrintStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.io.IOException;

public class Server{

   public static void main(String[] args) throws IOException
   {
      
      // Random numbers generated from 10 - 20 to serve as vender inventory
      Random rnd = new Random();
      int bananaCnt = rnd.nextInt(11) + 10;
      int orangeCnt = rnd.nextInt(11) + 10;
      int pineappleCnt = rnd.nextInt(11) + 10;
      int watermelonCnt = rnd.nextInt(11) + 10;
      
      // Vars created to represent pricing for various fruits
      int bananaCost = 2;
      int orangeCost = 3;
      int pineappleCost = 5;
      int watermelonCost = 8;
      
      // ServerSocket object created to connect to Client
      ServerSocket listen = new ServerSocket(1342);
      System.out.println("Waiting for connection....");
      
      // Socket object created to accpet incoming values from Client
      Socket ss = listen.accept();
      System.out.println("Connection Established");
      
      // Scanner object to read values recieved from Client
      Scanner GetMessage = new Scanner(ss.getInputStream());
      
      // PrintStream object used to pass values back to Client
      PrintStream p = new PrintStream(ss.getOutputStream());
      
      // Using PrintStream object to pass inventory values to Client via socket
      p.println(bananaCnt);
      p.println(orangeCnt);
      p.println(pineappleCnt);
      p.println(watermelonCnt);
      
      /*Receiving input from Client via scanner object
        Then calculating pricing and returing it to Client*/
      int bananas = GetMessage.nextInt();
      int bananaPrc = bananas * bananaCost;
      p.println(bananaPrc);
      
      int oranges = GetMessage.nextInt();
      int orangePrc = oranges * orangeCost;
      p.println(orangePrc);
      
      int pineapples = GetMessage.nextInt();
      int pineapplePrc = pineapples * pineappleCost;
      p.println(pineapplePrc);
      
      int watermelons = GetMessage.nextInt();
      int watermelonPrc = watermelons * watermelonCost;      
      p.println(watermelonPrc);      
      
      // objs closed for security
      listen.close();
      ss.close();
      GetMessage.close();
      p.close();
   
   }

}