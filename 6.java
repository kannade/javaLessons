import java.util.*;
import java.util.concurrent.*;

/**
 * Интерфейс наблюдателя (паттерн Observer).
 * Используется для получения уведомлений о событиях в банке.
 */
interface Observer {
    /**
     * Вызывается при возникновении события.
     *
     * @param message текст сообщения об изменении
     */
    void update(String message);
}

/**
 * Класс логгера, реализующий интерфейс {@link Observer}.
 * Выводит все уведомления банка в консоль с меткой времени.
 */
class Logger implements Observer {
    @Override
    public void update(String message) {
        System.out.printf("[%tT] %s%n", new Date(), message);
    }
}

/**
 * Перечисление возможных типов транзакций.
 */
enum TransactionType {
    DEPOSIT, WITHDRAW, TRANSFER, EXCHANGE
}

/**
 * Класс, представляющий банковскую транзакцию.
 * Может быть пополнение, снятие, перевод или обмен валюты.
 */
class Transaction {
    final TransactionType type;
    final int clientId;
    final int receiverId;
    final double amount;
    final String fromCurrency;
    final String toCurrency;

    /**
     * Создаёт новый объект транзакции.
     *
     * @param type тип транзакции
     * @param clientId ID клиента, совершающего операцию
     * @param receiverId ID получателя (для перевода)
     * @param amount сумма транзакции
     * @param fromCurrency исходная валюта
     * @param toCurrency целевая валюта
     */
    public Transaction(TransactionType type, int clientId, int receiverId,
                       double amount, String fromCurrency, String toCurrency) {
        this.type = type;
        this.clientId = clientId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
    }

    /** Создаёт транзакцию пополнения счёта. */
    public static Transaction deposit(int clientId, double amount) {
        return new Transaction(TransactionType.DEPOSIT, clientId, 0, amount, null, null);
    }

    /** Создаёт транзакцию снятия средств. */
    public static Transaction withdraw(int clientId, double amount) {
        return new Transaction(TransactionType.WITHDRAW, clientId, 0, amount, null, null);
    }

    /** Создаёт транзакцию перевода между клиентами. */
    public static Transaction transfer(int fromId, int toId, double amount) {
        return new Transaction(TransactionType.TRANSFER, fromId, toId, amount, null, null);
    }

    /** Создаёт транзакцию обмена валюты. */
    public static Transaction exchange(int clientId, double amount, String from, String to) {
        return new Transaction(TransactionType.EXCHANGE, clientId, 0, amount, from, to);
    }
}

/**
 * Класс, представляющий клиента банка.
 * Содержит баланс и валюту счёта.
 */
class Client {
    private final int clientId;
    private double balance;
    private String currency;

    /**
     * Создаёт нового клиента.
     *
     * @param id        идентификатор клиента
     * @param balance   начальный баланс
     * @param currency  валюта счёта
     */
    public Client(int id, double balance, String currency) {
        this.clientId = id;
        this.balance = balance;
        this.currency = currency;
    }

    /** Возвращает идентификатор клиента. */
    public int getClientId() {
        return clientId;
    }

    /** Возвращает текущий баланс. */
    public synchronized double getBalance() {
        return balance;
    }

    /**
     * Пополняет счёт клиента.
     *
     * @param amount сумма пополнения
     */
    public synchronized void deposit(double amount) {
        balance += amount;
    }

    /**
     * Снимает средства со счёта, если хватает баланса.
     *
     * @param amount сумма для снятия
     * @return true, если операция успешна
     */
    public synchronized boolean withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    /** Возвращает валюту счёта. */
    public synchronized String getCurrency() {
        return currency;
    }

    /** Устанавливает валюту счёта. */
    public synchronized void setCurrency(String currency) {
        this.currency = currency;
    }
}

/**
 * Класс, представляющий кассира.
 * Кассир работает в отдельном потоке и обрабатывает транзакции из очереди банка.
 */
class Cashier extends Thread {
    private final int cashierId;
    private final Bank bank;
    private volatile boolean active = true;

    /**
     * Создаёт нового кассира.
     *
     * @param id   идентификатор кассира
     * @param bank ссылка на банк
     */
    public Cashier(int id, Bank bank) {
        this.cashierId = id;
        this.bank = bank;
    }

    /**
     * Основной цикл кассира.
     * Извлекает транзакции из очереди и обрабатывает их.
     */
    @Override
    public void run() {
        while (active) {
            try {
                Transaction tx = bank.getTransactionQueue().take();
                processTransaction(tx);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                active = false;
            }
        }
    }

    /**
     * Обрабатывает конкретную транзакцию.
     *
     * @param tx объект транзакции
     */
    private void processTransaction(Transaction tx) {
        switch (tx.type) {
            case DEPOSIT -> {
                Client c = bank.getClient(tx.clientId);
                c.deposit(tx.amount);
                bank.notifyObservers(String.format(
                        "Кассир %d: пополнил счёт клиента %d на %.2f (баланс: %.2f)",
                        cashierId, c.getClientId(), tx.amount, c.getBalance()));
            }
            case WITHDRAW -> {
                Client c = bank.getClient(tx.clientId);
                if (c.withdraw(tx.amount))
                    bank.notifyObservers(String.format(
                            "Кассир %d: снял %.2f со счёта клиента %d (баланс: %.2f)",
                            cashierId, tx.amount, c.getClientId(), c.getBalance()));
                else
                    bank.notifyObservers(String.format(
                            "Кассир %d: недостаточно средств у клиента %d", cashierId, c.getClientId()));
            }
            case TRANSFER -> {
                Client from = bank.getClient(tx.clientId);
                Client to = bank.getClient(tx.receiverId);
                if (from.withdraw(tx.amount)) {
                    to.deposit(tx.amount);
                    bank.notifyObservers(String.format(
                            "Кассир %d: перевёл %.2f от клиента %d клиенту %d",
                            cashierId, tx.amount, from.getClientId(), to.getClientId()));
                } else {
                    bank.notifyObservers(String.format(
                            "Кассир %d: перевод не выполнен (недостаточно средств у клиента %d)",
                            cashierId, from.getClientId()));
                }
            }
            case EXCHANGE -> {
                Client c = bank.getClient(tx.clientId);
                if (!c.getCurrency().equals(tx.fromCurrency)) {
                    bank.notifyObservers(String.format(
                            "Кассир %d: несоответствие валюты у клиента %d", cashierId, c.getClientId()));
                    return;
                }
                double rate = bank.getExchangeRate(tx.fromCurrency + "_" + tx.toCurrency);
                if (c.withdraw(tx.amount)) {
                    double converted = tx.amount * rate;
                    c.deposit(converted);
                    c.setCurrency(tx.toCurrency);
                    bank.notifyObservers(String.format(
                            "Кассир %d: обменял %.2f %s → %.2f %s для клиента %d",
                            cashierId, tx.amount, tx.fromCurrency, converted, tx.toCurrency, c.getClientId()));
                }
            }
        }
    }

    /** Останавливает работу кассира. */
    public void shutdown() {
        active = false;
        interrupt();
    }
}

/**
 * Класс, представляющий банк.
 * Управляет клиентами, кассирами, курсами валют и транзакциями.
 */
class Bank {
    private final ConcurrentHashMap<Integer, Client> clients = new ConcurrentHashMap<>();
    private final List<Cashier> cashiers = new ArrayList<>();
    private final ConcurrentHashMap<String, Double> exchangeRates = new ConcurrentHashMap<>();
    private final BlockingQueue<Transaction> queue = new LinkedBlockingQueue<>();
    private final Random random = new Random();
    private final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
    private final List<Observer> observers = new CopyOnWriteArrayList<>();

    /**
     * Создаёт новый банк и запускает кассиров и обновление курсов валют.
     *
     * @param numCashiers количество кассиров
     * @param updateIntervalMs интервал обновления курсов валют (мс)
     */
    public Bank(int numCashiers, long updateIntervalMs) {
        for (int i = 0; i < numCashiers; i++) {
            Cashier cashier = new Cashier(i + 1, this);
            cashiers.add(cashier);
            cashier.start();
        }
        startExchangeRateUpdater(updateIntervalMs);
    }

    /** Добавляет наблюдателя (например, логгер). */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /** Уведомляет всех наблюдателей о событии. */
    public void notifyObservers(String message) {
        for (Observer o : observers) {
            o.update(message);
        }
    }

    /** Добавляет клиента в систему. */
    public void addClient(Client client) {
        clients.put(client.getClientId(), client);
    }

    /** Возвращает клиента по ID. */
    public Client getClient(int clientId) {
        return clients.get(clientId);
    }

    /** Устанавливает курс валютной пары. */
    public void setExchangeRate(String pair, double rate) {
        exchangeRates.put(pair, rate);
    }

    /** Возвращает курс валютной пары. */
    public double getExchangeRate(String pair) {
        return exchangeRates.getOrDefault(pair, 1.0);
    }

    /** Возвращает очередь транзакций. */
    public BlockingQueue<Transaction> getTransactionQueue() {
        return queue;
    }

    /** Добавляет транзакцию в очередь. */
    public void submitTransaction(Transaction tx) {
        try {
            queue.put(tx);
            notifyObservers("Транзакция поставлена в очередь: " + tx.type + " (клиент " + tx.clientId + ")");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Запускает планировщик обновления курсов валют.
     *
     * @param intervalMs интервал обновления (мс)
     */
    public void startExchangeRateUpdater(long intervalMs) {
        exchangeRates.putIfAbsent("USD_EUR", 0.92);
        exchangeRates.putIfAbsent("EUR_USD", 1.09);
        exchangeRates.putIfAbsent("USD_RUB", 95.0);
        exchangeRates.putIfAbsent("RUB_USD", 0.0105);

        executor.scheduleAtFixedRate(() -> {
            for (String pair : exchangeRates.keySet()) {
                double oldRate = exchangeRates.get(pair);
                double delta = (random.nextDouble() - 0.5) * 0.02;
                double newRate = Math.max(0.1, oldRate + oldRate * delta);
                exchangeRates.put(pair, newRate);
                notifyObservers(String.format("Курс обновлён: %s = %.4f → %.4f", pair, oldRate, newRate));
            }
        }, 0, intervalMs, TimeUnit.MILLISECONDS);
    }

    /** Останавливает все процессы банка (кассиров и обновление курсов). */
    public void stopExchangeRateUpdater() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(3, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        cashiers.forEach(Cashier::shutdown);
        for (Cashier c : cashiers) {
            try {
                c.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        notifyObservers("Банк завершил работу.");
    }
}

/**
 * Точка входа в программу.
 * Демонстрирует работу многопоточной банковской системы.
 */
public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank(2, 3000);
        bank.addObserver(new Logger());

        bank.addClient(new Client(1, 200, "USD"));
        bank.addClient(new Client(2, 100, "USD"));

        bank.submitTransaction(Transaction.deposit(1, 50));
        bank.submitTransaction(Transaction.withdraw(2, 20));
        bank.submitTransaction(Transaction.transfer(1, 2, 40));
        bank.submitTransaction(Transaction.exchange(1, 30, "USD", "EUR"));

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n=== Итоговые балансы клиентов ===");
        for (int id : List.of(1, 2)) {
            Client c = bank.getClient(id);
            System.out.printf("Клиент %d: %.2f %s%n", c.getClientId(), c.getBalance(), c.getCurrency());
        }

        bank.stopExchangeRateUpdater();
        System.out.println("Завершено");
    }
}
