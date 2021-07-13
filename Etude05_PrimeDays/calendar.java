import java.util.Scanner;
import java.lang.Math;

//Is prime if day of month is prime, month is prime, and day of year is prime...
//output: <number of day>: <number of month> <day of month>

public class calendar{
    public static void main(String[] args){
        int yearLength = 0;
        int dayOfYear = 0;
        if(args.length > 0){
            for(String monthLenghts: args){
                try {
                    yearLength += Integer.parseInt(monthLenghts);
                } catch (NumberFormatException e){
                    System.out.println(e+" in main while trying to parse integers from command line!");
                }
            }
            System.out.println("Year Length: "+yearLength);
            for(int month = 0; month < args.length; month++){
                for(int day = 0; day < Integer.parseInt(args[month]); day++){
                    if(isPrime(day+1)&&isPrime(month+1)&&isPrime(dayOfYear+1)) System.out.println((dayOfYear+1)+": "+(month+1)+ " "+(day+1));
                    dayOfYear++;
                }
            }
        }
        
    }


    public static boolean isPrime(int number){
        //for(int i = 2; i < number ** 0.5; i++
        if(number ==0) return false;
        if(number == 1) return false;
        if(number == 2 || number ==3) return true;
        int i = 2;
        while(i*i <= number){
            if((number % i) ==0) return false;
            i++;
        }
        return true;
    }
    public static boolean eratosthenes(int num){
         return false;
    }
}
