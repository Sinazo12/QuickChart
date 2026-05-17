/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

package QuickChart;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LoginRegisterSystem
 * Tests all required methods for IIE assignment
 */
public class LoginRegisterSystemTest {
    
    private LoginRegisterSystem system;
    
    @BeforeEach
    void setUp() {
        system = new LoginRegisterSystem();
    }
    
    // ============ TEST 1: checkUserName() ============
    @Test
    void testCheckUserName_ValidUsername_ReturnsTrue() {
        System.out.println("Running: testCheckUserName_ValidUsername_ReturnsTrue");
        assertTrue(LoginRegisterSystem.checkUserName("john_doe"));
        assertTrue(LoginRegisterSystem.checkUserName("a_b"));
        assertTrue(LoginRegisterSystem.checkUserName("user_name"));
        assertTrue(LoginRegisterSystem.checkUserName("abc_def"));
        System.out.println("  ✅ PASSED");
    }
    
    @Test
    void testCheckUserName_NoUnderscore_ReturnsFalse() {
        System.out.println("Running: testCheckUserName_NoUnderscore_ReturnsFalse");
        assertFalse(LoginRegisterSystem.checkUserName("johndoe"));
        assertFalse(LoginRegisterSystem.checkUserName("username"));
        assertFalse(LoginRegisterSystem.checkUserName("abcdefghij"));
        System.out.println("  ✅ PASSED");
    }
    
    @Test
    void testCheckUserName_TooLong_ReturnsFalse() {
        System.out.println("Running: testCheckUserName_TooLong_ReturnsFalse");
        assertFalse(LoginRegisterSystem.checkUserName("john_doe_123"));
        assertFalse(LoginRegisterSystem.checkUserName("very_long_name"));
        assertFalse(LoginRegisterSystem.checkUserName("this_is_way_too_long"));
        System.out.println("  ✅ PASSED");
    }
    
    @Test
    void testCheckUserName_TooLongAndNoUnderscore_ReturnsFalse() {
        System.out.println("Running: testCheckUserName_TooLongAndNoUnderscore_ReturnsFalse");
        assertFalse(LoginRegisterSystem.checkUserName("thisiswaytoolong"));
        assertFalse(LoginRegisterSystem.checkUserName("abcdefghijklmnop"));
        System.out.println("  ✅ PASSED");
    }
    
    // ============ TEST 2: checkPasswordComplexity() ============
    @Test
    void testCheckPasswordComplexity_ValidPassword_ReturnsTrue() {
        System.out.println("Running: testCheckPasswordComplexity_ValidPassword_ReturnsTrue");
        assertTrue(LoginRegisterSystem.checkPasswordComplexity("Password123!"));
        assertTrue(LoginRegisterSystem.checkPasswordComplexity("MyP@ssw0rd"));
        assertTrue(LoginRegisterSystem.checkPasswordComplexity("Secure#9pass"));
        assertTrue(LoginRegisterSystem.checkPasswordComplexity("Test123$%^"));
        System.out.println("  ✅ PASSED");
    }
    
    @Test
    void testCheckPasswordComplexity_TooShort_ReturnsFalse() {
        System.out.println("Running: testCheckPasswordComplexity_TooShort_ReturnsFalse");
        assertFalse(LoginRegisterSystem.checkPasswordComplexity("Pass1!"));
        assertFalse(LoginRegisterSystem.checkPasswordComplexity("Abc12!"));
        assertFalse(LoginRegisterSystem.checkPasswordComplexity("Short1!"));
        System.out.println("  ✅ PASSED");
    }
    
    @Test
    void testCheckPasswordComplexity_NoCapital_ReturnsFalse() {
        System.out.println("Running: testCheckPasswordComplexity_NoCapital_ReturnsFalse");
        assertFalse(LoginRegisterSystem.checkPasswordComplexity("password123!"));
        assertFalse(LoginRegisterSystem.checkPasswordComplexity("alllowercase1!"));
        assertFalse(LoginRegisterSystem.checkPasswordComplexity("nocapital123$"));
        System.out.println("  ✅ PASSED");
    }
    
    @Test
    void testCheckPasswordComplexity_NoNumber_ReturnsFalse() {
        System.out.println("Running: testCheckPasswordComplexity_NoNumber_ReturnsFalse");
        assertFalse(LoginRegisterSystem.checkPasswordComplexity("Password!!!"));
        assertFalse(LoginRegisterSystem.checkPasswordComplexity("NoNumbersHere!"));
        assertFalse(LoginRegisterSystem.checkPasswordComplexity("TestOnly!!"));
        System.out.println("  ✅ PASSED");
    }
    
    @Test
    void testCheckPasswordComplexity_NoSpecialChar_ReturnsFalse() {
        System.out.println("Running: testCheckPasswordComplexity_NoSpecialChar_ReturnsFalse");
        assertFalse(LoginRegisterSystem.checkPasswordComplexity("Password123"));
        assertFalse(LoginRegisterSystem.checkPasswordComplexity("NoSpecial123"));
        assertFalse(LoginRegisterSystem.checkPasswordComplexity("TestPassword456"));
        System.out.println("  ✅ PASSED");
    }
    
    // ============ TEST 3: checkCellPhoneNumber() ============
    @Test
    void testCheckCellPhoneNumber_ValidSAPhone_ReturnsTrue() {
        System.out.println("Running: testCheckCellPhoneNumber_ValidSAPhone_ReturnsTrue");
        assertTrue(LoginRegisterSystem.checkCellPhoneNumber("+27712345678"));
        assertTrue(LoginRegisterSystem.checkCellPhoneNumber("+27821234567"));
        assertTrue(LoginRegisterSystem.checkCellPhoneNumber("+27639876543"));
        System.out.println("  ✅ PASSED");
    }
    
    @Test
    void testCheckCellPhoneNumber_WrongCountryCode_ReturnsFalse() {
        System.out.println("Running: testCheckCellPhoneNumber_WrongCountryCode_ReturnsFalse");
        assertFalse(LoginRegisterSystem.checkCellPhoneNumber("+17712345678"));
        assertFalse(LoginRegisterSystem.checkCellPhoneNumber("+44712345678"));
        assertFalse(LoginRegisterSystem.checkCellPhoneNumber("+17712345678"));
        System.out.println("  ✅ PASSED");
    }
    
    @Test
    void testCheckCellPhoneNumber_TooShort_ReturnsFalse() {
        System.out.println("Running: testCheckCellPhoneNumber_TooShort_ReturnsFalse");
        assertFalse(LoginRegisterSystem.checkCellPhoneNumber("+2771234567"));
        assertFalse(LoginRegisterSystem.checkCellPhoneNumber("+277123456"));
        assertFalse(LoginRegisterSystem.checkCellPhoneNumber("+27712345"));
        System.out.println("  ✅ PASSED");
    }
    
    @Test
    void testCheckCellPhoneNumber_TooLong_ReturnsFalse() {
        System.out.println("Running: testCheckCellPhoneNumber_TooLong_ReturnsFalse");
        assertFalse(LoginRegisterSystem.checkCellPhoneNumber("+277123456789"));
        assertFalse(LoginRegisterSystem.checkCellPhoneNumber("+2771234567890"));
        System.out.println("  ✅ PASSED");
    }
    
    @Test
    void testCheckCellPhoneNumber_NoPlus_ReturnsFalse() {
        System.out.println("Running: testCheckCellPhoneNumber_NoPlus_ReturnsFalse");
        assertFalse(LoginRegisterSystem.checkCellPhoneNumber("27712345678"));
        assertFalse(LoginRegisterSystem.checkCellPhoneNumber("2771234567"));
        System.out.println("  ✅ PASSED");
    }
    
    @Test
    void testCheckCellPhoneNumber_InvalidPrefix_ReturnsFalse() {
        System.out.println("Running: testCheckCellPhoneNumber_InvalidPrefix_ReturnsFalse");
        assertFalse(LoginRegisterSystem.checkCellPhoneNumber("+27512345678"));
        assertFalse(LoginRegisterSystem.checkCellPhoneNumber("+27912345678"));
        assertFalse(LoginRegisterSystem.checkCellPhoneNumber("+27412345678"));
        System.out.println("  ✅ PASSED");
    }
    
    // ============ TEST 4: registerUser() ============
    @Test
    void testRegisterUser_ValidDetails_ReturnsSuccess() {
        System.out.println("Running: testRegisterUser_ValidDetails_ReturnsSuccess");
        String result = LoginRegisterSystem.registerUser(
            "test_user_valid", 
            "TestPass123!", 
            "+27712345678"
        );
        assertTrue(result.contains("successful"));
        assertTrue(result.contains("test_user_valid"));
        System.out.println("  ✅ PASSED - Result: " + result);
    }
    
    @Test
    void testRegisterUser_InvalidUsername_ReturnsError() {
        System.out.println("Running: testRegisterUser_InvalidUsername_ReturnsError");
        String result = LoginRegisterSystem.registerUser(
            "testuser",  // No underscore
            "TestPass123!", 
            "+27712345678"
        );
        assertTrue(result.contains("Registration failed"));
        assertTrue(result.contains("Username"));
        System.out.println("  ✅ PASSED - Result: " + result);
    }
    
    @Test
    void testRegisterUser_InvalidPassword_ReturnsError() {
        System.out.println("Running: testRegisterUser_InvalidPassword_ReturnsError");
        String result = LoginRegisterSystem.registerUser(
            "test_user_invalidpass",
            "weak",  // Too weak
            "+27712345678"
        );
        assertTrue(result.contains("Registration failed"));
        assertTrue(result.contains("Password"));
        System.out.println("  ✅ PASSED - Result: " + result);
    }
    
    @Test
    void testRegisterUser_InvalidPhone_ReturnsError() {
        System.out.println("Running: testRegisterUser_InvalidPhone_ReturnsError");
        String result = LoginRegisterSystem.registerUser(
            "test_user_invalidphone",
            "TestPass123!",
            "123456789"  // Invalid phone
        );
        assertTrue(result.contains("Registration failed"));
        assertTrue(result.contains("phone"));
        System.out.println("  ✅ PASSED - Result: " + result);
    }
    
    @Test
    void testRegisterUser_DuplicateUsername_ReturnsError() {
        System.out.println("Running: testRegisterUser_DuplicateUsername_ReturnsError");
        // First registration
        LoginRegisterSystem.registerUser("duplicate_user_test", "Pass123!", "+27712345678");
        
        // Second registration with same username
        String result = LoginRegisterSystem.registerUser("duplicate_user_test", "Pass456!", "+27719876543");
        
        assertTrue(result.contains("Registration failed"));
        assertTrue(result.contains("already exists"));
        System.out.println("  ✅ PASSED - Result: " + result);
    }
    
    // ============ TEST 5: loginUser() ============
    @Test
    void testLoginUser_ValidCredentials_ReturnsTrue() {
        System.out.println("Running: testLoginUser_ValidCredentials_ReturnsTrue");
        // Register first
        LoginRegisterSystem.registerUser("login_test_valid", "TestPass123!", "+27712345678");
        
        // Test login
        assertTrue(LoginRegisterSystem.loginUser("login_test_valid", "TestPass123!"));
        System.out.println("  ✅ PASSED");
    }
    
    @Test
    void testLoginUser_InvalidPassword_ReturnsFalse() {
        System.out.println("Running: testLoginUser_InvalidPassword_ReturnsFalse");
        // Register first
        LoginRegisterSystem.registerUser("login_test_invalidpass", "TestPass123!", "+27712345678");
        
        // Test login with wrong password
        assertFalse(LoginRegisterSystem.loginUser("login_test_invalidpass", "WrongPass!"));
        System.out.println("  ✅ PASSED");
    }
    
    @Test
    void testLoginUser_InvalidUsername_ReturnsFalse() {
        System.out.println("Running: testLoginUser_InvalidUsername_ReturnsFalse");
        // Try login with non-existent user
        assertFalse(LoginRegisterSystem.loginUser("nonexistent_user_xyz", "AnyPass123!"));
        System.out.println("  ✅ PASSED");
    }
    
    @Test
    void testLoginUser_EmptyCredentials_ReturnsFalse() {
        System.out.println("Running: testLoginUser_EmptyCredentials_ReturnsFalse");
        assertFalse(LoginRegisterSystem.loginUser("", ""));
        assertFalse(LoginRegisterSystem.loginUser("test", ""));
        assertFalse(LoginRegisterSystem.loginUser("", "password"));
        System.out.println("  ✅ PASSED");
    }
    
    // ============ TEST 6: returnLoginStatus() ============
    @Test
    void testReturnLoginStatus_Success_ReturnsWelcomeMessage() {
        System.out.println("Running: testReturnLoginStatus_Success_ReturnsWelcomeMessage");
        // Register and login a user
        LoginRegisterSystem.registerUser("status_test_success", "TestPass123!", "+27712345678");
        LoginRegisterSystem.loginUser("status_test_success", "TestPass123!");
        
        String result = LoginRegisterSystem.returnLoginStatus(true);
        assertTrue(result.contains("Login Successful"));
        assertTrue(result.contains("Welcome back"));
        System.out.println("  ✅ PASSED - Result: " + result);
    }
    
    @Test
    void testReturnLoginStatus_Failure_ReturnsErrorMessage() {
        System.out.println("Running: testReturnLoginStatus_Failure_ReturnsErrorMessage");
        String result = LoginRegisterSystem.returnLoginStatus(false);
        assertTrue(result.contains("Login Failed"));
        assertTrue(result.contains("Invalid username"));
        System.out.println("  ✅ PASSED - Result: " + result);
    }
}