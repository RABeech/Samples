/*
Richard Beech
Spring 2017
2/23/17
*/

import java.util.Scanner;
import java.util.InputMismatchException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;


public class Client{

   public static void main(String[] args) throws UnknownHostException, IOException
   {
      
      // Socket object created to communicate message to Server
      Socket ClientSocket = new Socket("127.0.0.1",1342);
      
      // Scanner object created to use the ClientSocket, input back from Server
      Scanner ServerScanner = new Scanner(ClientSocket.getInputStream());
      
      // Scanner object for reading input from keyboard
      Scanner keyboard = new Scanner(System.in);
      
      // PrintStream object created to pass the String variable to the Server
      PrintStream p = new PrintStream(ClientSocket.getOutputStream());
      
      // Vars created to store userinput for loop and various fruits
      int userInput = 0;
      int bananas = 0;
      int oranges = 0;
      int pineapples = 0;
      int watermelons = 0;
      int total = 0;
      
      // Inventory for fruits received from Server
      int bananaCnt = ServerScanner.nextInt();
      int orangeCnt = ServerScanner.nextInt();
      int pineappleCnt = ServerScanner.nextInt();
      int watermelonCnt = ServerScanner.nextInt();
      
      // Condition loop to keep program running until told to stop
      while (userInput != 6){
      
         // Furit vender menu presented to user
         System.out.println();
         System.out.println("*Fruit Vender Menu*");
         System.out.println("Please choose from the following items:");
         System.out.println("1 - Purchase Bananas ($2 each)");
         System.out.println("2 - Purchase Oranges ($3 each)");
         System.out.println("3 - Purchase Pineapples ($5 each)");
         System.out.println("4 - Purchase Watermelons ($8 each)");
         System.out.println("5 - Display items chosen to buy");
         System.out.println("6 - Finish Shopping");
         
         // Data validation throw non numeric field values 
         try {
         
            userInput = keyboard.nextInt();
            
            // Decision statments to sort user input to nav fruit vender menu
            if ( userInput == 1){
               System.out.println("The Fruit Vender has " + bananaCnt + " bananas for sell at $2 each");
               System.out.println("How many Bananas would you like to buy?");
               bananas = keyboard.nextInt();
               
               // Data validation to avoid negative quantites
               while (bananas < 1){
                  System.out.println("Food quantities cannot be negative, please enter a valid quantity");
                  bananas = keyboard.nextInt();
               }
               
               // Data validation to avoid overselling inventory
               while (bananas > bananaCnt){
                  System.out.println("You have entered " + bananas + " bananas");
                  System.out.println("The most you can purchase is " + bananaCnt);
                  System.out.println("Please enter a quantity " + bananaCnt + " or below");
                  bananas = keyboard.nextInt();
               }
               
               System.out.println("You have a total of " + bananas + " bananas in your cart.");
            }
            
            // Decision statments to sort user input to nav fruit vender menu cont.
            if ( userInput == 2){
               System.out.println("The Fruit Vender has " + orangeCnt + " oranges for sell at $3 each");
               System.out.println("How many Oranges would you like to buy?");
               oranges = keyboard.nextInt();
               
               while (oranges < 1){
                  System.out.println("Food quantities cannot be negative, please enter a valid quantity");
                  oranges = keyboard.nextInt();
               }
               while (oranges > orangeCnt){
                  System.out.println("You have entered " + oranges + " oranges");
                  System.out.println("The most you can purchase is " + orangeCnt);
                  System.out.println("Please enter a quantity " + orangeCnt + " or below");
                  oranges = keyboard.nextInt();
                  
               }

               System.out.println("You have a total of " + oranges + " oranges in your cart.");
            }
            
            // Decision statments to sort user input to nav fruit vender menu cont.
            if ( userInput == 3){
               System.out.println("The Fruit Vender has " + pineappleCnt + " pineapples for sell at $5 each");
               System.out.println("How many Pineapples would you like to buy?");
               pineapples = keyboard.nextInt();
               
               while (pineapples < 1){
                  System.out.println("Food quantities cannot be negative, please enter a valid quantity");
                  pineapples = keyboard.nextInt();
               }
               
               while (pineapples > pineappleCnt){
                  System.out.println("You have entered " + pineapples + " pineapples");
                  System.out.println("The most you can purchase is " + pineappleCnt);
                  System.out.println("Please enter a quantity " + pineappleCnt + " or below");
                  pineapples = keyboard.nextInt();
               }

               System.out.println("You have a total of " + pineapples + " pineapples in your cart.");
            }
            
            // Decision statments to sort user input to nav fruit vender menu cont.
            if ( userInput == 4){
               System.out.println("The Fruit Vender has " + watermelonCnt + " watermelons for sell at $8 each");
               System.out.println("How many Watermelons would you like to buy?");
               watermelons = keyboard.nextInt();
               
               while (watermelons < 1){
                  System.out.println("Food quantities cannot be negative, please enter a valid quantity");
                  watermelons = keyboard.nextInt();
               }
               
               while (watermelons > watermelonCnt){
                  System.out.println("You have entered " + watermelons + " watermelons");
                  System.out.println("The most you can purchase is " + watermelonCnt);
                  System.out.println("Please enter a quantity " + watermelonCnt + " or below");
                  watermelons = keyboard.nextInt();
               }

               System.out.println("You have a total of " + watermelons + " watermelons in your cart.");
            }
            
            // Decision statments to sort user input to nav fruit vender menu cont.
            if ( userInput == 5){
               System.out.println("*Your Cart*");
               System.out.println("Total Bananas: " + bananas);
               System.out.println("Total Oranges: " + oranges);
               System.out.println("Total Pineapples: " + pineapples);
               System.out.println("Total Watermelons: " + watermelons);
            }
            
            // Decision statments to sort user input to nav fruit vender menu cont.
            if ( userInput == 6){
               
               // User input sent to server to calculate price and total
               p.println(bananas);
               int bananaPrc = ServerScanner.nextInt();
               
               p.println(oranges);
               int orangePrc = ServerScanner.nextInt();
               
               p.println(pineapples);
               int pineapplePrc = ServerScanner.nextInt();
               
               p.println(watermelons);
               int watermelonPrc = ServerScanner.nextInt();
               
               // Total calculated for checkout
               total = bananaPrc + orangePrc + pineapplePrc + watermelonPrc;
               
               // Output displaying breakdown of pricing and total
               System.out.println("Checkout:");
               System.out.println(bananas + " Bananas: $" + bananaPrc);
               System.out.println(oranges + " Oranges: $" + orangePrc);
               System.out.println(pineapples + " Pineapples: $" + pineapplePrc);
               System.out.println(watermelons + " Watermelons: $" + watermelonPrc);
               System.out.println("---------------------");
               System.out.println("TOTAL:           $" + total);
               System.out.println();
               System.out.println("Thank you!");
            }
            // Catch invalid menu input
            if ( userInput > 6) {
               System.out.println("You have entered a number out of range, please try again");
            }
         } catch (InputMismatchException nfe) {
            System.out.println("You have entered a non numeric field value");
            keyboard.nextLine();
            
         }
               
      }
      
      // objs closed for security
      ClientSocket.close();
      ServerScanner.close();
      p.close();
      
   }

}
