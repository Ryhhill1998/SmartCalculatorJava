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
                String[] splitInput = input.split(" ");

                if (inputIsValid(splitInput)) {
                    int[] numbers = parseInput(splitInput);
                    int sum = calculateSum(numbers);
                    System.out.println(sum);
                }
            }
        }

        System.out.println("Bye!");
    }

    private static String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private static boolean inputIsValid(String[] splitInput) {
        boolean isValid = true;

        for (int i = 0; i < splitInput.length; i++) {
            if (!splitInput[i].matches("[-0-9]+")) {
                isValid = false;
                break;
            }
        }

        return isValid;
    }

    private static int[] parseInput(String[] splitInput) {
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
