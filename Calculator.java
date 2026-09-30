import java.util.Scanner;

public class Calculator {

    private static final String RESET_COMMAND = "c";
    private static final String STOP_COMMAND = "s";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Калькулятор. Операции: + - * /");
        System.out.println("Команды: C - сбросить результат, S - выключить.");

        double result = 0;
        boolean hasResult = false;

        while (true) {
            if (!hasResult) {
                String token = readToken(scanner, "Введите число: ");
                if (token == null || isCommand(token, STOP_COMMAND)) {
                    break;
                }
                if (isCommand(token, RESET_COMMAND)) {
                    continue;
                }
                Double number = parseNumber(token);
                if (number == null) {
                    continue;
                }
                result = number;
                hasResult = true;
            }

            String operationToken = readToken(scanner, "Введите операцию: ");
            if (operationToken == null || isCommand(operationToken, STOP_COMMAND)) {
                break;
            }
            if (isCommand(operationToken, RESET_COMMAND)) {
                hasResult = false;
                System.out.println("Результат сброшен.");
                continue;
            }
            if (!isSupportedOperation(operationToken)) {
                System.out.println("Неподдерживаемая операция: " + operationToken + ". Доступны + - * /");
                continue;
            }
            char operation = operationToken.charAt(0);

            String secondToken = readToken(scanner, "Введите второе число: ");
            if (secondToken == null || isCommand(secondToken, STOP_COMMAND)) {
                break;
            }
            if (isCommand(secondToken, RESET_COMMAND)) {
                hasResult = false;
                System.out.println("Результат сброшен.");
                continue;
            }
            Double secondOperand = parseNumber(secondToken);
            if (secondOperand == null) {
                continue;
            }

            if (operation == '/' && secondOperand == 0) {
                System.out.println("Ошибка: деление на ноль. Результат не изменился: " + format(result));
                continue;
            }
            result = calculate(result, operation, secondOperand);
            System.out.println("Результат: " + format(result));
        }

        System.out.println("Калькулятор выключен.");
    }

    private static String readToken(Scanner scanner, String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNext()) {
            System.out.println();
            return null;
        }
        return scanner.next();
    }

    private static boolean isCommand(String token, String command) {
        return token.equalsIgnoreCase(command);
    }

    private static boolean isSupportedOperation(String token) {
        if (token.length() != 1) {
            return false;
        }
        switch (token.charAt(0)) {
            case '+':
            case '-':
            case '*':
            case '/':
                return true;
            default:
                return false;
        }
    }

    private static Double parseNumber(String token) {
        try {
            double value = Double.parseDouble(token.replace(',', '.'));
            if (Double.isFinite(value)) {
                return value;
            }
        } catch (NumberFormatException e) {
            System.out.println("Это не число: " + token);
            return null;
        }
        System.out.println("Число должно быть конечным: " + token);
        return null;
    }

    private static double calculate(double firstOperand, char operation, double secondOperand) {
        switch (operation) {
            case '+':
                return firstOperand + secondOperand;
            case '-':
                return firstOperand - secondOperand;
            case '*':
                return firstOperand * secondOperand;
            case '/':
                return firstOperand / secondOperand;
            default:
                throw new IllegalArgumentException("Неподдерживаемая операция: " + operation);
        }
    }

    private static String format(double value) {
        if (value == Math.rint(value) && Math.abs(value) < 1e15) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }
}
