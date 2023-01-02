import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final Pattern NUMBER_PATTERN = Pattern.compile("^[+-]?[0-9]+");
    private static final Pattern OPERATIONS_PATTERN = Pattern.compile("[+-]+");
    private static final Pattern EXTRA_SPACES_PATTERN = Pattern.compile("\\s+");

    public static void main(String[] args) {
        boolean quit = false;

        while (!quit) {
            String input = getUserInput();

            if (input.startsWith("/")) {
                if (input.equals("/exit")) {
                    quit = true;
                } else if (input.equals("/help")) {
                    System.out.println("The program calculates the sum of numbers");
                } else {
                    System.out.println("Unknown command");
                }
            } else if (!input.isEmpty()) {
                if (!processInput(input)) {
                    System.out.println("Invalid expression");
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

    private static boolean processInput(String input) {
        String[] splitInput = input.split("\\s");
        Queue<Integer> numbers = new LinkedList<>();
        Queue<String> operations = new LinkedList<>();

        for (int i = 0; i < splitInput.length; i++) {
            String entry = splitInput[i];
            Matcher numberMatcher = NUMBER_PATTERN.matcher(entry);
            Matcher operationsMatcher = OPERATIONS_PATTERN.matcher(entry);

            if ((i % 2 == 0 || i == splitInput.length - 1) && !numberMatcher.matches()
                    || i % 2 != 0 && !operationsMatcher.matches()) {
                return false;
            }

            if (numberMatcher.matches()) {
                numbers.offer(Integer.parseInt(entry));
            } else if (operationsMatcher.matches()) {
                if (entry.length() > 1) {
                    operations.offer(evaluateOperation(entry));
                } else {
                    operations.offer(entry);
                }
            }
        }

        Integer result = evaluateExpression(numbers, operations);
        System.out.println(result);

        return true;
    }

    private static String evaluateOperation(String operation) {
        String[] splitOperations = operation.split("");
        int minusCount = 0;

        for (String op : splitOperations) {
            if (op.equals("-")) {
                minusCount++;
            }
        }

        String evaluatedOperation = "+";

        if (minusCount > 0 && minusCount % 2 != 0) {
            evaluatedOperation = "-";
        }

        return evaluatedOperation;
    }

    private static Integer evaluateExpression(Queue<Integer> numbers, Queue<String> operations) {
        if (numbers.isEmpty()) {
            return null;
        }

        int result = numbers.poll();

        while (!numbers.isEmpty() && !operations.isEmpty()) {
            int number = numbers.poll();
            String operation = operations.poll();

            if ("+".equals(operation)) {
                result += number;
            } else {
                result -= number;
            }
        }

        return result;
    }
}