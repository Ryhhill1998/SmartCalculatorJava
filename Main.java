import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        boolean quit = false;

        while (!quit) {
            String input = getUserInput();

            if (input.equals("/exit")) {
                quit = true;
            } else {
                try {
                    int[] numbers = parseInput(input);
                    if (numbers.length == 1) {
                        System.out.println(numbers[0]);
                    } else {
                        System.out.println(numbers[0] + numbers[1]);
                    }
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
}
