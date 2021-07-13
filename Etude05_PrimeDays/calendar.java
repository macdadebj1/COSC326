import java.util.Scanner;
import java.lang.Math;

public class calendar{
    public static void main(String[] args){
        if(args.length <= 0){
            System.out.println("Please provide input data!");
            
        } else{
            for(String nums : args){
                System.out.println(nums + " " + isPrime(Integer.parseInt(nums)));
            } 
        }
        
    }


    public static boolean isPrime(int number){
        //for(int i = 2; i < number ** 0.5; i++
        if(number == 2 || number ==3) return true;
        if(Math.pow(number,0.5) %2 == 0) return false;
        for(int i = 1; i < number; i++){
            if(number % i != 0){}
        }
        return false;
    }
    public static boolean eratosthenes(int num){
         
    }
}
