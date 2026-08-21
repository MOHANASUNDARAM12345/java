        import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

class Transaction {
    private LocalDateTime timestamp;
    private String type;
    private double amount;
    private String description;

    public Transaction(LocalDateTime timestamp, String type, double amount, String description) {
        this.timestamp = timestamp;
        this.type = type;
        this.amount = amount;
        this.description = description;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
}

class Account {
    private Integer id;
    private String customerName;
    private double balance;
    private NavigableMap<LocalDateTime, Transaction> transactions;

    public Account(Integer id, String customerName, double balance) {
        this.id = id;
        this.customerName = customerName;
        this.balance = balance;
        this.transactions = new TreeMap<>();
    }

    public Integer getId() { return id; }
    public String getCustomerName() { return customerName; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public NavigableMap<LocalDateTime, Transaction> getTransactions() { return transactions; }
}

class BankLedger {
    private TreeMap<Integer, Account> accounts = new TreeMap<>();

    public void addAccount(int id, String name, double initialBalance) {
        if (accounts.containsKey(id)) {
            System.out.println("Account already exists!");
            return;
        }
        Account account = new Account(id, name, initialBalance);
        accounts.put(id, account);
        System.out.printf("[SUCCESS] Account %d created for %s | Balance: ₹%,.2f%n", id, name, initialBalance);
    }

    public void addMoney(int accountId, double amount, LocalDateTime time, String description) {
        Account account = accounts.get(accountId);
        if (account == null) {
            System.out.println("Account not found!");
            return;
        }
        account.setBalance(account.getBalance() + amount);
        Transaction transaction = new Transaction(time, "CREDIT", amount, description);
        account.getTransactions().put(time, transaction);
        System.out.printf("[SUCCESS] Account %d credited with +₹%,.2f | New Balance: ₹%,.2f%n", accountId, amount, account.getBalance());
    }

    public void debitMoney(int accountId, double amount, LocalDateTime time, String description) {
        Account account = accounts.get(accountId);
        if (account == null) {
            System.out.println("Account not found!");
            return;
        }
        if (account.getBalance() < amount) {
            System.out.println("Insufficient funds!");
            return;
        }
        account.setBalance(account.getBalance() - amount);
        Transaction transaction = new Transaction(time, "DEBIT", amount, description);
        account.getTransactions().put(time, transaction);
        System.out.printf("[SUCCESS] Account %d debited with -₹%,.2f | New Balance: ₹%,.2f%n", accountId, amount, account.getBalance());
    }

    public NavigableMap<LocalDateTime, Transaction> getStatement(int accountId, LocalDateTime startDate, LocalDateTime endDate) {
        Account account = accounts.get(accountId);
        if (account == null) return new TreeMap<>();
        return account.getTransactions().subMap(startDate, true, endDate, true);
    }

    public Account getAccount(int id) { return accounts.get(id); }
}

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static BankLedger bank = new BankLedger();

    private static LocalDateTime readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return LocalDateTime.parse(scanner.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println("[ERROR] Invalid Date! Example: 2026-01-30T10:15:00 (Check leap years/format)");
            }
        }
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("============================================================");
            System.out.println("              SECUREBANK — CONSOLE MENU");
            System.out.println("============================================================");
            System.out.println("1. Add Account\n2. Add Money (Deposit)\n3. Debit Money (Withdrawal)\n4. Display User Statement\n5. Exit");
            System.out.println("============================================================");
            System.out.print("Select Option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1:
                        System.out.print("Enter Account ID: ");
                        int id = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter Customer Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter Initial Balance: ");
                        double balance = Double.parseDouble(scanner.nextLine().trim());
                        bank.addAccount(id, name, balance);
                        break;

                    case 2:
                        System.out.print("Enter Account ID: ");
                        int depId = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter Amount: ");
                        double depAmount = Double.parseDouble(scanner.nextLine().trim());
                        LocalDateTime depTime = readDate("Enter Date-Time (YYYY-MM-DDTHH:MM:SS): ");
                        System.out.print("Enter Description: ");
                        String depDesc = scanner.nextLine();
                        bank.addMoney(depId, depAmount, depTime, depDesc);
                        break;

                    case 3:
                        System.out.print("Enter Account ID: ");
                        int webId = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter Amount: ");
                        double webAmount = Double.parseDouble(scanner.nextLine().trim());
                        LocalDateTime webTime = readDate("Enter Date-Time (YYYY-MM-DDTHH:MM:SS): ");
                        System.out.print("Enter Description: ");
                        String webDesc = scanner.nextLine();
                        bank.debitMoney(webId, webAmount, webTime, webDesc);
                        break;

                    case 4:
                        System.out.print("Enter Account ID: ");
                        int stId = Integer.parseInt(scanner.nextLine().trim());
                        LocalDateTime start = readDate("Enter Start Date-Time: ");
                        LocalDateTime end = readDate("Enter End Date-Time: ");

                        Account acc = bank.getAccount(stId);
                        if (acc == null) {
                            System.out.println("Account not found!");
                            break;
                        }

                        NavigableMap<LocalDateTime, Transaction> statement = bank.getStatement(stId, start, end);

                        System.out.println("============================================================");
                        System.out.printf("         ACCOUNT STATEMENT: %d (%s)%n", acc.getId(), acc.getCustomerName());
                        System.out.printf("         Filter Period: %s to %s%n", start.toLocalDate(), end.toLocalDate());
                        System.out.println("============================================================");
                        System.out.printf("%-20s | %-7s | %-10s | %s%n", "DATE & TIME", "TYPE", "AMOUNT", "DESCRIPTION");
                        System.out.println("------------------------------------------------------------");

                        for (Transaction t : statement.values()) {
                            String sign = t.getType().equals("CREDIT") ? "+" : "-";
                            String amountStr = String.format("%s₹%,.2f", sign, t.getAmount());
                            System.out.printf("%-20s | %-7s | %-10s | %s%n",
                                    t.getTimestamp().toString().substring(0, 16),
                                    t.getType(),
                                    amountStr,
                                    t.getDescription());
                        }

                        System.out.println("------------------------------------------------------------");
                        System.out.printf("Statement complete (%d transaction(s) found in date range)%n", statement.size());
                        break;

                    case 5:
                        System.out.println("Exiting SecureBank. Goodbye!");
                        return;

                    default:
                        System.out.println("Invalid option!");
                }
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Invalid Number Input!");
            }
        }
    }
                }
