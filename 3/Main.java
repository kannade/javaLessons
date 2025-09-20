import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

enum OilType {
    БЕНЗИН,
    ДИЗЕЛЬ,
    ЭЛЕКТРИЧЕСКИЙ,
    ГИБРИДНЫЙ,
    КЕРОСИН,
    НЕТ
}

enum EngineType {
    ДВС,
    РЕАКТИВНЫЙ,
    ЭЛЕКТРОДВИГАТЕЛЬ,
    НЕТ
}

interface Flyable {
    void fly();
}

interface Drivable {
    void drive();
}

interface Floatable {
    void floatOnWater();
}

abstract sealed class Vehicles permits Flying, GroundBased, WaterBased {
    private String name;
    private OilType oilType;
    private EngineType engineType;
    private Integer maxSpeed;

    public Vehicles(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setOilType(OilType oilType) {
        this.oilType = oilType;
    }

    public OilType getOilType() {
        return oilType;
    }

    public void setEngineType(EngineType engineType) {
        this.engineType = engineType;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public void setMaxSpeed(Integer maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Integer getMaxSpeed() {
        return maxSpeed;
    }

    protected void printSpecificInfo() {
    }

    public void printInfo() {
        System.out.println("\nТип: " + getClass().getSimpleName());
        System.out.println("Название: " + name);
        if (engineType != null) System.out.println("Двигатель: " + engineType);
        if (oilType != null) System.out.println("Тип топлива: " + oilType);
        if (maxSpeed != null) System.out.println("Макс. скорость: " + maxSpeed + " км/ч");
        printSpecificInfo();
        System.out.println("----------------------------");
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "name='" + name + '\'' +
                (oilType != null ? ", oilType='" + oilType + '\'' : "") +
                (engineType != null ? ", engineType='" + engineType + '\'' : "") +
                (maxSpeed != null ? ", maxSpeed=" + maxSpeed : "") +
                '}';
    }
}

abstract sealed class Flying extends Vehicles implements Flyable permits Plane, Helicopter {

    public Flying(String name) {
        super(name);
    }
}

final class Plane extends Flying {
    private int passengerCapacity;

    public Plane(String name, int passengerCapacity) {
        super(name);
        this.passengerCapacity = passengerCapacity;
    }

    @Override
    protected void printSpecificInfo() {
        System.out.println("Вместимость пассажиров: " + passengerCapacity);
    }

    @Override
    public void fly() {
        System.out.println(getName() + " летит!");
    }
}

final class Helicopter extends Flying {
    private int blades;

    public Helicopter(String name, int blades) {
        super(name);
        this.blades = blades;
    }

    @Override
    protected void printSpecificInfo() {
        System.out.println("Количество лопастей: " + blades);
    }

    @Override
    public void fly() {
        System.out.println(getName() + " поднимается в воздух!");
    }
}

abstract sealed class GroundBased extends Vehicles implements Drivable permits Car, Bike, Bicycle, RailsBased {

    public GroundBased(String name) {
        super(name);
    }
}

final class Car extends GroundBased {
    private int doors;

    public Car(String name, int doors) {
        super(name);
        this.doors = doors;
    }

    @Override
    protected void printSpecificInfo() {
        System.out.println("Количество дверей: " + doors);
    }

    @Override
    public void drive() {
        System.out.println(getName() + " едет по дороге!");
    }
}

final class Bike extends GroundBased {
    private boolean hasElectricEngine;

    public Bike(String name, boolean hasElectricEngine) {
        super(name);
        this.hasElectricEngine = hasElectricEngine;
    }

    @Override
    protected void printSpecificInfo() {
        System.out.println("Электродвигатель: " + (hasElectricEngine ? "Да" : "Нет"));
    }

    @Override
    public void drive() {
        System.out.println(getName() + " едет по дороге!");
    }
}

final class Bicycle extends GroundBased {
    private boolean hasGears;

    public Bicycle(String name, boolean hasGears) {
        super(name);
        this.hasGears = hasGears;
    }

    @Override
    protected void printSpecificInfo() {
        System.out.println("Есть передачи: " + (hasGears ? "Да" : "Нет"));
    }

    @Override
    public void drive() {
        System.out.println(getName() + " едет по лесу!");
    }
}

abstract sealed class RailsBased extends GroundBased permits Train, Subway {

    public RailsBased(String name) {
        super(name);
    }
}

final class Train extends RailsBased {
    private int wagons;

    public Train(String name, int wagons) {
        super(name);
        this.wagons = wagons;
    }

    @Override
    protected void printSpecificInfo() {
        System.out.println("Количество вагонов: " + wagons);
    }

    @Override
    public void drive() {
        System.out.println(getName() + " едет по рельсам!");
    }
}

final class Subway extends RailsBased {
    private int stations;

    public Subway(String name, int stations) {
        super(name);
        this.stations = stations;
    }

    @Override
    protected void printSpecificInfo() {
        System.out.println("Станций на маршруте: " + stations);
    }

    @Override
    public void drive() {
        System.out.println(getName() + " едет по рельсам под землей!");
    }
}

abstract sealed class WaterBased extends Vehicles implements Floatable permits Boat, Ship {

    public WaterBased(String name) {
        super(name);
    }
}

final class Boat extends WaterBased {
    private double displacement;

    public Boat(String name, double displacement) {
        super(name);
        this.displacement = displacement;
    }

    @Override
    protected void printSpecificInfo() {
        System.out.println("Водоизмещение: " + displacement + " т");
    }

    @Override
    public void floatOnWater() {
        System.out.println(getName() + " рассекает волны!");
    }
}

final class Ship extends WaterBased {
    private double tonnage;

    public Ship(String name, double tonnage) {
        super(name);
        this.tonnage = tonnage;
    }

    @Override
    protected void printSpecificInfo() {
        System.out.println("Масса: " + tonnage + " т");
    }

    @Override
    public void floatOnWater() {
        System.out.println(getName() + " плывет по воде!");
    }
}

public class Main {
    private static final List<Vehicles> vehicles = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    private static boolean checkIsEmptyList() {
        if (vehicles.isEmpty()) {
            System.out.println("\nСписок пуст.");
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== Меню =====");
            System.out.println("1. Добавить транспорт");
            System.out.println("2. Показать все созданные транспорты");
            System.out.println("3. Выполнить действие с транспортом");
            System.out.println("4. Выход");
            System.out.print("Выберите пункт: ");

            int variant = scanner.nextInt();
            scanner.nextLine();

            switch (variant) {
                case 1 -> addVehicle();
                case 2 -> showVehicles();
                case 3 -> doAction();
                case 4 -> {
                    System.out.println("Выход из программы...");
                    return;
                }
                default -> System.out.println("Неизвестный вариант выбора, попробуйте еще раз.");
            }
        }
    }

    private static OilType chooseOilType() {
        System.out.println("Выберите тип топлива:");
        OilType[] values = OilType.values();
        for (int i = 0; i < values.length; i++) {
            System.out.println((i + 1) + ". " + values[i]);
        }
        int oilVariant = scanner.nextInt();
        scanner.nextLine();
        if (oilVariant >= 1 && oilVariant <= values.length) {
            return values[oilVariant - 1];
        } else {
            System.out.println("Неверный ввод, выбрано НЕТ");
            return OilType.НЕТ;
        }
    }

    private static EngineType chooseEngineType() {
        System.out.println("Выберите тип двигателя:");
        EngineType[] values = EngineType.values();
        for (int i = 0; i < values.length; i++) {
            System.out.println((i + 1) + ". " + values[i]);
        }
        int engineVariant = scanner.nextInt();
        scanner.nextLine();
        if (engineVariant >= 1 && engineVariant <= values.length) {
            return values[engineVariant - 1];
        } else {
            System.out.println("Неверный ввод, выбрано НЕТ");
            return EngineType.НЕТ;
        }
    }


    private static void addVehicle() {
        System.out.println("\nКакой транспорт добавить?");
        System.out.println("1. Машина");
        System.out.println("2. Самолет");
        System.out.println("3. Мотоцикл");
        System.out.println("4. Велосипед");
        System.out.println("5. Поезд");
        System.out.println("6. Метро");
        System.out.println("7. Лодка");
        System.out.println("8. Корабль");
        System.out.print("Введите номер: ");
        int type = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Введите название: ");
        String name = scanner.nextLine();

        Vehicles vehicle = null;

        switch (type) {
            case 1 -> {
                System.out.print("Количество дверей: ");
                int doors = scanner.nextInt();
                vehicle = new Car(name, doors);
            }
            case 2 -> {
                System.out.print("Количество посадочных мест: ");
                int capacity = scanner.nextInt();
                vehicle = new Plane(name, capacity);
            }
            case 3 -> {
                System.out.print("Есть ли электродвигатель? (true/false): ");
                boolean electroEngine = scanner.nextBoolean();
                vehicle = new Bike(name, electroEngine);
            }
            case 4 -> {
                System.out.print("Есть ли переключатель передач? (true/false): ");
                boolean shifter = scanner.nextBoolean();
                vehicle = new Bicycle(name, shifter);
            }
            case 5 -> {
                System.out.print("Количество вагонов: ");
                int wagons = scanner.nextInt();
                vehicle = new Train(name, wagons);
            }
            case 6 -> {
                System.out.print("Количество станций: ");
                int stations = scanner.nextInt();
                vehicle = new Subway(name, stations);
            }
            case 7 -> {
                System.out.print("Водоизмещение: ");
                double d = scanner.nextDouble();
                vehicle = new Boat(name, d);
            }
            case 8 -> {
                System.out.print("Масса: ");
                double mass = scanner.nextDouble();
                vehicle = new Ship(name, mass);
            }
            default -> System.out.println("Неизвестный тип транспорта.");
        }

        if (vehicle != null) {
            scanner.nextLine();
            vehicle.setEngineType(chooseEngineType());
            vehicle.setOilType(chooseOilType());

            System.out.print("Введите макс. скорость: ");
            int speed = scanner.nextInt();
            if (speed > 0) vehicle.setMaxSpeed(speed);

            vehicles.add(vehicle);
            System.out.println("Транспорт добавлен!");
        }
    }

    private static void showVehicles() {
        if (checkIsEmptyList()) return;
        System.out.println("\n===== Список созданного транспорта =====");
        System.out.println("\nВсего транспорта в списке " + vehicles.size());
        System.out.println("\n----------------------------");
        for (Vehicles v : vehicles) {
            v.printInfo();
        }
    }

    private static void doAction() {
        if (checkIsEmptyList()) return;

        System.out.println("\nВыберите транспорт для действия:");
        for (int i = 0; i < vehicles.size(); i++) {
            System.out.println((i + 1) + ". " + vehicles.get(i).getName() + " (" + vehicles.get(i).getClass().getSimpleName() + ")");
        }

        int index = scanner.nextInt() - 1;
        scanner.nextLine();
        if (index < 0 || index >= vehicles.size()) {
            System.out.println("Неверный выбор.");
            return;
        }

        Vehicles v = vehicles.get(index);

        System.out.println("\nВыберите действие:");
        if (v instanceof Flyable) System.out.println("1. Летать");
        if (v instanceof Drivable) System.out.println("2. Ехать");
        if (v instanceof Floatable) System.out.println("3. Плыть");

        int action = scanner.nextInt();
        scanner.nextLine();

        if (v instanceof Flyable f && action == 1) f.fly();
        else if (v instanceof Drivable d && action == 2) d.drive();
        else if (v instanceof Floatable fl && action == 3) fl.floatOnWater();
        else System.out.println("Неверное действие для выбранного транспорта.");
    }

}
