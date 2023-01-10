import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final Pattern LETTER_PATTERN = Pattern.compile("[a-zA-Z]+");
    private static final Pattern VALUE_PATTERN = Pattern.compile("^[+-]?\\d+");
    private static final Pattern EXTRA_SPACES_PATTERN = Pattern.compile("\\s+");
    private static final Pattern VARIABLE_ASSIGNMENT_PATTERN = Pattern.compile(".+=.+");
    private static final Pattern VARIABLE_NAME_PATTERN = Pattern.compile("[a-zA-Z]+");
    private static final Map<String, Integer> intVariableMap = new HashMap<>();
    private static final Map<String, BigInteger> bigIntVariableMap = new HashMap<>();

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
                try {
                    createIntVariable(input);
                } catch (NumberFormatException e) {
                    createBigIntVariable(input);
                }
            } else if (!input.isEmpty()) {
                if (inputContainsVariablePattern(input)) {
                    input = convertVariablesToValues(input);
                }

                if (input == null) {
                    System.out.println("Variable not found");
                } else {
                    String processedInput = processInput(input);
                    evaluateInput(processedInput);
                }
            }
        }

        System.out.println("Bye!");
    }

    private static boolean inputContainsBigInteger(String input) {
        String[] splitInput = input.replaceAll("\\s", "").split("");

        boolean containsBigInt = false;

        for (int i = 0; i < splitInput.length; i++) {
            if (stringNumberIsBigInt(splitInput[i])) {
                containsBigInt = true;
                break;
            }
        }

        return containsBigInt;
    }

    private static boolean stringNumberIsBigInt(String number) {
        try {
            Integer.parseInt(number);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    private static boolean inputContainsVariablePattern(String input) {
        Matcher matcher = LETTER_PATTERN.matcher(input);
        return matcher.find();
    }

    private static boolean isVariableAssignment(String input) {
        Matcher matcher = VARIABLE_ASSIGNMENT_PATTERN.matcher(input);
        return matcher.matches();
    }

    private static boolean variableNameIsValid(String variableName) {
        Matcher matcher = VARIABLE_NAME_PATTERN.matcher(variableName);
        return matcher.matches();
    }

    private static void createIntVariable(String input) {
        input = input.replaceAll("\\s", "");
        String[] splitInput = input.split("=");
        String variableName = splitInput[0];
        String variableValue = splitInput[1];

        if (!variableNameIsValid(variableName)) {
            System.out.println("Invalid identifier");
        } else if (isDigit(variableValue)) {
            updateIntVariableMap(variableName, Integer.parseInt(variableValue));
        } else if (isAlpha(variableValue)) {
            Integer value = intVariableMap.get(variableValue);
            if (value != null) {
                updateIntVariableMap(variableName, value);
            } else {
                System.out.println("Invalid assignment");
            }
        } else {
            System.out.println("Invalid assignment");
        }
    }

    private static void createBigIntVariable(String input) {
        input = input.replaceAll("\\s", "");
        String[] splitInput = input.split("=");
        String variableName = splitInput[0];
        String variableValue = splitInput[1];

        if (!variableNameIsValid(variableName)) {
            System.out.println("Invalid identifier");
        } else if (isDigit(variableValue)) {
            updateBigIntVariableMap(variableName, new BigInteger(variableValue));
        } else if (isAlpha(variableValue)) {
            BigInteger value = bigIntVariableMap.get(variableValue);
            if (value != null) {
                updateBigIntVariableMap(variableName, value);
            } else {
                System.out.println("Invalid assignment");
            }
        } else {
            System.out.println("Invalid assignment");
        }
    }

    private static boolean isDigit(String input) {
        Matcher matcher = VALUE_PATTERN.matcher(input);
        return matcher.matches();
    }

    private static boolean isAlpha(String input) {
        Matcher matcher = LETTER_PATTERN.matcher(input);
        return matcher.matches();
    }

    private static void updateIntVariableMap(String key, int value) {
        bigIntVariableMap.remove(key);
        intVariableMap.put(key, value);
    }

    private static void updateBigIntVariableMap(String key, BigInteger value) {
        intVariableMap.remove(key);
        bigIntVariableMap.put(key, value);
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

    private static String convertVariablesToValues(String input) {
        String[] splitInput = input.replaceAll("[-+*/()\\s\\d]+", " ")
                .trim()
                .split("\\s");

        for (int i = 0; i < splitInput.length; i++) {
            String character = splitInput[i].toLowerCase();

            if (intVariableMap.containsKey(character)) {
                int value = intVariableMap.get(character);
                input = input.replaceAll(character, String.valueOf(value));
            } else if (bigIntVariableMap.containsKey(character)) {
                BigInteger value = bigIntVariableMap.get(character);
                input = input.replaceAll(character, String.valueOf(value));
            } else {
                return null;
            }
        }

        return input;
    }

    private static String processInput(String input) {
        String[] splitInput = input.replaceAll("\\s", "").split("");
        StringBuilder output = new StringBuilder();
        StringBuilder plusMinusSequence = new StringBuilder();

        for (int i = 0; i < splitInput.length; i++) {
            String character = splitInput[i];

            if (character.equals("+") || character.equals("-")) {
                plusMinusSequence.append(character);

                if (i != splitInput.length - 1 && !splitInput[i + 1].equals("+") && !splitInput[i + 1].equals("-")) {
                    output.append(evaluateOperation(plusMinusSequence.toString()));
                }
            } else {
                output.append(character);
                plusMinusSequence.setLength(0);
            }
        }

        return output.toString();
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

    private static void evaluateInput(String input) {
        try {
            String postfixExpression = convertInfixExpressionToPostfix(input);
            if (inputContainsBigInteger(input)) {
                BigInteger result = computePostfixExpressionBigInt(postfixExpression);
                System.out.println(result);
            } else {
                int result = computePostfixExpressionInt(postfixExpression);
                System.out.println(result);
            }
        } catch (EmptyStackException e) {
            System.out.println("Invalid expression");
        }
    }

    private static String convertInfixExpressionToPostfix(String expression) {
        StringBuilder output = new StringBuilder();
        StringBuilder digits = new StringBuilder();
        Stack<String> stack = new Stack<>();
        String[] splitExpression = expression.replaceAll("\\s", "").split("");

        HashMap<String, Integer> operatorPrecedence = new HashMap<>();
        operatorPrecedence.put("*", 2);
        operatorPrecedence.put("/", 2);
        operatorPrecedence.put("+", 1);
        operatorPrecedence.put("-", 1);

        for (int i = 0; i < splitExpression.length; i++) {
            String op = splitExpression[i];

            if (!op.isBlank()) {
                if (isDigit(op) || (op.equals("-") && i == 0)) {
                    digits.append(op);

                    if (i == splitExpression.length - 1 || !isDigit(splitExpression[i + 1])) {
                        output.append(digits).append(" ");
                        digits.setLength(0);
                    }
                } else {
                    if (stack.isEmpty() || stack.peek().equals("(") || op.equals("(")) {
                        stack.push(op);
                    } else if (op.equals(")")) {
                        while (!stack.peek().equals("(") && !stack.isEmpty()) {
                            output.append(stack.pop()).append(" ");
                        }

                        if (!stack.isEmpty()) {
                            stack.pop();
                        }
                    } else if (operatorPrecedence.get(op) > operatorPrecedence.getOrDefault(stack.peek(), 0)) {
                        stack.push(op);
                    } else {
                        while (!stack.isEmpty() && operatorPrecedence.get(op) <= operatorPrecedence.getOrDefault(stack.peek(), 0)) {
                            output.append(stack.pop()).append(" ");
                        }

                        stack.push(op);
                    }
                }
            }
        }

        while (!stack.isEmpty()) {
            output.append(stack.pop()).append(" ");
        }

        return output.toString();
    }

    private static int computePostfixExpressionInt(String expression) {
        Stack<Integer> numbers = new Stack<>();
        String[] splitExpression = expression.split(" ");

        for (String op : splitExpression) {
            if (isDigit(op)) {
                numbers.push(Integer.parseInt(op));
            } else {
                int rhs = numbers.pop();
                int lhs = numbers.pop();
                int result = 0;

                switch(op) {
                    case "+":
                        result = lhs + rhs;
                        break;
                    case "-":
                        result = lhs - rhs;
                        break;
                    case "*":
                        result = lhs * rhs;
                        break;
                    case "/":
                        result = lhs / rhs;
                        break;
                }

                numbers.push(result);
            }
        }

        return numbers.pop();
    }

    private static BigInteger computePostfixExpressionBigInt(String expression) {
        Stack<BigInteger> numbers = new Stack<>();
        String[] splitExpression = expression.split(" ");

        for (String op : splitExpression) {
            if (isDigit(op)) {
                numbers.push(new BigInteger(op));
            } else {
                BigInteger rhs = numbers.pop();
                BigInteger lhs = numbers.pop();
                BigInteger result = BigInteger.ZERO;

                switch(op) {
                    case "+":
                        result = lhs.add(rhs);
                        break;
                    case "-":
                        result = lhs.subtract(rhs);
                        break;
                    case "*":
                        result = lhs.multiply(rhs);
                        break;
                    case "/":
                        result = lhs.divide(rhs);
                        break;
                }

                numbers.push(result);
            }
        }

        return numbers.pop();
    }
}