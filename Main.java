import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final Pattern DIGIT_PATTERN = Pattern.compile("\\d+");
    private static final Pattern LETTER_PATTERN = Pattern.compile("[a-zA-Z]+");
    private static final Pattern VALUE_PATTERN = Pattern.compile("^[+-]?\\d+");
    private static final Pattern OPERATIONS_PATTERN = Pattern.compile("[+-]+");
    private static final Pattern EXTRA_SPACES_PATTERN = Pattern.compile("\\s+");
    private static final Pattern VARIABLE_ASSIGNMENT_PATTERN = Pattern.compile(".+=.+");
    private static final Pattern VARIABLE_NAME_PATTERN = Pattern.compile("[a-zA-Z]+");
    private static final Map<String, Integer> variableMap = new HashMap<>();

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
            } else if (isVariableAssignment(input)) {
                createVariable(input);
            } else if (!input.isEmpty()) {
                processInput(input);
            }
        }

        System.out.println("Bye!");
    }

    private static boolean isVariableAssignment(String input) {
        Matcher matcher = VARIABLE_ASSIGNMENT_PATTERN.matcher(input);
        return matcher.matches();
    }

    private static boolean variableNameIsValid(String variableName) {
        Matcher matcher = VARIABLE_NAME_PATTERN.matcher(variableName);
        return matcher.matches();
    }

    private static void createVariable(String input) {
        input = input.replaceAll("\\s", "");
        String[] splitInput = input.split("=");
        String variableName = splitInput[0];
        String variableValue = splitInput[1];

        if (!variableNameIsValid(variableName)) {
            System.out.println("Invalid identifier");
        } else if (isDigit(variableValue)) {
            updateVariableMap(variableName, Integer.parseInt(variableValue));
        } else if (isAlpha(variableValue)) {
            Integer value = variableMap.get(variableValue);
            if (value != null) {
                updateVariableMap(variableName, value);
            } else {
                System.out.println("Invalid assignment");
            }
        } else {
            System.out.println("Invalid assignment");
        }
    }

    private static boolean isDigit(String input) {
        Matcher matcher = DIGIT_PATTERN.matcher(input);
        return matcher.matches();
    }

    private static boolean isAlpha(String input) {
        Matcher matcher = LETTER_PATTERN.matcher(input);
        return matcher.matches();
    }

    private static void updateVariableMap(String key, int value) {
        variableMap.put(key, value);
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

    private static void processInput(String input) {
        String[] splitInput = input.split("\\s");
        Queue<Integer> values = new LinkedList<>();
        Queue<String> operations = new LinkedList<>();

        for (int i = 0; i < splitInput.length; i++) {
            String entry = splitInput[i];
            Matcher valueMatcher = VALUE_PATTERN.matcher(entry);
            Matcher operationsMatcher = OPERATIONS_PATTERN.matcher(entry);

            if (i % 2 == 0) {
                if (valueMatcher.matches()) {
                    values.offer(Integer.parseInt(entry));
                } else {
                    Integer value = variableMap.get(entry);
                    if (value == null) {
                        System.out.println("Unknown variable");
                        return;
                    } else {
                        values.offer(value);
                    }
                }
            } else if (i != splitInput.length - 1 && operationsMatcher.matches()) {
                if (entry.length() > 1) {
                    operations.offer(evaluateOperation(entry));
                } else {
                    operations.offer(entry);
                }
            } else {
                System.out.println("Invalid expression");
                return;
            }
        }

        evaluateExpression(values, operations);
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

    private static void evaluateExpression(Queue<Integer> numbers, Queue<String> operations) {
        if (numbers.isEmpty()) {
            return;
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

        System.out.println(result);
    }
}