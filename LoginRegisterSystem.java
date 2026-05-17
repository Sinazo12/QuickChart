package QuickChart;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;

public class LoginRegisterSystem {
    
    // File to store user data
    private static final String USER_DATA_FILE = "users.txt";
    private static Map<String, User> users = new HashMap<>();
    private static String currentUsername = ""; // Store currently logged-in user
    private static boolean isLoggedIn = false;
    
    // Store messages for the current session
    private static List<Message> sentMessages = new ArrayList<>();
    
    // User inner class to store credentials
    static class User {
        String password;
        String phone;
        
        User(String password, String phone) {
            this.password = password;
            this.phone = phone;
        }
    }
    
    public static void main(String[] args) {
        loadUsers();
        Scanner scanner = new Scanner(System.in);
        
        // First, login or register
        while (!isLoggedIn) {
            System.out.println("\n===== LOGIN & REGISTRATION SYSTEM =====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Select option (1-3): ");
            
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine().trim();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine().trim();
                    System.out.print("Enter cell phone number (e.g., +27712345678): ");
                    String phone = scanner.nextLine().trim();
                    
                    String registrationResult = registerUser(username, password, phone);
                    System.out.println(registrationResult);
                    break;
                case "2":
                    System.out.print("Enter username: ");
                    String loginUser = scanner.nextLine().trim();
                    System.out.print("Enter password: ");
                    String loginPass = scanner.nextLine().trim();
                    
                    boolean loginSuccess = loginUser(loginUser, loginPass);
                    String loginMessage = returnLoginStatus(loginSuccess);
                    System.out.println(loginMessage);
                    break;
                case "3":
                    System.out.println("Goodbye!");
                    saveUsers();
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
        
        // After successful login, show QuickChat welcome message
        System.out.println("\n=====================================");
        System.out.println("Welcome to QuickChat");
        System.out.println("=====================================");
        
        // Ask how many messages the user wants to send
        System.out.print("\nHow many messages do you wish to send today? ");
        int numMessages = 0;
        while (true) {
            try {
                numMessages = Integer.parseInt(scanner.nextLine().trim());
                if (numMessages > 0) {
                    break;
                } else {
                    System.out.print("Please enter a positive number: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid number: ");
            }
        }
        
        // Main application loop
        boolean quit = false;
        while (!quit) {
            System.out.println("\n===== QuickChat Menu =====");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Quit");
            System.out.print("Choose an option (1-3): ");
            
            String option = scanner.nextLine().trim();
            
            switch (option) {
                case "1":
                    sendMessages(scanner, numMessages);
                    break;
                case "2":
                    System.out.println("Coming Soon.");
                    break;
                case "3":
                    System.out.println("Thank you for using QuickChat. Goodbye!");
                    quit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please enter 1, 2, or 3.");
            }
        }
        
        saveUsers();
        scanner.close();
    }
    
    /**
     * Send messages using a for loop based on the number of messages specified.
     */
    private static void sendMessages(Scanner scanner, int numMessages) {
        sentMessages.clear(); // Clear previous messages for new session
        
        for (int messageNumber = 1; messageNumber <= numMessages; messageNumber++) {
            System.out.println("\n--- Message " + messageNumber + " of " + numMessages + " ---");
            
            // Generate Message ID (10-digit random number)
            String messageID = generateMessageID();
            
            // Get recipient cell number
            String recipient = "";
            boolean validRecipient = false;
            while (!validRecipient) {
                System.out.print("Enter recipient cell number (e.g., +27712345678): ");
                recipient = scanner.nextLine().trim();
                if (Message.checkRecipientCell(recipient)) {
                    validRecipient = true;
                } else {
                    System.out.println("Invalid recipient number. Must start with international code (+27) and be no more than 10 characters after the code.");
                }
            }
            
            // Get message content
            String messageText = "";
            boolean validMessage = false;
            while (!validMessage) {
                System.out.print("Enter your message (max 250 characters): ");
                messageText = scanner.nextLine().trim();
                if (messageText.length() <= 250) {
                    validMessage = true;
                    System.out.println("Message sent");
                } else {
                    System.out.println("Please enter a message of less than 250 characters.");
                }
            }
            
            // Create Message object
            Message msg = new Message(messageNumber, messageID, recipient, messageText);
            
            // Ask user: Send, Disregard, or Store
            String action = msg.sentMessage(scanner);
            
            if (action.equals("SEND")) {
                msg.setStatus("Sent");
                sentMessages.add(msg);
                // Display message details after sending
                msg.printMessages();
                // Store to JSON file
                storeMessageToJSON(msg);
                System.out.println("✓ Message successfully sent and stored!");
            } else if (action.equals("STORE")) {
                msg.setStatus("Stored");
                sentMessages.add(msg);
                storeMessageToJSON(msg);
                System.out.println("✓ Message successfully stored!");
            } else if (action.equals("DISREGARD")) {
                System.out.println("✗ Message disregarded and deleted.");
            }
        }
        
        // Display total number of messages sent
        int totalSent = returnTotalMessages();
        System.out.println("\n=====================================");
        System.out.println("Total number of messages sent: " + totalSent);
        System.out.println("=====================================");
    }
    
    /**
     * Generate a random 10-digit Message ID.
     */
    private static String generateMessageID() {
        Random rand = new Random();
        long id = 1000000000L + (long)(rand.nextDouble() * 9000000000L);
        return String.valueOf(id);
    }
    
    /**
     * Store a single message to a JSON file (messages.json).
     */
    private static void storeMessageToJSON(Message message) {
        JSONArray messagesArray = new JSONArray();
        File jsonFile = new File("messages.json");
        
        // Load existing messages if file exists
        if (jsonFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(jsonFile))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                if (sb.length() > 0) {
                    messagesArray = new JSONArray(sb.toString());
                }
            } catch (IOException e) {
                System.out.println("Warning: Could not read existing messages file.");
            }
        }
        
        // Create JSON object for this message
        JSONObject msgJson = new JSONObject();
        msgJson.put("messageNumber", message.getMessageNumber());
        msgJson.put("messageID", message.getMessageID());
        msgJson.put("messageHash", message.getMessageHash());
        msgJson.put("recipient", message.getRecipient());
        msgJson.put("messageText", message.getMessageText());
        msgJson.put("status", message.getStatus());
        msgJson.put("timestamp", new Date().toString());
        
        messagesArray.put(msgJson);
        
        // Write back to file
        try (FileWriter writer = new FileWriter(jsonFile)) {
            writer.write(messagesArray.toString(4)); // Pretty print with 4 spaces
        } catch (IOException e) {
            System.out.println("Error storing message to JSON: " + e.getMessage());
        }
    }
    
    /**
     * Return total number of messages sent in current session.
     */
    private static int returnTotalMessages() {
        int count = 0;
        for (Message msg : sentMessages) {
            if (msg.getStatus().equals("Sent")) {
                count++;
            }
        }
        return count;
    }
    
    // ==================== EXISTING METHODS FROM YOUR CODE ====================
    
    private static void loadUsers() {
        File file = new File(USER_DATA_FILE);
        if (!file.exists()) return;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 3) {
                    users.put(parts[0], new User(parts[1], parts[2]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }
    
    private static void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_DATA_FILE))) {
            for (Map.Entry<String, User> entry : users.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue().password + ":" + entry.getValue().phone);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }
    
    public static boolean checkUserName(String username) {
        if (username.length() > 10) return false;
        if (!username.contains("_")) return false;
        return true;
    }
    
    public static boolean checkPasswordComplexity(String password) {
        if (password.length() < 8) return false;
        if (!Pattern.compile("[A-Z]").matcher(password).find()) return false;
        if (!Pattern.compile("\\d").matcher(password).find()) return false;
        if (!Pattern.compile("[!@#$%^&*(),.?\":{}|<>]").matcher(password).find()) return false;
        return true;
    }
    
    public static boolean checkCellPhoneNumber(String phone) {
        String pattern = "^\\+27[6-8][0-9]{8}$";
        return Pattern.matches(pattern, phone);
    }
    
    public static String registerUser(String username, String password, String phone) {
        if (!checkUserName(username)) {
            return "Registration failed: Username is incorrectly formatted. Username must contain an underscore (_) and be no more than 10 characters.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Registration failed: Password does not meet the complexity requirements. Password must be at least 8 characters long, contain a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber(phone)) {
            return "Registration failed: Cell phone number is invalid. Must include international country code (+27) and be the correct length (e.g., +27712345678).";
        }
        if (users.containsKey(username)) {
            return "Registration failed: Username already exists. Please choose another username.";
        }
        users.put(username, new User(password, phone));
        saveUsers();
        return "Registration successful! User '" + username + "' has been successfully registered.";
    }
    
    public static boolean loginUser(String username, String password) {
        if (users.containsKey(username) && users.get(username).password.equals(password)) {
            currentUsername = username;
            isLoggedIn = true;
            return true;
        }
        return false;
    }
    
    public static String returnLoginStatus(boolean loginSuccess) {
        if (loginSuccess) {
            return "✅ Login Successful! Welcome back, " + currentUsername + "!";
        } else {
            return "❌ Login Failed! Invalid username or password. Please try again.";
        }
    }
}

// ==================== MESSAGE CLASS ====================

class Message {
    private int messageNumber;
    private String messageID;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String status;
    
    // Constructor
    public Message(int messageNumber, String messageID, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.messageID = messageID;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
        this.status = "Pending";
    }
    
    /**
     * METHOD 1: checkMessageID()
     * Ensures the message ID is not more than 10 characters.
     */
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }
    
    /**
     * METHOD 2: checkRecipientCell()
     * Ensures recipient cell number is no more than 10 characters long after the code and starts with +27.
     */
    public static boolean checkRecipientCell(String phone) {
        // South African format: +27 followed by 9 digits (total length 12 characters)
        String pattern = "^\\+27[6-8][0-9]{8}$";
        return Pattern.matches(pattern, phone);
    }
    
    /**
     * METHOD 3: createMessageHash()
     * Creates Message Hash: first two numbers of message ID + ":" + message number + ":" + first word + last word (all caps)
     */
    public String createMessageHash() {
        // First two numbers of message ID
        String firstTwo = messageID.length() >= 2 ? messageID.substring(0, 2) : messageID;
        
        // Message number
        String msgNum = String.valueOf(messageNumber);
        
        // First and last words of the message
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
        
        // Build hash
        String hash = firstTwo + ":" + msgNum + ":" + firstWord + lastWord;
        return hash.toUpperCase();
    }
    
    /**
     * METHOD 4: sentMessage()
     * Allows user to choose: send, disregard, or store the message.
     */
    public String sentMessage(Scanner scanner) {
        System.out.println("\nWhat would you like to do with this message?");
        System.out.println("1. Send Message");
        System.out.println("2. Disregard Message (Press 0 to delete)");
        System.out.println("3. Store Message to send later");
        System.out.print("Enter your choice (1-3): ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                return "SEND";
            case "2":
                System.out.println("Press 0 to delete the message");
                String confirm = scanner.nextLine().trim();
                if (confirm.equals("0")) {
                    return "DISREGARD";
                } else {
                    System.out.println("Message not deleted. Storing instead.");
                    return "STORE";
                }
            case "3":
                return "STORE";
            default:
                System.out.println("Invalid choice. Storing message by default.");
                return "STORE";
        }
    }
    
    /**
     * METHOD 5: printMessages()
     * Returns all message details in order: Message ID, Message Hash, Recipient, Message.
     */
    public String printMessages() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n+--------------------------------------------------+\n");
        sb.append(String.format("| %-15s | %-30s |\n", "Field", "Value"));
        sb.append("+--------------------------------------------------+\n");
        sb.append(String.format("| %-15s | %-30s |\n", "Message ID", messageID));
        sb.append(String.format("| %-15s | %-30s |\n", "Message Hash", messageHash));
        sb.append(String.format("| %-15s | %-30s |\n", "Recipient", recipient));
        sb.append(String.format("| %-15s | %-30s |\n", "Message", 
            messageText.length() > 28 ? messageText.substring(0, 25) + "..." : messageText));
        sb.append("+--------------------------------------------------+\n");
        return sb.toString();
    }
    
    /**
     * METHOD 6: returnTotalMessages() - defined in LoginRegisterSystem class
     * (Kept here for reference, but implemented in main class as returnTotalMessages())
     */
    
    // Getters and Setters
    public int getMessageNumber() { return messageNumber; }
    public String getMessageID() { return messageID; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}