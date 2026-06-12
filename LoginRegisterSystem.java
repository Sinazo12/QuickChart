/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.quickchart;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;

public class LoginRegisterSystem {
    
    // File to store user data
    private static final String USER_DATA_FILE = "users.txt";
    private static Map<String, User> users = new HashMap<>();
    private static String currentUsername = "";
    private static boolean isLoggedIn = false;
    
    // Store messages for the current session
    private static List<Message> sentMessages = new ArrayList<>();
    private static List<Message> disregardedMessages = new ArrayList<>();
    private static List<Message> storedMessages = new ArrayList<>();
    
    // Arrays for Part 3 requirements
    private static List<String> messageHashes = new ArrayList<>();
    private static List<String> messageIDs = new ArrayList<>();
    
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
        loadMessagesFromJSON();
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
                    saveAllMessagesToJSON();
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
        
        // Populate with test data (for assignment requirements)
        populateTestData();
        
        // Main application loop
        boolean quit = false;
        while (!quit) {
            System.out.println("\n===== QuickChat Menu =====");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Stored Messages Management");
            System.out.println("4. Quit");
            System.out.print("Choose an option (1-4): ");
            
            String option = scanner.nextLine().trim();
            
            switch (option) {
                case "1":
                    sendMessages(scanner);
                    break;
                case "2":
                    showRecentlySentMessages();
                    break;
                case "3":
                    storedMessagesMenu(scanner);
                    break;
                case "4":
                    System.out.println("Thank you for using QuickChat. Goodbye!");
                    quit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please enter 1, 2, 3, or 4.");
            }
        }
        
        saveUsers();
        saveAllMessagesToJSON();
        scanner.close();
    }
    
    /**
     * Populate test data as per assignment requirements
     */
    private static void populateTestData() {
        // Check if test data already exists to avoid duplicates
        if (!sentMessages.isEmpty() || !storedMessages.isEmpty()) {
            return;
        }
        
        // Test Data Message 1 - Sent
        Message msg1 = new Message(1, generateMessageID(), "+27834557896", "Did you get the cake?");
        msg1.setStatus("Sent");
        sentMessages.add(msg1);
        messageHashes.add(msg1.getMessageHash());
        messageIDs.add(msg1.getMessageID());
        
        // Test Data Message 2 - Stored
        Message msg2 = new Message(2, generateMessageID(), "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        msg2.setStatus("Stored");
        storedMessages.add(msg2);
        messageHashes.add(msg2.getMessageHash());
        messageIDs.add(msg2.getMessageID());
        
        // Test Data Message 3 - Disregard
        Message msg3 = new Message(3, generateMessageID(), "+27834484567", "Yohoooo, I am at your gate.");
        msg3.setStatus("Disregarded");
        disregardedMessages.add(msg3);
        messageHashes.add(msg3.getMessageHash());
        messageIDs.add(msg3.getMessageID());
        
        // Test Data Message 4 - Sent (Developer field means recipient)
        Message msg4 = new Message(4, generateMessageID(), "0838884567", "It is dinner time !");
        msg4.setStatus("Sent");
        sentMessages.add(msg4);
        messageHashes.add(msg4.getMessageHash());
        messageIDs.add(msg4.getMessageID());
        
        // Test Data Message 5 - Stored
        Message msg5 = new Message(5, generateMessageID(), "+27838884567", "Ok, I am leaving without you.");
        msg5.setStatus("Stored");
        storedMessages.add(msg5);
        messageHashes.add(msg5.getMessageHash());
        messageIDs.add(msg5.getMessageID());
        
        System.out.println("Test data loaded successfully!");
    }
    
    /**
     * Send messages dynamically based on user input
     */
    private static void sendMessages(Scanner scanner) {
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
                messageHashes.add(msg.getMessageHash());
                messageIDs.add(msg.getMessageID());
                System.out.println("✓ Message sent successfully!");
                System.out.println(msg.printMessages());
            } else if (action.equals("STORE")) {
                msg.setStatus("Stored");
                storedMessages.add(msg);
                messageHashes.add(msg.getMessageHash());
                messageIDs.add(msg.getMessageID());
                System.out.println("✓ Message stored successfully!");
            } else if (action.equals("DISREGARD")) {
                msg.setStatus("Disregarded");
                disregardedMessages.add(msg);
                System.out.println("✗ Message disregarded and deleted.");
            }
        }
        
        // Display total number of messages sent
        System.out.println("\n=====================================");
        System.out.println("Total number of messages sent: " + sentMessages.size());
        System.out.println("=====================================");
    }
    
    /**
     * Show recently sent messages
     */
    private static void showRecentlySentMessages() {
        if (sentMessages.isEmpty()) {
            System.out.println("No messages sent in this session.");
        } else {
            System.out.println("\n===== Recently Sent Messages =====");
            for (Message msg : sentMessages) {
                if (msg.getStatus().equals("Sent")) {
                    System.out.println(msg.printMessages());
                }
            }
        }
    }
    
    /**
     * Stored Messages Menu - Part 3 requirements
     */
    private static void storedMessagesMenu(Scanner scanner) {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== STORED MESSAGES MANAGEMENT =====");
            System.out.println("a. Display sender and recipient of all stored messages");
            System.out.println("b. Display the longest stored message");
            System.out.println("c. Search for a message ID");
            System.out.println("d. Search all messages for a particular recipient");
            System.out.println("e. Delete a message using message hash");
            System.out.println("f. Display full report of all stored messages");
            System.out.println("g. Back to Main Menu");
            System.out.print("Choose an option (a-g): ");
            
            String choice = scanner.nextLine().trim().toLowerCase();
            
            switch (choice) {
                case "a":
                    displaySenderAndRecipient();
                    break;
                case "b":
                    displayLongestStoredMessage();
                    break;
                case "c":
                    searchByMessageID(scanner);
                    break;
                case "d":
                    searchByRecipient(scanner);
                    break;
                case "e":
                    deleteByMessageHash(scanner);
                    break;
                case "f":
                    displayFullReport();
                    break;
                case "g":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option. Please choose a-g.");
            }
        }
    }
    
    /**
     * a. Display sender and recipient of all stored messages
     */
    private static void displaySenderAndRecipient() {
        System.out.println("\n===== SENDER AND RECIPIENT OF STORED MESSAGES =====");
        if (storedMessages.isEmpty()) {
            System.out.println("No stored messages found.");
            return;
        }
        
        System.out.printf("%-20s | %-20s%n", "Sender", "Recipient");
        System.out.println("----------------------------------------");
        for (Message msg : storedMessages) {
            System.out.printf("%-20s | %-20s%n", currentUsername, msg.getRecipient());
        }
    }
    
    /**
     * b. Display the longest stored message
     */
    private static void displayLongestStoredMessage() {
        System.out.println("\n===== LONGEST STORED MESSAGE =====");
        if (storedMessages.isEmpty()) {
            System.out.println("No stored messages found.");
            return;
        }
        
        Message longest = storedMessages.get(0);
        for (Message msg : storedMessages) {
            if (msg.getMessageText().length() > longest.getMessageText().length()) {
                longest = msg;
            }
        }
        
        System.out.println("Longest Message: " + longest.getMessageText());
        System.out.println("Length: " + longest.getMessageText().length() + " characters");
        System.out.println("Recipient: " + longest.getRecipient());
        System.out.println("Message Hash: " + longest.getMessageHash());
    }
    
    /**
     * c. Search for a message ID and display corresponding recipient and message
     */
    private static void searchByMessageID(Scanner scanner) {
        System.out.print("\nEnter Message ID to search: ");
        String searchID = scanner.nextLine().trim();
        
        boolean found = false;
        for (Message msg : storedMessages) {
            if (msg.getMessageID().equals(searchID)) {
                System.out.println("\n===== MESSAGE FOUND =====");
                System.out.println("Recipient: " + msg.getRecipient());
                System.out.println("Message: " + msg.getMessageText());
                System.out.println("Message Hash: " + msg.getMessageHash());
                found = true;
                break;
            }
        }
        
        if (!found) {
            for (Message msg : sentMessages) {
                if (msg.getMessageID().equals(searchID)) {
                    System.out.println("\n===== MESSAGE FOUND (in Sent Messages) =====");
                    System.out.println("Recipient: " + msg.getRecipient());
                    System.out.println("Message: " + msg.getMessageText());
                    found = true;
                    break;
                }
            }
        }
        
        if (!found) {
            System.out.println("Message ID not found: " + searchID);
        }
    }
    
    /**
     * d. Search for all messages stored for a particular recipient
     */
    private static void searchByRecipient(Scanner scanner) {
        System.out.print("\nEnter recipient phone number to search: ");
        String searchRecipient = scanner.nextLine().trim();
        
        System.out.println("\n===== MESSAGES FOR RECIPIENT: " + searchRecipient + " =====");
        boolean found = false;
        
        for (Message msg : storedMessages) {
            if (msg.getRecipient().equals(searchRecipient)) {
                System.out.println("\n[STORED] " + msg.getMessageText());
                found = true;
            }
        }
        
        for (Message msg : sentMessages) {
            if (msg.getRecipient().equals(searchRecipient)) {
                System.out.println("\n[SENT] " + msg.getMessageText());
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No messages found for recipient: " + searchRecipient);
        }
    }
    
    /**
     * e. Delete a message using the message hash
     */
    private static void deleteByMessageHash(Scanner scanner) {
        System.out.print("\nEnter Message Hash to delete: ");
        String searchHash = scanner.nextLine().trim().toUpperCase();
        
        boolean found = false;
        Iterator<Message> iterator = storedMessages.iterator();
        while (iterator.hasNext()) {
            Message msg = iterator.next();
            if (msg.getMessageHash().equals(searchHash)) {
                System.out.println("Message: \"" + msg.getMessageText() + "\" successfully deleted.");
                iterator.remove();
                messageHashes.remove(searchHash);
                found = true;
                break;
            }
        }
        
        if (!found) {
            Iterator<Message> sentIterator = sentMessages.iterator();
            while (sentIterator.hasNext()) {
                Message msg = sentIterator.next();
                if (msg.getMessageHash().equals(searchHash)) {
                    System.out.println("Message: \"" + msg.getMessageText() + "\" successfully deleted from sent messages.");
                    sentIterator.remove();
                    found = true;
                    break;
                }
            }
        }
        
        if (!found) {
            System.out.println("Message hash not found: " + searchHash);
        }
    }
    
    /**
     * f. Display a report that lists the full details of all stored messages
     */
    private static void displayFullReport() {
        System.out.println("\n===== FULL STORED MESSAGES REPORT =====");
        if (storedMessages.isEmpty() && sentMessages.isEmpty()) {
            System.out.println("No messages available.");
            return;
        }
        
        System.out.println("\n--- STORED MESSAGES ---");
        if (storedMessages.isEmpty()) {
            System.out.println("No stored messages.");
        } else {
            for (Message msg : storedMessages) {
                System.out.println("\nMessage Hash: " + msg.getMessageHash());
                System.out.println("Recipient: " + msg.getRecipient());
                System.out.println("Message: " + msg.getMessageText());
                System.out.println("Message ID: " + msg.getMessageID());
                System.out.println("Status: " + msg.getStatus());
                System.out.println("----------------------------------------");
            }
        }
        
        System.out.println("\n--- SENT MESSAGES ---");
        if (sentMessages.isEmpty()) {
            System.out.println("No sent messages.");
        } else {
            for (Message msg : sentMessages) {
                System.out.println("\nMessage Hash: " + msg.getMessageHash());
                System.out.println("Recipient: " + msg.getRecipient());
                System.out.println("Message: " + msg.getMessageText());
                System.out.println("Message ID: " + msg.getMessageID());
                System.out.println("Status: " + msg.getStatus());
                System.out.println("----------------------------------------");
            }
        }
    }
    
    /**
     * Load messages from JSON file
     */
    private static void loadMessagesFromJSON() {
        File jsonFile = new File("messages.json");
        if (!jsonFile.exists()) return;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(jsonFile))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            if (sb.length() > 0) {
                JSONArray messagesArray = new JSONArray(sb.toString());
                for (int i = 0; i < messagesArray.length(); i++) {
                    JSONObject obj = messagesArray.getJSONObject(i);
                    Message msg = new Message(
                        obj.getInt("messageNumber"),
                        obj.getString("messageID"),
                        obj.getString("recipient"),
                        obj.getString("messageText")
                    );
                    msg.setStatus(obj.getString("status"));
                    
                    if (msg.getStatus().equals("Sent")) {
                        sentMessages.add(msg);
                    } else if (msg.getStatus().equals("Stored")) {
                        storedMessages.add(msg);
                    } else if (msg.getStatus().equals("Disregarded")) {
                        disregardedMessages.add(msg);
                    }
                    
                    messageHashes.add(msg.getMessageHash());
                    messageIDs.add(msg.getMessageID());
                }
            }
        } catch (IOException e) {
            System.out.println("Warning: Could not read messages file.");
        }
    }
    
    /**
     * Save all messages to JSON file
     */
    private static void saveAllMessagesToJSON() {
        JSONArray messagesArray = new JSONArray();
        
        for (Message msg : sentMessages) {
            messagesArray.put(messageToJSON(msg));
        }
        for (Message msg : storedMessages) {
            messagesArray.put(messageToJSON(msg));
        }
        for (Message msg : disregardedMessages) {
            messagesArray.put(messageToJSON(msg));
        }
        
        try (FileWriter writer = new FileWriter("messages.json")) {
            writer.write(messagesArray.toString(4));
        } catch (IOException e) {
            System.out.println("Error saving messages: " + e.getMessage());
        }
    }
    
    private static JSONObject messageToJSON(Message msg) {
        JSONObject obj = new JSONObject();
        obj.put("messageNumber", msg.getMessageNumber());
        obj.put("messageID", msg.getMessageID());
        obj.put("messageHash", msg.getMessageHash());
        obj.put("recipient", msg.getRecipient());
        obj.put("messageText", msg.getMessageText());
        obj.put("status", msg.getStatus());
        obj.put("timestamp", new Date().toString());
        return obj;
    }
    
    /**
     * Generate a random 10-digit Message ID.
     */
    private static String generateMessageID() {
        Random rand = new Random();
        long id = 1000000000L + (long)(rand.nextDouble() * 9000000000L);
        return String.valueOf(id);
    }
    
    // ==================== USER MANAGEMENT METHODS ====================
    
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
    
    // Getters for testing
    public static List<Message> getSentMessages() { return sentMessages; }
    public static List<Message> getStoredMessages() { return storedMessages; }
    public static List<Message> getDisregardedMessages() { return disregardedMessages; }
    public static List<String> getMessageHashes() { return messageHashes; }
    public static List<String> getMessageIDs() { return messageIDs; }
    public static String getCurrentUsername() { return currentUsername; }
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
        String pattern = "^\\+27[6-8][0-9]{8}$";
        return Pattern.matches(pattern, phone);
    }
    
    /**
     * METHOD 3: createMessageHash()
     * Creates Message Hash: first two numbers of message ID + ":" + message number + ":" + first word + last word (all caps)
     */
    public String createMessageHash() {
        String firstTwo = messageID.length() >= 2 ? messageID.substring(0, 2) : messageID;
        String msgNum = String.valueOf(messageNumber);
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
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
                System.out.println("Message sent");
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
    
    // Getters and Setters
    public int getMessageNumber() { return messageNumber; }
    public String getMessageID() { return messageID; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}