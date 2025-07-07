package com.flipkart.raghav.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Users Entity Tests")
class UsersTest {

    private Users user1;
    private Users user2;
    private Users user3;

    @BeforeEach
    void setUp() {
        user1 = new Users(1, "john_doe", "password123");
        user2 = new Users(2, "jane_smith", "secure456");
        user3 = new Users(1, "john_doe", "password123");
    }

    @Test
    @DisplayName("Should create Users with no-args constructor")
    void testNoArgsConstructor() {
        Users user = new Users();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
    }

    @Test
    @DisplayName("Should create Users with all-args constructor")
    void testAllArgsConstructor() {
        Users user = new Users(100, "testuser", "testpass");
        assertEquals(100, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("testpass", user.getPassword());
    }

    @Test
    @DisplayName("Should set and get id correctly")
    void testIdGetterAndSetter() {
        Users user = new Users();
        user.setId(999);
        assertEquals(999, user.getId());
    }

    @Test
    @DisplayName("Should set and get username correctly")
    void testUsernameGetterAndSetter() {
        Users user = new Users();
        user.setUsername("newuser");
        assertEquals("newuser", user.getUsername());
    }

    @Test
    @DisplayName("Should set and get password correctly")
    void testPasswordGetterAndSetter() {
        Users user = new Users();
        user.setPassword("newpassword");
        assertEquals("newpassword", user.getPassword());
    }

    @Test
    @DisplayName("Should return true for equal objects")
    void testEqualsWithEqualObjects() {
        assertEquals(user1, user3);
    }

    @Test
    @DisplayName("Should return false for different objects")
    void testEqualsWithDifferentObjects() {
        assertNotEquals(user1, user2);
    }

    @Test
    @DisplayName("Should return false when comparing with null")
    void testEqualsWithNull() {
        assertNotEquals(null, user1);
    }

    @Test
    @DisplayName("Should return false when comparing with different type")
    void testEqualsWithDifferentType() {
        assertNotEquals(user1, "Not a Users object");
    }

    @Test
    @DisplayName("Should return false when id is different")
    void testEqualsWithDifferentId() {
        Users differentId = new Users(999, "john_doe", "password123");
        assertNotEquals(user1, differentId);
    }

    @Test
    @DisplayName("Should return false when username is different")
    void testEqualsWithDifferentUsername() {
        Users differentUsername = new Users(1, "different_user", "password123");
        assertNotEquals(user1, differentUsername);
    }

    @Test
    @DisplayName("Should return false when password is different")
    void testEqualsWithDifferentPassword() {
        Users differentPassword = new Users(1, "john_doe", "different_password");
        assertNotEquals(user1, differentPassword);
    }

    @Test
    @DisplayName("Should return same hash code for equal objects")
    void testHashCodeWithEqualObjects() {
        assertEquals(user1.hashCode(), user3.hashCode());
    }

    @Test
    @DisplayName("Should return different hash codes for different objects")
    void testHashCodeWithDifferentObjects() {
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    @DisplayName("Should return consistent hash code")
    void testHashCodeConsistency() {
        int hashCode1 = user1.hashCode();
        int hashCode2 = user1.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    @DisplayName("Should return meaningful string representation")
    void testToString() {
        String expected = "Users(id=1, username=john_doe, password=password123)";
        assertEquals(expected, user1.toString());
    }

    @Test
    @DisplayName("Should handle null username in toString")
    void testToStringWithNullUsername() {
        Users user = new Users(1, null, "password123");
        String result = user.toString();
        assertTrue(result.contains("username=null"));
    }

    @Test
    @DisplayName("Should handle null password in toString")
    void testToStringWithNullPassword() {
        Users user = new Users(1, "john_doe", null);
        String result = user.toString();
        assertTrue(result.contains("password=null"));
    }

    @Test
    @DisplayName("Should handle empty strings")
    void testEmptyStrings() {
        Users user = new Users(1, "", "");
        assertEquals("", user.getUsername());
        assertEquals("", user.getPassword());
    }

    @Test
    @DisplayName("Should handle zero id")
    void testZeroId() {
        Users user = new Users(0, "testuser", "testpass");
        assertEquals(0, user.getId());
    }

    @Test
    @DisplayName("Should handle negative id")
    void testNegativeId() {
        Users user = new Users(-1, "testuser", "testpass");
        assertEquals(-1, user.getId());
    }

    @Test
    @DisplayName("Should handle large id")
    void testLargeId() {
        Users user = new Users(Integer.MAX_VALUE, "testuser", "testpass");
        assertEquals(Integer.MAX_VALUE, user.getId());
    }

    @Test
    @DisplayName("Should handle special characters in username")
    void testSpecialCharactersInUsername() {
        Users user = new Users(1, "user@domain.com", "password");
        assertEquals("user@domain.com", user.getUsername());
    }

    @Test
    @DisplayName("Should handle special characters in password")
    void testSpecialCharactersInPassword() {
        Users user = new Users(1, "username", "p@ssw0rd!@#$%");
        assertEquals("p@ssw0rd!@#$%", user.getPassword());
    }

    @Test
    @DisplayName("Should handle very long username")
    void testLongUsername() {
        String longUsername = "a".repeat(1000);
        Users user = new Users(1, longUsername, "password");
        assertEquals(longUsername, user.getUsername());
    }

    @Test
    @DisplayName("Should handle very long password")
    void testLongPassword() {
        String longPassword = "p".repeat(1000);
        Users user = new Users(1, "username", longPassword);
        assertEquals(longPassword, user.getPassword());
    }

    @Test
    @DisplayName("Should handle unicode characters")
    void testUnicodeCharacters() {
        Users user = new Users(1, "用户", "密码");
        assertEquals("用户", user.getUsername());
        assertEquals("密码", user.getPassword());
    }
}