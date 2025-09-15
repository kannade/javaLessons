import java.util.Scanner;

// Типы операций
enum OperationType {
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    INT_DIVIDE("//"),
    POWER("^"),
    MODULO("%");

    private final String symbol;

    OperationType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    // Поиск по символу
    public static OperationType fromSymbol(String symbol) {
        for (OperationType type : values()) {
            if (type.symbol.equals(symbol)) {
                return type;
            }
        }
        return null;
    }
}

// Абстрактный класс операции
// Класс операции должен иметь поля - операнды и метод, который возвращает результат операции.
abstract class Operation {
    protected double operand1;
    protected double operand2;

    public Operation(double operand1, double operand2) {
        this.operand1 = operand1;
        this.operand2 = operand2;
    }

    // Метод, который возвращает результат операции
    public abstract double getResult() throws ArithmeticException;
}

// Реализации операций
class Addition extends Operation {
    public Addition(double a, double b) {
        super(a, b);
    }

    @Override
    public double getResult() {
        return operand1 + operand2;
    }
}

class Subtraction extends Operation {
    public Subtraction(double a, double b) {
        super(a, b);
    }

    @Override
    public double getResult() {
        return operand1 - operand2;
    }
}

class Multiplication extends Operation {
    public Multiplication(double a, double b) {
        super(a, b);
    }

    @Override
    public double getResult() {
        return operand1 * operand2;
    }
}

class Division extends Operation {
    public Division(double a, double b) {
        super(a, b);
    }

    @Override
    public double getResult() {
        if (operand2 == 0) throw new ArithmeticException("Ошибка - деление на 0.");
        return operand1 / operand2;
    }
}

class IntDivision extends Operation {
    public IntDivision(double a, double b) {
        super(a, b);
    }

    @Override
    public double getResult() {
        if ((int) operand2 == 0) throw new ArithmeticException("Ошибка - деление на 0.");
        return (int) operand1 / (int) operand2;
    }
}

class Power extends Operation {
    public Power(double a, double b) {
        super(a, b);
    }

    @Override
    public double getResult() {
        return Math.pow(operand1, operand2);
    }
}

class Modulo extends Operation {
    public Modulo(double a, double b) {
        super(a, b);
    }

    @Override
    public double getResult() {
        if (operand2 == 0) throw new ArithmeticException("Ошибка - деление на 0.");
        return operand1 % operand2;
    }
}

// Класс калькулятора
class Calculator {

    // Метод по вычислению операции
    public double calculate(Operation operation) {
        return operation.getResult();
    }
}

// Класс для валидации выражений
class Validator {
    public static boolean isValid(String[] parts) {
        if (parts.length != 3) return false;

        try {
            Double.parseDouble(parts[0]);
            Double.parseDouble(parts[2]);
        } catch (NumberFormatException e) {
            return false;
        }

        String op = parts[1];
        return op.equals("+") || op.equals("-") || op.equals("*") ||
                op.equals("/") || op.equals("//") || op.equals("^") || op.equals("%");
    }
}

// Главный класс с консольным интерфейсом
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Calculator calculator = new Calculator();

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
            if (!Validator.isValid(parts)) {
                System.out.println("Неверное выражение. Введите еще раз.");
                continue;
            }

            // Проверка чисел, что они действительно числа
            double a = Double.parseDouble(parts[0]);
            double b = Double.parseDouble(parts[2]);
            OperationType type = OperationType.fromSymbol(parts[1]);
            if (type == null) {
                System.out.println("Неизвестная операция. Введите еще раз.");
                continue;
            }

            try {
                Operation operation;

                switch (type) { // делаем арифметическое действие в зависимости от оператора
                    case ADD -> operation = new Addition(a, b);
                    case SUBTRACT -> operation = new Subtraction(a, b);
                    case MULTIPLY -> operation = new Multiplication(a, b);
                    case DIVIDE -> operation = new Division(a, b);
                    case INT_DIVIDE -> operation = new IntDivision(a, b);
                    case POWER -> operation = new Power(a, b);
                    case MODULO -> operation = new Modulo(a, b);
                    default -> throw new IllegalStateException("Неизвестная операция: " + type);
                }

                double result = calculator.calculate(operation);
                System.out.println("Результат: " + result);

            } catch (ArithmeticException e) {
                System.out.println(e.getMessage());
            }
        }

        scanner.close();
    }
}
