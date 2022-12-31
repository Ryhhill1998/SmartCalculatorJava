import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            int number1 = scanner.nextInt();
            int number2 = scanner.nextInt();
            int sum = add(number1, number2);
            System.out.println(sum);
        } catch (InputMismatchException e) {
            System.out.println(e.getMessage());
        }
    }

    private static int add(int number1, int number2) {
        return number1 + number2;
    }
}