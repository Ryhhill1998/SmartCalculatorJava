import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        boolean quit = false;

        while (!quit) {
            String input = getUserInput();

            if (input.equals("/exit")) {
                quit = true;
            } else if (input.equals("/help")) {
                System.out.println("The program calculates the sum of numbers");
            } else {
                try {
                    int[] numbers = parseInput(input);
                    int sum = calculateSum(numbers);
                    System.out.println(sum);
                } catch (NumberFormatException ignored) {
                }
            }
        }

        System.out.println("Bye!");
    }

    private static String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private static int[] parseInput(String input) {
        String[] splitInput = input.split(" ");
        int[] parsedInput = new int[splitInput.length];

        for (int i = 0; i < splitInput.length; i++) {
            parsedInput[i] = Integer.parseInt(splitInput[i]);
        }

        return parsedInput;
    }

    private static int calculateSum(int[] numbers) {
        int sum = 0;

        for (int num : numbers) {
            sum += num;
        }

        return sum;
    }
}
