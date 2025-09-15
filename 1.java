import java.util.Scanner;

public class Main {

    // Функции для операций
    static double sum(double a, double b) {
        return a + b;
    }

    static double subtract(double a, double b) {
        return a - b;
    }

    static double multiply(double a, double b) {
        return a * b;
    }

    static double divide(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Ошибка - деление на 0.");
        }
        return a / b;
    }

    static int intDivide(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Ошибка - деление на 0.");
        }
        return a / b;
    }

    static double power(double a, double b) {
        return Math.pow(a, b);
    }

    static double mod(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Ошибка - деление на 0.");
        }
        return a % b;
    }

    // Проверка введеных чисел и знака
    static boolean isValid(String[] parts) {
        if (parts.length != 3) {
            return false;
        }

        // Проверка чисел, что они действительно числа
        try {
            Double.parseDouble(parts[0]);
            Double.parseDouble(parts[2]);
        } catch (NumberFormatException e) {
            return false;
        }

        // Проверка на поддерживаемый знак
        String op = parts[1];
        return op.equals("+") || op.equals("-") || op.equals("*") ||
                op.equals("/") || op.equals("//") || op.equals("^") || op.equals("%");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Вас приветствует простой консольный калькулятор.");
        System.out.println("Поддерживаемые операции: +, -, *, /, //, ^, %");
        System.out.println("Для выхода введите: exit");

        while (true) { //бесконечный цикл, в котором запрашиваем выражение и обрабатываем его.
            System.out.print("\nВведите выражение: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("учше")) { //сравниваем строку игнорируя регистр
                System.out.println("Выход...");
                break;
            }

            // Разбиваем строку на части. В качестве разделителя используем пробел (из примеров к заданию)
            String[] parts = input.split(" ");
            if (!isValid(parts)) {
                System.out.println("Неверное выражение. Введите еще раз.");
                continue;
            }

            // Проверка чисел, что они действительно числа
            double a = Double.parseDouble(parts[0]);
            String op = parts[1];
            double b = Double.parseDouble(parts[2]);

            try {
                double result = 0;

                switch (op) { // делаем арифметическое действие в зависимости от оператора
                    case "+":
                        result = sum(a, b);
                        break;
                    case "-":
                        result = subtract(a, b);
                        break;
                    case "*":
                        result = multiply(a, b);
                        break;
                    case "/":
                        result = divide(a, b);
                        break;
                    case "//":
                        result = intDivide((int) a, (int) b);
                        break;
                    case "^":
                        result = power(a, b);
                        break;
                    case "%":
                        result = mod(a, b);
                        break;
                    default:
                        System.out.println("Неизвестная операция.");
                        continue;
                }

                System.out.println("Результат: " + result);

            } catch (ArithmeticException e) {
                System.out.println("Произошла ошибка: " + e.getMessage());
            }
        }

        scanner.close();
    }
}