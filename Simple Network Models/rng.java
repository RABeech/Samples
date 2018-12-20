import java.util.Random;

public class rng{

   public static void main(String[] args){
   
   Random rnd = new Random();
   
   int bananaCnt = rnd.nextInt(11) + 10;
   int orangeCnt = rnd.nextInt(11) + 10;
   int pineappleCnt = rnd.nextInt(11) + 10;
   int watermelonCnt = rnd.nextInt(11) + 10;
   
   System.out.println(bananaCnt);
   System.out.println(orangeCnt);
   System.out.println(pineappleCnt);
   System.out.println(watermelonCnt);
   
   
   }

}