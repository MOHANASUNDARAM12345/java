import java.util.*;
import java.time.*;

class OrderItem {

    LocalDateTime timestamp;
    String type;
    double amount;
    String description;

    OrderItem(LocalDateTime t, String type,
              double amount, String description) {

        timestamp = t;
        this.type = type;
        this.amount = amount;
        this.description = description;
    }

    LocalDateTime getTimestamp() {
        return timestamp;
    }

    String getType() {
        return type;
    }

    double getAmount() {
        return amount;
    }

    String getDescription() {
        return description;
    }
}


class Customer {

    Integer id;
    String customerName;
    double walletBalance;

    NavigableMap<LocalDateTime, OrderItem> orders =
            new TreeMap<>();

    Customer(Integer id, String name, double balance) {
        this.id = id;
        customerName = name;
        walletBalance = balance;
    }

    Integer getId() {
        return id;
    }

    String getCustomerName() {
        return customerName;
    }

    double getWalletBalance() {
        return walletBalance;
    }

    NavigableMap<LocalDateTime, OrderItem> getOrders() {
        return orders;
    }
}


class StoreLedger {

    TreeMap<Integer, Customer> customers =
            new TreeMap<>();


    void addCustomer(int id, String name, double balance) {

        customers.put(
                id,
                new Customer(id, name, balance)
        );

        System.out.println("[SUCCESS] Customer registered");
    }


    void addPurchase(int id, double amount,
                     LocalDateTime time,
                     String description) {

        Customer c = customers.get(id);

        c.walletBalance -= amount;

        c.orders.put(
                time,
                new OrderItem(
                        time,
                        "PURCHASE",
                        -amount,
                        description
                )
        );

        System.out.println("[SUCCESS] Order recorded");
    }


    void processRefund(int id, double amount,
                       LocalDateTime time,
                       String description) {

        Customer c = customers.get(id);

        if (amount > 0) {

            c.walletBalance += amount;

            c.orders.put(
                    time,
                    new OrderItem(
                            time,
                            "REFUND",
                            amount,
                            description
                    )
            );

            System.out.println("[SUCCESS] Refund processed");

        } else {
            System.out.println("Invalid refund");
        }
    }


    void getOrderHistory(int id,
                         LocalDateTime start,
                         LocalDateTime end) {

        Customer c = customers.get(id);

        NavigableMap<LocalDateTime, OrderItem> result =
                c.orders.subMap(
                        start, true,
                        end, true
                );

        System.out.println("\nORDER HISTORY: "
                + id + " (" + c.customerName + ")");

        System.out.println(
                "DATE & TIME | TYPE | AMOUNT | DESCRIPTION"
        );

        for (OrderItem o : result.values()) {

            System.out.println(
                    o.timestamp + " | " +
                    o.type + " | " +
                    o.amount + " | " +
                    o.description
            );
        }

        System.out.println(
                "History complete (" +
                result.size() +
                " order(s) found in date range)"
        );
    }
}


public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        StoreLedger store = new StoreLedger();

        while (true) {

            System.out.println("\n========== SHOPMART ==========");
            System.out.println("1. Add Customer");
            System.out.println("2. Add Purchase");
            System.out.println("3. Process Refund");
            System.out.println("4. Display Order History");
            System.out.println("5. Exit");

            System.out.print("Select Option: ");

            int choice =
                    Integer.parseInt(sc.nextLine());


            switch (choice) {

                case 1:

                    System.out.print("Enter Customer ID: ");
                    int id =
                            Integer.parseInt(sc.nextLine());

                    System.out.print("Enter Customer Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Initial Wallet Balance: ");
                    double balance =
                            Double.parseDouble(sc.nextLine());

                    store.addCustomer(id, name, balance);
                    break;


                case 2:

                    System.out.print("Enter Customer ID: ");
                    id =
                            Integer.parseInt(sc.nextLine());

                    System.out.print("Enter Amount: ");
                    double amount =
                            Double.parseDouble(sc.nextLine());

                    System.out.print("Enter Date-Time: ");
                    LocalDateTime time =
                            LocalDateTime.parse(sc.nextLine());

                    System.out.print("Enter Description: ");
                    String desc = sc.nextLine();

                    store.addPurchase(
                            id, amount, time, desc
                    );
                    break;


                case 3:

                    System.out.print("Enter Customer ID: ");
                    id =
                            Integer.parseInt(sc.nextLine());

                    System.out.print("Enter Amount: ");
                    amount =
                            Double.parseDouble(sc.nextLine());

                    System.out.print("Enter Date-Time: ");
                    time =
                            LocalDateTime.parse(sc.nextLine());

                    System.out.print("Enter Description: ");
                    desc = sc.nextLine();

                    store.processRefund(
                            id, amount, time, desc
                    );
                    break;


                case 4:

                    System.out.print("Enter Customer ID: ");
                    id =
                            Integer.parseInt(sc.nextLine());

                    System.out.print("Enter Start Date-Time: ");
                    LocalDateTime start =
                            LocalDateTime.parse(sc.nextLine());

                    System.out.print("Enter End Date-Time: ");
                    LocalDateTime end =
                            LocalDateTime.parse(sc.nextLine());

                    store.getOrderHistory(
                            id, start, end
                    );
                    break;


                case 5:

                    System.out.println(
                            "Exiting SHOPMART. Goodbye!"
                    );
                    return;
            }
        }
    }
}
