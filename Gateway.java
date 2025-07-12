import java.util.*;
import java.io.*;

class User implements Serializable {
    private String bankName;
    private int id;
    private String password;
    private String name;
    private int phone;
    private String email;
    private double balance;

    User(String bankName, int id, String password, String name, int phone, String email, double balance) {
        this.bankName = bankName;
        this.id = id;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.balance = balance;
    }

    public String getBankName() {
        return bankName;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public int getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public double getBalance() {
        return balance;
    }

    public void addBalance(double amount) {
        balance += amount;
    }

    public boolean deductBalance(double amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        } else {
            return false;
        }
    }
}

class Visa extends User {
    private String cardPassword;

    Visa(String bankName, int id, String password, String name, int phone, String email, double balance) {
        super(bankName, id, password, name, phone, email, balance);
        this.cardPassword = password;
    }

    @Override
    public void addBalance(double amount) {
        System.out.println("Visa payment processing...");
        super.addBalance(amount);
        System.out.println("Visa payment successful.");
    }
}

class Wallet extends User {
    private int walletId;

    Wallet(String bankName, int id, String password, String name, int phone, String email, double balance, int walletId) {
        super(bankName, id, password, name, phone, email, balance);
        this.walletId = walletId;
    }

    @Override
    public void addBalance(double amount) {
        System.out.println("Wallet payment processing...");
        super.addBalance(amount);
        System.out.println("Wallet payment successful.");
    }
}

public class Gatewaypayment {
    private static final String DATA_FILE = "user_balances.dat";

    public static void saveUsers(Map<Integer, User> users) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(users);
        } catch (IOException e) {
            System.out.println("Failed to save user balances.");
        }
    }

    public static Map<Integer, User> loadUsers() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            return (Map<Integer, User>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    public static void main(String[] args) {
        Map<Integer, User> users = loadUsers();

        if (users.isEmpty()) {
            users.put(1, new User("NBE", 1, "ahmed", "Ahmed", 1000, "ahmed@gmail.com", 500.0));
            users.put(2, new User("NBE", 2, "esam", "Esam", 1001, "esam@gmail.com", 300.0));
            users.put(3, new User("NBE", 3, "mohamed", "Mohamed", 1002, "mohamed@gmail.com", 700.0));
        }

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your user ID: ");
        int senderId = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter your password: ");
        String senderPass = sc.nextLine();

        if (!users.containsKey(senderId) || !users.get(senderId).getPassword().equals(senderPass)) {
            System.out.println("Invalid user ID or password.");
            return;
        }

        User sender = users.get(senderId);
        System.out.println("Welcome, " + sender.getName() + ". Your balance is: " + sender.getBalance());

        System.out.print("Enter receiver user ID: ");
        int receiverId = sc.nextInt();

        if (!users.containsKey(receiverId)) {
            System.out.println("Receiver not found.");
            return;
        }

        User receiver = users.get(receiverId);
        System.out.print("Enter amount to send: ");
        double amount = sc.nextDouble();

        if (sender.deductBalance(amount)) {
            receiver.addBalance(amount);
            System.out.println("Transaction successful!");
            System.out.println("Sender new balance: " + sender.getBalance());
            System.out.println("Receiver new balance: " + receiver.getBalance());
            saveUsers(users);
        } else {
            System.out.println("Insufficient balance. Transaction failed.");
        }

        sc.close();
    }
}
