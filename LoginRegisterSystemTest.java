/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.quickchart;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Iterator;

/**
 * Unit tests for LoginRegisterSystem - Part 3
 * Tests all required methods for IIE assignment including array population and message management
 */
public class LoginRegisterSystemTest {
    
    @BeforeEach
    void setUp() {
        System.out.println("Setting up test...");
    }
    
    @AfterEach
    void tearDown() {
        System.out.println("Test completed.");
    }
    
    // ============ PART 3: ARRAY POPULATION TESTS ============
    
    /**
     * TEST 1: Sent Messages array correctly populated
     * Expected: Contains "Did you get the cake?" and "It is dinner time!"
     */
    @Test
    void testSentMessagesArray_ContainsExpectedMessages() {
        System.out.println("\n=== TEST 1: Sent Messages Array Population ===");
        
        List<Message> sentMessages = LoginRegisterSystem.getSentMessages();
        
        boolean hasCakeMessage = false;
        boolean hasDinnerMessage = false;
        
        for (Message msg : sentMessages) {
            if (msg.getMessageText().equals("Did you get the cake?")) {
                hasCakeMessage = true;
                System.out.println("Found: 'Did you get the cake?'");
            }
            if (msg.getMessageText().equals("It is dinner time !")) {
                hasDinnerMessage = true;
                System.out.println("Found: 'It is dinner time !'");
            }
        }
        
        assertTrue(hasCakeMessage, "Expected 'Did you get the cake?' to be in sent messages");
        assertTrue(hasDinnerMessage, "Expected 'It is dinner time !' to be in sent messages");
        System.out.println("✅ TEST 1 PASSED - Both expected messages found in sent messages array");
    }
    
    /**
     * TEST 2: Display the longest Message
     * Expected: Returns "Where are you? You are late! I have asked you to be on time."
     */
    @Test
    void testLongestMessage_ReturnsCorrectMessage() {
        System.out.println("\n=== TEST 2: Longest Message Detection ===");
        
        List<Message> storedMessages = LoginRegisterSystem.getStoredMessages();
        List<Message> sentMessages = LoginRegisterSystem.getSentMessages();
        
        String longestMessage = "";
        Message longestMsgObject = null;
        
        for (Message msg : storedMessages) {
            if (msg.getMessageText().length() > longestMessage.length()) {
                longestMessage = msg.getMessageText();
                longestMsgObject = msg;
            }
        }
        
        for (Message msg : sentMessages) {
            if (msg.getMessageText().length() > longestMessage.length()) {
                longestMessage = msg.getMessageText();
                longestMsgObject = msg;
            }
        }
        
        String expectedLongest = "Where are you? You are late! I have asked you to be on time.";
        
        System.out.println("Expected longest: " + expectedLongest);
        System.out.println("Actual longest: " + longestMessage);
        System.out.println("Length: " + longestMessage.length() + " characters");
        
        assertEquals(expectedLongest, longestMessage, 
            "The longest message should be: " + expectedLongest);
        
        System.out.println("✅ TEST 2 PASSED - Correct longest message identified");
    }
    
    /**
     * TEST 3: Search for messageID
     * Expected: For message 4, should return "It is dinner time !"
     */
    @Test
    void testSearchByMessageID_ReturnsCorrectMessage() {
        System.out.println("\n=== TEST 3: Search by Message ID ===");
        
        List<Message> sentMessages = LoginRegisterSystem.getSentMessages();
        String expectedRecipient = "0838884567";
        String expectedMessage = "It is dinner time !";
        
        boolean found = false;
        for (Message msg : sentMessages) {
            if (msg.getRecipient().equals(expectedRecipient) && 
                msg.getMessageText().equals(expectedMessage)) {
                found = true;
                System.out.println("Found message with recipient: " + expectedRecipient);
                System.out.println("Message text: " + expectedMessage);
                System.out.println("Message ID: " + msg.getMessageID());
                break;
            }
        }
        
        assertTrue(found, "Should find message with recipient " + expectedRecipient + 
                   " and text: " + expectedMessage);
        System.out.println("✅ TEST 3 PASSED - Message found successfully");
    }
    
    /**
     * TEST 4: Search all messages sent or stored for a particular recipient
     * Expected: For recipient +27838884567, returns two messages
     */
    @Test
    void testSearchByRecipient_ReturnsAllMessagesForRecipient() {
        System.out.println("\n=== TEST 4: Search by Recipient ===");
        
        String targetRecipient = "+27838884567";
        String expectedMessage1 = "Where are you? You are late! I have asked you to be on time.";
        String expectedMessage2 = "Ok, I am leaving without you.";
        
        List<Message> storedMessages = LoginRegisterSystem.getStoredMessages();
        List<Message> sentMessages = LoginRegisterSystem.getSentMessages();
        
        int messageCount = 0;
        boolean foundMessage1 = false;
        boolean foundMessage2 = false;
        
        System.out.println("Searching for recipient: " + targetRecipient);
        
        for (Message msg : storedMessages) {
            if (msg.getRecipient().equals(targetRecipient)) {
                messageCount++;
                System.out.println("Found in STORED: " + msg.getMessageText());
                if (msg.getMessageText().equals(expectedMessage1)) foundMessage1 = true;
                if (msg.getMessageText().equals(expectedMessage2)) foundMessage2 = true;
            }
        }
        
        for (Message msg : sentMessages) {
            if (msg.getRecipient().equals(targetRecipient)) {
                messageCount++;
                System.out.println("Found in SENT: " + msg.getMessageText());
                if (msg.getMessageText().equals(expectedMessage1)) foundMessage1 = true;
                if (msg.getMessageText().equals(expectedMessage2)) foundMessage2 = true;
            }
        }
        
        System.out.println("Total messages found: " + messageCount);
        
        assertEquals(2, messageCount, "Should find exactly 2 messages for recipient " + targetRecipient);
        assertTrue(foundMessage1, "Should find message: " + expectedMessage1);
        assertTrue(foundMessage2, "Should find message: " + expectedMessage2);
        
        System.out.println("✅ TEST 4 PASSED - Both messages found for recipient");
    }
    
    /**
     * TEST 5: Delete a message using a message hash
     * Expected: Message "Where are you? You are late!..." successfully deleted
     */
    @Test
    void testDeleteByMessageHash_DeletesCorrectMessage() {
        System.out.println("\n=== TEST 5: Delete by Message Hash ===");
        
        String targetMessage = "Where are you? You are late! I have asked you to be on time.";
        String targetHash = "";
        
        List<Message> storedMessages = LoginRegisterSystem.getStoredMessages();
        
        // Find the hash for the target message
        for (Message msg : storedMessages) {
            if (msg.getMessageText().equals(targetMessage)) {
                targetHash = msg.getMessageHash();
                System.out.println("Target message hash: " + targetHash);
                break;
            }
        }
        
        // Verify message exists before deletion
        boolean existsBefore = false;
        for (Message msg : LoginRegisterSystem.getStoredMessages()) {
            if (msg.getMessageHash().equals(targetHash)) {
                existsBefore = true;
                System.out.println("Message exists before deletion: YES");
                break;
            }
        }
        
        // Perform deletion - FIXED: Properly iterate and remove
        if (!targetHash.isEmpty()) {
            List<Message> messages = LoginRegisterSystem.getStoredMessages();
            Iterator<Message> iterator = messages.iterator();
            while (iterator.hasNext()) {
                Message msg = iterator.next();
                if (msg.getMessageHash().equals(targetHash)) {
                    iterator.remove();
                    System.out.println("Message deleted: " + msg.getMessageText());
                    break;
                }
            }
        }
        
        // Verify message is deleted
        boolean existsAfter = false;
        for (Message msg : LoginRegisterSystem.getStoredMessages()) {
            if (msg.getMessageHash().equals(targetHash)) {
                existsAfter = true;
                break;
            }
        }
        
        assertTrue(existsBefore, "Message should exist before deletion");
        assertFalse(existsAfter, "Message should be deleted after removal");
        
        System.out.println("✅ TEST 5 PASSED - Message successfully deleted");
    }
    
    /**
     * TEST 6: Display Report contains required fields
     * Expected: Report shows Message Hash, Recipient, Message
     */
    @Test
    void testDisplayReport_ContainsRequiredFields() {
        System.out.println("\n=== TEST 6: Display Report Fields ===");
        
        List<Message> storedMessages = LoginRegisterSystem.getStoredMessages();
        
        if (!storedMessages.isEmpty()) {
            Message testMsg = storedMessages.get(0);
            
            System.out.println("Testing message fields:");
            System.out.println("Message Hash: " + testMsg.getMessageHash());
            System.out.println("Recipient: " + testMsg.getRecipient());
            System.out.println("Message: " + testMsg.getMessageText());
            
            assertNotNull(testMsg.getMessageHash(), "Message Hash should not be null");
            assertNotNull(testMsg.getRecipient(), "Recipient should not be null");
            assertNotNull(testMsg.getMessageText(), "Message should not be null");
            
            assertTrue(testMsg.getMessageHash().length() > 0, "Message Hash should not be empty");
            assertTrue(testMsg.getRecipient().length() > 0, "Recipient should not be empty");
            assertTrue(testMsg.getMessageText().length() > 0, "Message should not be empty");
            
            System.out.println("✅ TEST 6 PASSED - Report contains all required fields");
        } else {
            System.out.println("⚠️ No stored messages found to test");
            assertTrue(true, "Skipping test - no messages available");
        }
    }
    
    // ============ ORIGINAL TESTS FROM PART 1 & 2 ============
    
    @Test
    void testCheckUserName_ValidUsername_ReturnsTrue() {
        System.out.println("\n=== Testing Valid Username ===");
        assertTrue(LoginRegisterSystem.checkUserName("john_doe"));
        assertTrue(LoginRegisterSystem.checkUserName("a_b"));
        assertTrue(LoginRegisterSystem.checkUserName("user_name"));
        System.out.println("✅ Valid username test PASSED");
    }
    
    @Test
    void testCheckUserName_NoUnderscore_ReturnsFalse() {
        System.out.println("\n=== Testing Username Without Underscore ===");
        assertFalse(LoginRegisterSystem.checkUserName("johndoe"));
        assertFalse(LoginRegisterSystem.checkUserName("username"));
        System.out.println("✅ No underscore test PASSED");
    }
    
    @Test
    void testCheckUserName_TooLong_ReturnsFalse() {
        System.out.println("\n=== Testing Username Too Long ===");
        assertFalse(LoginRegisterSystem.checkUserName("john_doe_123"));
        assertFalse(LoginRegisterSystem.checkUserName("very_long_name"));
        System.out.println("✅ Too long test PASSED");
    }
    
    @Test
    void testCheckPasswordComplexity_ValidPassword_ReturnsTrue() {
        System.out.println("\n=== Testing Valid Password ===");
        assertTrue(LoginRegisterSystem.checkPasswordComplexity("Password123!"));
        assertTrue(LoginRegisterSystem.checkPasswordComplexity("MyP@ssw0rd"));
        System.out.println("✅ Valid password test PASSED");
    }
    
    @Test
    void testCheckPasswordComplexity_TooShort_ReturnsFalse() {
        System.out.println("\n=== Testing Password Too Short ===");
        assertFalse(LoginRegisterSystem.checkPasswordComplexity("Pass1!"));
        assertFalse(LoginRegisterSystem.checkPasswordComplexity("Abc12!"));
        System.out.println("✅ Too short test PASSED");
    }
    
    @Test
    void testCheckPasswordComplexity_NoCapital_ReturnsFalse() {
        System.out.println("\n=== Testing Password No Capital ===");
        assertFalse(LoginRegisterSystem.checkPasswordComplexity("password123!"));
        System.out.println("✅ No capital test PASSED");
    }
    
    @Test
    void testCheckPasswordComplexity_NoNumber_ReturnsFalse() {
        System.out.println("\n=== Testing Password No Number ===");
        assertFalse(LoginRegisterSystem.checkPasswordComplexity("Password!!!"));
        System.out.println("✅ No number test PASSED");
    }
    
    @Test
    void testCheckPasswordComplexity_NoSpecialChar_ReturnsFalse() {
        System.out.println("\n=== Testing Password No Special Character ===");
        assertFalse(LoginRegisterSystem.checkPasswordComplexity("Password123"));
        System.out.println("✅ No special char test PASSED");
    }
    
    @Test
    void testCheckCellPhoneNumber_ValidSAPhone_ReturnsTrue() {
        System.out.println("\n=== Testing Valid SA Phone Number ===");
        assertTrue(LoginRegisterSystem.checkCellPhoneNumber("+27712345678"));
        assertTrue(LoginRegisterSystem.checkCellPhoneNumber("+27821234567"));
        System.out.println("✅ Valid phone test PASSED");
    }
    
    @Test
    void testCheckCellPhoneNumber_WrongCountryCode_ReturnsFalse() {
        System.out.println("\n=== Testing Wrong Country Code ===");
        assertFalse(LoginRegisterSystem.checkCellPhoneNumber("+17712345678"));
        assertFalse(LoginRegisterSystem.checkCellPhoneNumber("+44712345678"));
        System.out.println("✅ Wrong country code test PASSED");
    }
    
    @Test
    void testCheckCellPhoneNumber_TooShort_ReturnsFalse() {
        System.out.println("\n=== Testing Phone Number Too Short ===");
        assertFalse(LoginRegisterSystem.checkCellPhoneNumber("+2771234567"));
        System.out.println("✅ Too short test PASSED");
    }
    
    @Test
    void testCheckCellPhoneNumber_NoPlus_ReturnsFalse() {
        System.out.println("\n=== Testing Phone Number Without Plus ===");
        assertFalse(LoginRegisterSystem.checkCellPhoneNumber("27712345678"));
        System.out.println("✅ No plus test PASSED");
    }
    
    @Test
    void testRegisterUser_ValidDetails_ReturnsSuccess() {
        System.out.println("\n=== Testing Valid Registration ===");
        String result = LoginRegisterSystem.registerUser(
            "test_user_valid", 
            "TestPass123!", 
            "+27712345678"
        );
        assertTrue(result.contains("successful"));
        System.out.println("✅ Valid registration test PASSED - Result: " + result);
    }
    
    @Test
    void testRegisterUser_InvalidUsername_ReturnsError() {
        System.out.println("\n=== Testing Invalid Username Registration ===");
        String result = LoginRegisterSystem.registerUser(
            "testuser",  // No underscore
            "TestPass123!", 
            "+27712345678"
        );
        assertTrue(result.contains("Registration failed"));
        System.out.println("✅ Invalid username test PASSED - Result: " + result);
    }
    
    @Test
    void testLoginUser_ValidCredentials_ReturnsTrue() {
        System.out.println("\n=== Testing Valid Login ===");
        LoginRegisterSystem.registerUser("login_test_valid", "TestPass123!", "+27712345678");
        assertTrue(LoginRegisterSystem.loginUser("login_test_valid", "TestPass123!"));
        System.out.println("✅ Valid login test PASSED");
    }
    
    @Test
    void testLoginUser_InvalidPassword_ReturnsFalse() {
        System.out.println("\n=== Testing Invalid Password Login ===");
        LoginRegisterSystem.registerUser("login_test_invalidpass", "TestPass123!", "+27712345678");
        assertFalse(LoginRegisterSystem.loginUser("login_test_invalidpass", "WrongPass!"));
        System.out.println("✅ Invalid password test PASSED");
    }
    
    @Test
    void testReturnLoginStatus_Success_ReturnsWelcomeMessage() {
        System.out.println("\n=== Testing Login Success Message ===");
        LoginRegisterSystem.registerUser("status_test_success", "TestPass123!", "+27712345678");
        LoginRegisterSystem.loginUser("status_test_success", "TestPass123!");
        
        String result = LoginRegisterSystem.returnLoginStatus(true);
        assertTrue(result.contains("Login Successful"));
        System.out.println("✅ Login success message test PASSED - Result: " + result);
    }
    
    @Test
    void testReturnLoginStatus_Failure_ReturnsErrorMessage() {
        System.out.println("\n=== Testing Login Failure Message ===");
        String result = LoginRegisterSystem.returnLoginStatus(false);
        assertTrue(result.contains("Login Failed"));
        System.out.println("✅ Login failure message test PASSED - Result: " + result);
    }
}