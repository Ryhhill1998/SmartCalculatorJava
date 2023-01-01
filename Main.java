import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final Pattern NUMBER_PATTERN = Pattern.compile("^-?[0-9]+");
    private static final Pattern OPERATIONS_PATTERN = Pattern.compile("[+-]");
    private static final Pattern EXTRA_SPACES_PATTERN = Pattern.compile("\\s+");

    public static void main(String[] args) {
        boolean quit = false;

        while (!quit) {
            String input = getUserInput();

            if (input.equals("/exit")) {
                quit = true;
            } else if (input.equals("/help")) {
                System.out.println("The program calculates the sum of numbers");
            } else {
                if (inputIsValid(input)) {
                    processInput(input);
                }
            }
        }

        System.out.println("Bye!");
    }

    private static String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        return removeExtraSpaces(scanner.nextLine());
    }

    private static String removeExtraSpaces(String text) {
        Matcher matcher = EXTRA_SPACES_PATTERN.matcher(text);
        text = matcher.replaceAll(" ");
        return text;
    }

    private static boolean inputIsValid(String input) {
        Pattern pattern = Pattern.compile("[-+0-9\\s]+");
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    private static int[] parseInput(String[] splitInput) {
        int[] parsedInput = new int[splitInput.length];

        for (int i = 0; i < splitInput.length; i++) {
            parsedInput[i] = Integer.parseInt(splitInput[i]);
        }

        return parsedInput;
    }

    private static void processInput(String input) {
        String[] splitInput = input.split("\\s");
        Queue<Integer> numbers = new LinkedList<>();
        Queue<String> operations = new LinkedList<>();

        for (String entry : splitInput) {
            Matcher numberMatcher = NUMBER_PATTERN.matcher(entry);
            Matcher operationsMatcher = OPERATIONS_PATTERN.matcher(entry);

            if (numberMatcher.matches()) {
                numbers.offer(Integer.parseInt(entry));
            } else if (operationsMatcher.matches()) {
                operations.offer(entry);
            } else {
                String[] splitOperations = entry.split("");
                int minusCount = 0;
                int plusCount = 0;

                for (String op : splitOperations) {
                    if (op.equals("-")) {
                        minusCount++;
                    } else {
                        plusCount++;
                    }
                }

                String evaluatedOperation = "+";

                if (minusCount > 0) {
                    if (minusCount % 2 != 0) {
                        evaluatedOperation = "-";
                    }
                }

                operations.offer(evaluatedOperation);
            }
        }

        int lhs = 0;

        if (!numbers.isEmpty()) {
            lhs = numbers.poll();
        }

        while (!numbers.isEmpty() && !operations.isEmpty()) {
            int rhs = numbers.poll();
            String operation = operations.poll();

            if (operation.equals("+")) {
                lhs += rhs;
            } else {
                lhs -= rhs;
            }
        }

        System.out.println(lhs);
    }

    private static int calculateSum(int[] numbers) {
        int sum = 0;

        for (int num : numbers) {
            sum += num;
        }

        return sum;
    }
}
