/*
 * Login and Registration System
 * IIE Unit Test Requirements with SPECIFIED METHOD NAMES:
 * - checkUserName() - username contains '_' and max 10 characters
 * - checkPasswordComplexity() - length, capital letter, number, special character
 * - checkCellPhoneNumber() - correct length with international country code
 * - registerUser() - returns registration messaging
 * - loginUser() - verifies login details match stored details
 * - returnLoginStatus() - returns success/failed login messaging
 */

package loginregistersystem;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class LoginRegisterSystem {
    
    // File to store user data
    private static final String USER_DATA_FILE = "users.txt";
    private static Map<String, User> users = new HashMap<>();
    private static String currentUsername = ""; // Store username during registration
    
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
        
        while (true) {
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
    }
    
    /**
     * Load existing users from file.
     */
    private static void loadUsers() {
        File file = new File(USER_DATA_FILE);
        if (!file.exists()) {
            return;
        }
        
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
    
    /**
     * Save users to file.
     */
    private static void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_DATA_FILE))) {
            for (Map.Entry<String, User> entry : users.entrySet()) {
                writer.write(entry.getKey() + ":" + 
                            entry.getValue().password + ":" + 
                            entry.getValue().phone);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }
    
    /**
     * METHOD 1: checkUserName()
     * Ensures that any username contains an underscore (_) and is no more than 10 characters.
     * @param username The username to validate
     * @return true if valid, false otherwise
     */
    public static boolean checkUserName(String username) {
        if (username.length() > 10) {
            return false;
        }
        if (!username.contains("_")) {
            return false;
        }
        return true;
    }
    
    /**
     * METHOD 2: checkPasswordComplexity()
     * Ensures that the password:
     * - Length is long (at least 8 characters)
     * - Contains a capital letter
     * - Contains a number
     * - Contains a special character
     * @param password The password to validate
     * @return true if meets all requirements, false otherwise
     */
    public static boolean checkPasswordComplexity(String password) {
        // Check length is long (at least 8 characters)
        if (password.length() < 8) {
            return false;
        }
        // Check contains a capital letter
        if (!Pattern.compile("[A-Z]").matcher(password).find()) {
            return false;
        }
        // Check contains a number
        if (!Pattern.compile("\\d").matcher(password).find()) {
            return false;
        }
        // Check contains a special character
        if (!Pattern.compile("[!@#$%^&*(),.?\":{}|<>]").matcher(password).find()) {
            return false;
        }
        return true;
    }
    
    /**
     * METHOD 3: checkCellPhoneNumber()
     * Ensures that the cell phone is the correct length and contains the international country code.
     * South African format: +27 followed by 9 digits (total length 12 characters: +27xxxxxxxxx)
     * @param phone The phone number to validate
     * @return true if valid, false otherwise
     */
    public static boolean checkCellPhoneNumber(String phone) {
        // Must start with +27 (international country code for South Africa)
        // Total length should be 12 characters (+27 + 9 digits)
        // First digit after +27 must be 6, 7, or 8 (valid SA cell prefix)
        String pattern = "^\\+27[6-8][0-9]{8}$";
        return Pattern.matches(pattern, phone);
    }
    
    /**
     * METHOD 4: registerUser()
     * Returns the necessary registration messaging indicating if:
     * - The username is incorrectly formatted
     * - The password does not meet the complexity requirements
     * - The two above conditions have been met, and the user has been successfully registered
     * @param username The username to register
     * @param password The password to register
     * @param phone The phone number to register
     * @return Registration status message
     */
    public static String registerUser(String username, String password, String phone) {
        // Check username format
        if (!checkUserName(username)) {
            return "Registration failed: Username is incorrectly formatted. Username must contain an underscore (_) and be no more than 10 characters.";
        }
        
        // Check password complexity
        if (!checkPasswordComplexity(password)) {
            return "Registration failed: Password does not meet the complexity requirements. Password must be at least 8 characters long, contain a capital letter, a number, and a special character.";
        }
        
        // Check cell phone number
        if (!checkCellPhoneNumber(phone)) {
            return "Registration failed: Cell phone number is invalid. Must include international country code (+27) and be the correct length (e.g., +27712345678).";
        }
        
        // Check if username already exists
        if (users.containsKey(username)) {
            return "Registration failed: Username already exists. Please choose another username.";
        }
        
        // All conditions met - register the user
        users.put(username, new User(password, phone));
        saveUsers();
        currentUsername = username;
        return "Registration successful! User '" + username + "' has been successfully registered.";
    }
    
    /**
     * METHOD 5: loginUser()
     * Verifies that the login details entered match the login details stored when the user registers.
     * @param username The username entered during login
     * @param password The password entered during login
     * @return true if login details match stored details, false otherwise
     */
    public static boolean loginUser(String username, String password) {
        // Check if username exists AND password matches stored password
        if (users.containsKey(username) && users.get(username).password.equals(password)) {
            currentUsername = username;
            return true;
        }
        return false;
    }
    
    /**
     * METHOD 6: returnLoginStatus()
     * Returns the necessary messaging for:
     * - A successful login
     * - A failed login
     * @param loginSuccess The boolean result from loginUser()
     * @return Appropriate login status message
     */
    public static String returnLoginStatus(boolean loginSuccess) {
        if (loginSuccess) {
            return "✅ Login Successful! Welcome back, " + currentUsername + "!";
        } else {
            return "❌ Login Failed! Invalid username or password. Please try again.";
        }
    }
}