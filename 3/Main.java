import java.util.*;

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

interface Action<T extends Vehicle> {
    String getName();

    void perform(T target);
}

interface Vehicle {
    String getName();

    void printInfo();

    List<Action<? super Vehicle>> getActions();
}

abstract class AbstractVehicle implements Vehicle {
    private final String name;
    private OilType oilType;
    private EngineType engineType;
    private Integer maxSpeed;

    protected AbstractVehicle(String name) {
        this.name = Objects.requireNonNull(name);
    }

    @Override
    public String getName() {
        return name;
    }

    public OilType getOilType() {
        return oilType;
    }

    public void setOilType(OilType oilType) {
        this.oilType = oilType;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public void setEngineType(EngineType engineType) {
        this.engineType = engineType;
    }

    public Integer getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Integer maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    protected abstract void printSpecificInfo();

    @Override
    public void printInfo() {
        try {
            System.out.println("\nТип: " + getClass().getSimpleName());
            System.out.println("Название: " + name);
            if (engineType != null) System.out.println("Двигатель: " + engineType);
            if (oilType != null) System.out.println("Тип топлива: " + oilType);
            if (maxSpeed != null) System.out.println("Макс. скорость: " + maxSpeed + " км/ч");
            printSpecificInfo();
            System.out.println("----------------------------");
        } catch (Exception e) {
            System.out.println("Ошибка при выводе информации об объекте: " + e.getMessage());
        }
    }

    @Override
    public List<Action<? super Vehicle>> getActions() {
        return Collections.emptyList();
    }
}


final class Car extends AbstractVehicle implements Drivable {
    private int doors;

    public Car(String name, int doors) {
        super(name);
        this.doors = doors;
    }

    public int getDoors() {
        return doors;
    }

    public void setDoors(int doors) {
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

    @Override
    public List<Action<? super Vehicle>> getActions() {
        List<Action<? super Vehicle>> actions = new ArrayList<>();
        actions.add(new Action<Vehicle>() {
            @Override
            public String getName() {
                return "Ехать";
            }

            @Override
            public void perform(Vehicle target) {
                try {
                    Car.this.drive();
                } catch (Exception e) {
                    System.out.println("Ошибка при выполнении действия: " + e.getMessage());
                }
            }
        });
        actions.add(CommonActions.printInfoAction(this));
        return actions;
    }
}

final class Bike extends AbstractVehicle implements Drivable {
    private final boolean hasElectricEngine;

    public Bike(String name, boolean hasElectricEngine) {
        super(name);
        this.hasElectricEngine = hasElectricEngine;
    }

    public boolean isHasElectricEngine() {
        return hasElectricEngine;
    }

    @Override
    protected void printSpecificInfo() {
        System.out.println("Электродвигатель: " + (hasElectricEngine ? "Да" : "Нет"));
    }

    @Override
    public void drive() {
        System.out.println(getName() + " едет по дороге!");
    }

    @Override
    public List<Action<? super Vehicle>> getActions() {
        List<Action<? super Vehicle>> actions = new ArrayList<>();
        actions.add(new Action<Vehicle>() {
            @Override
            public String getName() {
                return "Ехать";
            }

            @Override
            public void perform(Vehicle target) {
                try {
                    Bike.this.drive();
                } catch (Exception e) {
                    System.out.println("Ошибка при выполнении действия: " + e.getMessage());
                }
            }
        });
        actions.add(CommonActions.printInfoAction(this));
        return actions;
    }
}

final class Bicycle extends AbstractVehicle implements Drivable {
    private final boolean hasGears;

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

    @Override
    public List<Action<? super Vehicle>> getActions() {
        List<Action<? super Vehicle>> actions = new ArrayList<>();
        actions.add(new Action<Vehicle>() {
            @Override
            public String getName() {
                return "Ехать";
            }

            @Override
            public void perform(Vehicle target) {
                try {
                    Bicycle.this.drive();
                } catch (Exception e) {
                    System.out.println("Ошибка при выполнении действия: " + e.getMessage());
                }
            }
        });
        actions.add(CommonActions.printInfoAction(this));
        return actions;
    }
}

final class Plane extends AbstractVehicle implements Flyable {
    private final int passengerCapacity;

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

    @Override
    public List<Action<? super Vehicle>> getActions() {
        List<Action<? super Vehicle>> actions = new ArrayList<>();
        actions.add(new Action<Vehicle>() {
            @Override
            public String getName() {
                return "Летать";
            }

            @Override
            public void perform(Vehicle target) {
                try {
                    Plane.this.fly();
                } catch (Exception e) {
                    System.out.println("Ошибка при выполнении действия: " + e.getMessage());
                }
            }
        });
        actions.add(CommonActions.printInfoAction(this));
        return actions;
    }
}

final class Helicopter extends AbstractVehicle implements Flyable {
    private final int blades;

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

    @Override
    public List<Action<? super Vehicle>> getActions() {
        List<Action<? super Vehicle>> actions = new ArrayList<>();
        actions.add(new Action<Vehicle>() {
            @Override
            public String getName() {
                return "Летать";
            }

            @Override
            public void perform(Vehicle target) {
                try {
                    Helicopter.this.fly();
                } catch (Exception e) {
                    System.out.println("Ошибка при выполнении действия: " + e.getMessage());
                }
            }
        });
        actions.add(CommonActions.printInfoAction(this));
        return actions;
    }
}

final class Train extends AbstractVehicle implements Drivable {
    private final int wagons;

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

    @Override
    public List<Action<? super Vehicle>> getActions() {
        List<Action<? super Vehicle>> actions = new ArrayList<>();
        actions.add(new Action<Vehicle>() {
            @Override
            public String getName() {
                return "Ехать по рельсам";
            }

            @Override
            public void perform(Vehicle target) {
                try {
                    Train.this.drive();
                } catch (Exception e) {
                    System.out.println("Ошибка при выполнении действия: " + e.getMessage());
                }
            }
        });
        actions.add(CommonActions.printInfoAction(this));
        return actions;
    }
}

final class Subway extends AbstractVehicle implements Drivable {
    private final int stations;

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

    @Override
    public List<Action<? super Vehicle>> getActions() {
        List<Action<? super Vehicle>> actions = new ArrayList<>();
        actions.add(new Action<Vehicle>() {
            @Override
            public String getName() {
                return "Ехать по рельсам (метро)";
            }

            @Override
            public void perform(Vehicle target) {
                try {
                    Subway.this.drive();
                } catch (Exception e) {
                    System.out.println("Ошибка при выполнении действия: " + e.getMessage());
                }
            }
        });
        actions.add(CommonActions.printInfoAction(this));
        return actions;
    }
}

final class Boat extends AbstractVehicle implements Floatable {
    private final double displacement;

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

    @Override
    public List<Action<? super Vehicle>> getActions() {
        List<Action<? super Vehicle>> actions = new ArrayList<>();
        actions.add(new Action<Vehicle>() {
            @Override
            public String getName() {
                return "Плыть";
            }

            @Override
            public void perform(Vehicle target) {
                try {
                    Boat.this.floatOnWater();
                } catch (Exception e) {
                    System.out.println("Ошибка при выполнении действия: " + e.getMessage());
                }
            }
        });
        actions.add(CommonActions.printInfoAction(this));
        return actions;
    }
}

final class Ship extends AbstractVehicle implements Floatable {
    private final double tonnage;

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

    @Override
    public List<Action<? super Vehicle>> getActions() {
        List<Action<? super Vehicle>> actions = new ArrayList<>();
        actions.add(new Action<Vehicle>() {
            @Override
            public String getName() {
                return "Плыть";
            }

            @Override
            public void perform(Vehicle target) {
                try {
                    Ship.this.floatOnWater();
                } catch (Exception e) {
                    System.out.println("Ошибка при выполнении действия: " + e.getMessage());
                }
            }
        });
        actions.add(CommonActions.printInfoAction(this));
        return actions;
    }
}

final class CommonActions {
    private CommonActions() {
    }

    static Action<? super Vehicle> printInfoAction(Vehicle who) {
        return new Action<Vehicle>() {
            @Override
            public String getName() {
                return "Показать информацию";
            }

            @Override
            public void perform(Vehicle target) {
                try {
                    who.printInfo();
                } catch (Exception e) {
                    System.out.println("Ошибка при выводе информации: " + e.getMessage());
                }
            }
        };
    }
}

@FunctionalInterface
interface VehicleBuilder {
    Vehicle build(ConsoleUI console) throws Exception;
}

final class VehicleFactory {
    private static final Map<Integer, RegisteredType> registry = new LinkedHashMap<>();
    private static int nextId = 1;

    private VehicleFactory() {
    }

    static void register(String displayName, VehicleBuilder builder) {
        registry.put(nextId++, new RegisteredType(displayName, builder));
    }

    static Map<Integer, RegisteredType> getRegistry() {
        return Collections.unmodifiableMap(registry);
    }

    static Vehicle createById(int id, ConsoleUI console) {
        RegisteredType rt = registry.get(id);
        if (rt == null) {
            console.printLine("Неизвестный тип транспорта: " + id);
            return null;
        }
        try {
            return rt.builder.build(console);
        } catch (Exception e) {
            console.printLine("Ошибка при создании транспорта: " + e.getMessage());
            return null;
        }
    }

    record RegisteredType(String displayName, VehicleBuilder builder) {
    }
}

final class ConsoleUI {
    private final Scanner scanner;

    ConsoleUI() {
        scanner = new Scanner(System.in);
    }

    int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String line = scanner.nextLine();
                if (line == null) {
                    System.out.println("\nВвод завершён. Выход.");
                    System.exit(0);
                }
                line = line.trim();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Пожалуйста, введите целое число.");
            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("\nВвод завершён или недоступен. Выход.");
                System.exit(0);
            } catch (Exception e) {
                System.out.println("Неожиданная ошибка при вводе числа: " + e.getMessage());
            }
        }
    }

    double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String line = scanner.nextLine();
                if (line == null) {
                    System.out.println("\nВвод завершён. Выход.");
                    System.exit(0);
                }
                line = line.trim();
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Пожалуйста, введите число (например, 12.5).");
            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("\nВвод завершён или недоступен. Выход.");
                System.exit(0);
            } catch (Exception e) {
                System.out.println("Неожиданная ошибка при вводе числа: " + e.getMessage());
            }
        }
    }

    boolean readBoolean(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + " (y/n): ");
                String line = scanner.nextLine();
                if (line == null) {
                    System.out.println("\nВвод завершён. Выход.");
                    System.exit(0);
                }
                line = line.trim().toLowerCase();
                if (line.equals("y") || line.equals("yes")) return true;
                if (line.equals("n") || line.equals("no")) return false;
                System.out.println("Пожалуйста, ответьте y или n.");
            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("\nВвод завершён или недоступен. Выход.");
                System.exit(0);
            } catch (Exception e) {
                System.out.println("Неожиданная ошибка при вводе: " + e.getMessage());
            }
        }
    }

    String readString(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String line = scanner.nextLine();
                if (line == null) {
                    System.out.println("\nВвод завершён. Выход.");
                    System.exit(0);
                }
                return line.trim();
            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("\nВвод завершён или недоступен. Выход.");
                System.exit(0);
            } catch (Exception e) {
                System.out.println("Неожиданная ошибка при вводе текста: " + e.getMessage());
            }
        }
    }

    <T extends Enum<T>> T chooseEnum(String prompt, Class<T> enumClass) {
        T[] vals = enumClass.getEnumConstants();
        while (true) {
            try {
                System.out.println(prompt);
                for (int i = 0; i < vals.length; i++) {
                    System.out.printf("%d. %s%n", i + 1, vals[i]);
                }
                int choice = readInt("Введите номер: ");
                if (choice >= 1 && choice <= vals.length) return vals[choice - 1];
                System.out.println("Неверный выбор, повторите.");
            } catch (Exception e) {
                System.out.println("Ошибка при выборе: " + e.getMessage());
            }
        }
    }

    void pause() {
        System.out.println("Нажмите Enter для продолжения...");
        try {
            scanner.nextLine();
        } catch (Exception _) {

        }
    }

    void printLine(String s) {
        System.out.println(s);
    }
}


public class Main {
    private final ConsoleUI console = new ConsoleUI();
    private final List<Vehicle> vehicles = new ArrayList<>();

    public static void main(String[] args) {
        Main app = new Main();
        app.registerTypes();
        app.runMainLoop();
    }

    private void registerTypes() {
        VehicleFactory.register("Машина", (c) -> {
            String name = c.readString("Введите название машины: ");
            int doors = c.readInt("Количество дверей: ");
            if (doors < 0) throw new IllegalArgumentException("Количество дверей не может быть отрицательным.");
            Car car = new Car(name, doors);
            applyCommonParams(car, c);
            return car;
        });

        VehicleFactory.register("Самолёт", (c) -> {
            String name = c.readString("Введите название самолёта: ");
            int capacity = c.readInt("Количество посадочных мест: ");
            if (capacity < 0)
                throw new IllegalArgumentException("Количество посадочных мест не может быть отрицательным.");
            Plane plane = new Plane(name, capacity);
            applyCommonParams(plane, c);
            return plane;
        });

        VehicleFactory.register("Мотоцикл", (c) -> {
            String name = c.readString("Введите название мотоцикла: ");
            boolean electro = c.readBoolean("Есть ли электродвигатель?");
            Bike bike = new Bike(name, electro);
            applyCommonParams(bike, c);
            return bike;
        });

        VehicleFactory.register("Велосипед", (c) -> {
            String name = c.readString("Введите название велосипеда: ");
            boolean gears = c.readBoolean("Есть ли переключатель передач?");
            Bicycle bicycle = new Bicycle(name, gears);
            applyCommonParams(bicycle, c);
            return bicycle;
        });

        VehicleFactory.register("Поезд", (c) -> {
            String name = c.readString("Введите название поезда: ");
            int wagons = c.readInt("Количество вагонов: ");
            if (wagons < 0) throw new IllegalArgumentException("Количество вагонов не может быть отрицательным.");
            Train train = new Train(name, wagons);
            applyCommonParams(train, c);
            return train;
        });

        VehicleFactory.register("Метро", (c) -> {
            String name = c.readString("Введите название метро: ");
            int stations = c.readInt("Количество станций: ");
            if (stations < 0) throw new IllegalArgumentException("Количество станций не может быть отрицательным.");
            Subway subway = new Subway(name, stations);
            applyCommonParams(subway, c);
            return subway;
        });

        VehicleFactory.register("Лодка", (c) -> {
            String name = c.readString("Введите название лодки: ");
            double displacement = c.readDouble("Водоизмещение (т): ");
            if (displacement < 0) throw new IllegalArgumentException("Водоизмещение не может быть отрицательным.");
            Boat boat = new Boat(name, displacement);
            applyCommonParams(boat, c);
            return boat;
        });

        VehicleFactory.register("Корабль", (c) -> {
            String name = c.readString("Введите название корабля: ");
            double tonnage = c.readDouble("Масса (т): ");
            if (tonnage < 0) throw new IllegalArgumentException("Масса корабля не может быть отрицательной.");
            Ship ship = new Ship(name, tonnage);
            applyCommonParams(ship, c);
            return ship;
        });
    }

    private static void applyCommonParams(AbstractVehicle v, ConsoleUI c) {
        EngineType et = c.chooseEnum("Выберите тип двигателя:", EngineType.class);
        v.setEngineType(et);
        OilType ot = c.chooseEnum("Выберите тип топлива:", OilType.class);
        v.setOilType(ot);
        int speed = c.readInt("Введите макс. скорость (км/ч, 0 чтобы пропустить): ");
        if (speed > 0) v.setMaxSpeed(speed);
    }

    private void runMainLoop() {
        while (true) {
            try {
                console.printLine("\n===== Меню =====");
                console.printLine("1. Добавить транспорт");
                console.printLine("2. Показать все созданные транспорты");
                console.printLine("3. Выполнить действие с транспортом");
                console.printLine("4. Выход");
                int choice = console.readInt("Выберите пункт: ");
                switch (choice) {
                    case 1 -> addVehicle();
                    case 2 -> showVehicles();
                    case 3 -> performActionOnVehicle();
                    case 4 -> {
                        console.printLine("Выход из программы...");
                        return;
                    }
                    default -> console.printLine("Неизвестный вариант, повторите.");
                }
            } catch (Exception e) {
                System.out.println("Непредвиденная ошибка: " + e.getMessage());
            }
        }
    }

    private void addVehicle() {
        console.printLine("\nКакой транспорт добавить?");
        VehicleFactory.getRegistry().forEach((id, reg) -> {
            console.printLine(id + ". " + reg.displayName());
        });

        while (true) {
            int id = console.readInt("Введите номер (0 — отмена): ");
            if (id == 0) {
                console.printLine("Добавление отменено.");
                return;
            }
            try {
                Vehicle v = VehicleFactory.createById(id, console);
                if (v != null) {
                    vehicles.add(v);
                    console.printLine("Транспорт добавлен: " + v.getName());
                    return;
                } else {
                    boolean retry = console.readBoolean("Добавление завершилось ошибкой. Повторить выбор типа транспорта?");
                    if (!retry) {
                        console.printLine("Добавление отменено.");
                        return;
                    } else {
                        console.printLine("\nПопробуйте снова.");
                    }
                }
            } catch (Exception e) {
                console.printLine("Ошибка при создании транспорта: " + e.getMessage());
                boolean retry = console.readBoolean("Повторить ввод?");
                if (!retry) return;
            }
        }
    }

    private void showVehicles() {
        if (vehicles.isEmpty()) {
            console.printLine("\nСписок пуст.");
            return;
        }
        console.printLine("\n===== Список созданного транспорта =====");
        console.printLine("Всего: " + vehicles.size());
        for (Vehicle v : vehicles) {
            try {
                v.printInfo();
            } catch (Exception e) {
                console.printLine("Ошибка при выводе транспорта: " + e.getMessage());
            }
        }
    }

    private void performActionOnVehicle() {
        if (vehicles.isEmpty()) {
            console.printLine("\nСписок пуст.");
            return;
        }
        console.printLine("\nВыберите транспорт для действия:");
        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle v = vehicles.get(i);
            console.printLine((i + 1) + ". " + v.getName() + " (" + v.getClass().getSimpleName() + ")");
        }
        int idx = console.readInt("Введите номер (0 — отмена): ") - 1;
        if (idx == -1) {
            console.printLine("Отменено.");
            return;
        }
        if (idx < 0 || idx >= vehicles.size()) {
            console.printLine("Неверный выбор.");
            return;
        }
        Vehicle v = vehicles.get(idx);
        List<Action<? super Vehicle>> actions;
        try {
            actions = v.getActions();
        } catch (Exception e) {
            console.printLine("Ошибка при получении действий: " + e.getMessage());
            return;
        }
        if (actions.isEmpty()) {
            console.printLine("Доступных действий нет.");
            return;
        }
        console.printLine("\nДоступные действия:");
        for (int i = 0; i < actions.size(); i++) {
            console.printLine((i + 1) + ". " + actions.get(i).getName());
        }
        int actIdx = console.readInt("Выберите действие (0 — отмена): ") - 1;
        if (actIdx == -1) {
            console.printLine("Отменено.");
            return;
        }
        if (actIdx < 0 || actIdx >= actions.size()) {
            console.printLine("Неверный выбор действия.");
            return;
        }
        Action<? super Vehicle> action = actions.get(actIdx);
        try {
            action.perform(v);
        } catch (Exception e) {
            console.printLine("Ошибка при выполнении действия: " + e.getMessage());
        }
    }
}
