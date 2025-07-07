package com.flipkart.raghav.repository;

import com.flipkart.raghav.model.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserRepo Repository Tests")
class UserRepoTest {

    @Mock
    private UserRepo userRepo;

    private Users testUser1;
    private Users testUser2;
    private Users testUser3;

    @BeforeEach
    void setUp() {
        testUser1 = new Users(1, "john_doe", "password123");
        testUser2 = new Users(2, "jane_smith", "secure456");
        testUser3 = new Users(3, "admin_user", "admin123");
    }

    @Test
    @DisplayName("Should find user by username when user exists")
    void testFindByUsernameWhenUserExists() {
        // Arrange
        when(userRepo.findByUsername("john_doe")).thenReturn(testUser1);

        // Act
        Users foundUser = userRepo.findByUsername("john_doe");

        // Assert
        assertNotNull(foundUser);
        assertEquals("john_doe", foundUser.getUsername());
        assertEquals("password123", foundUser.getPassword());
        assertEquals(1, foundUser.getId());

        verify(userRepo, times(1)).findByUsername("john_doe");
    }

    @Test
    @DisplayName("Should return null when username does not exist")
    void testFindByUsernameWhenUserDoesNotExist() {
        // Arrange
        when(userRepo.findByUsername("nonexistent_user")).thenReturn(null);

        // Act
        Users foundUser = userRepo.findByUsername("nonexistent_user");

        // Assert
        assertNull(foundUser);
        verify(userRepo, times(1)).findByUsername("nonexistent_user");
    }

    @Test
    @DisplayName("Should return null when username is null")
    void testFindByUsernameWithNullUsername() {
        // Arrange
        when(userRepo.findByUsername(null)).thenReturn(null);

        // Act
        Users foundUser = userRepo.findByUsername(null);

        // Assert
        assertNull(foundUser);
        verify(userRepo, times(1)).findByUsername(null);
    }

    @Test
    @DisplayName("Should return null when username is empty")
    void testFindByUsernameWithEmptyUsername() {
        // Arrange
        when(userRepo.findByUsername("")).thenReturn(null);

        // Act
        Users foundUser = userRepo.findByUsername("");

        // Assert
        assertNull(foundUser);
        verify(userRepo, times(1)).findByUsername("");
    }

    @Test
    @DisplayName("Should find user by username with special characters")
    void testFindByUsernameWithSpecialCharacters() {
        // Arrange
        Users specialUser = new Users(4, "user@domain.com", "password");
        when(userRepo.findByUsername("user@domain.com")).thenReturn(specialUser);

        // Act
        Users foundUser = userRepo.findByUsername("user@domain.com");

        // Assert
        assertNotNull(foundUser);
        assertEquals("user@domain.com", foundUser.getUsername());
        verify(userRepo, times(1)).findByUsername("user@domain.com");
    }

    @Test
    @DisplayName("Should find user by username with unicode characters")
    void testFindByUsernameWithUnicodeCharacters() {
        // Arrange
        Users unicodeUser = new Users(5, "用户", "密码");
        when(userRepo.findByUsername("用户")).thenReturn(unicodeUser);

        // Act
        Users foundUser = userRepo.findByUsername("用户");

        // Assert
        assertNotNull(foundUser);
        assertEquals("用户", foundUser.getUsername());
        verify(userRepo, times(1)).findByUsername("用户");
    }

    @Test
    @DisplayName("Should find user by username case sensitive")
    void testFindByUsernameCaseSensitive() {
        // Arrange
        when(userRepo.findByUsername("JOHN_DOE")).thenReturn(null);

        // Act
        Users foundUser = userRepo.findByUsername("JOHN_DOE");

        // Assert
        assertNull(foundUser); // Should be case sensitive
        verify(userRepo, times(1)).findByUsername("JOHN_DOE");
    }

    @Test
    @DisplayName("Should save new user")
    void testSaveNewUser() {
        // Arrange
        Users newUser = new Users(6, "newuser", "newpassword");
        when(userRepo.save(any(Users.class))).thenReturn(newUser);
        when(userRepo.findByUsername("newuser")).thenReturn(newUser);

        // Act
        Users savedUser = userRepo.save(newUser);

        // Assert
        assertNotNull(savedUser);
        assertEquals("newuser", savedUser.getUsername());
        assertEquals("newpassword", savedUser.getPassword());

        // Verify it's actually saved in database
        Users foundUser = userRepo.findByUsername("newuser");
        assertNotNull(foundUser);
        assertEquals("newuser", foundUser.getUsername());

        verify(userRepo, times(1)).save(any(Users.class));
        verify(userRepo, times(1)).findByUsername("newuser");
    }

    @Test
    @DisplayName("Should update existing user")
    void testUpdateExistingUser() {
        // Arrange
        Users userToUpdate = new Users(1, "john_doe", "updated_password");
        when(userRepo.findByUsername("john_doe")).thenReturn(userToUpdate);
        when(userRepo.save(any(Users.class))).thenReturn(userToUpdate);

        // Act
        Users updatedUser = userRepo.save(userToUpdate);

        // Assert
        assertNotNull(updatedUser);
        assertEquals("updated_password", updatedUser.getPassword());

        // Verify update in database
        Users foundUser = userRepo.findByUsername("john_doe");
        assertEquals("updated_password", foundUser.getPassword());

        verify(userRepo, times(1)).save(any(Users.class));
        verify(userRepo, times(1)).findByUsername("john_doe");
    }

    @Test
    @DisplayName("Should find user by id")
    void testFindById() {
        // Arrange
        when(userRepo.findById(1)).thenReturn(Optional.of(testUser1));

        // Act
        Optional<Users> foundUser = userRepo.findById(1);

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("john_doe", foundUser.get().getUsername());
        verify(userRepo, times(1)).findById(1);
    }

    @Test
    @DisplayName("Should return empty optional when id does not exist")
    void testFindByIdWhenIdDoesNotExist() {
        // Arrange
        when(userRepo.findById(999)).thenReturn(Optional.empty());

        // Act
        Optional<Users> foundUser = userRepo.findById(999);

        // Assert
        assertFalse(foundUser.isPresent());
        verify(userRepo, times(1)).findById(999);
    }

    @Test
    @DisplayName("Should find all users")
    void testFindAll() {
        // Arrange
        List<Users> allUsers = Arrays.asList(testUser1, testUser2, testUser3);
        when(userRepo.findAll()).thenReturn(allUsers);

        // Act
        List<Users> result = userRepo.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());

        // Verify all expected users are present
        assertTrue(result.stream().anyMatch(user -> "john_doe".equals(user.getUsername())));
        assertTrue(result.stream().anyMatch(user -> "jane_smith".equals(user.getUsername())));
        assertTrue(result.stream().anyMatch(user -> "admin_user".equals(user.getUsername())));

        verify(userRepo, times(1)).findAll();
    }

    @Test
    @DisplayName("Should delete user by id")
    void testDeleteById() {
        // Arrange
        doNothing().when(userRepo).deleteById(1);
        when(userRepo.findById(1)).thenReturn(Optional.empty());
        when(userRepo.findByUsername("john_doe")).thenReturn(null);

        // Act
        userRepo.deleteById(1);

        // Assert
        // Verify user is deleted
        Optional<Users> foundUser = userRepo.findById(1);
        assertFalse(foundUser.isPresent());

        // Verify findByUsername returns null
        Users foundByUsername = userRepo.findByUsername("john_doe");
        assertNull(foundByUsername);

        verify(userRepo, times(1)).deleteById(1);
        verify(userRepo, times(1)).findById(1);
        verify(userRepo, times(1)).findByUsername("john_doe");
    }

    @Test
    @DisplayName("Should check if user exists by id")
    void testExistsById() {
        // Arrange
        when(userRepo.existsById(1)).thenReturn(true);
        when(userRepo.existsById(999)).thenReturn(false);

        // Act & Assert
        assertTrue(userRepo.existsById(1));
        assertFalse(userRepo.existsById(999));

        verify(userRepo, times(1)).existsById(1);
        verify(userRepo, times(1)).existsById(999);
    }

    @Test
    @DisplayName("Should count all users")
    void testCount() {
        // Arrange
        when(userRepo.count()).thenReturn(3L);

        // Act
        long count = userRepo.count();

        // Assert
        assertEquals(3, count);
        verify(userRepo, times(1)).count();
    }

    @Test
    @DisplayName("Should handle very long username")
    void testFindByUsernameWithLongUsername() {
        // Arrange
        String longUsername = "a".repeat(1000);
        Users longUser = new Users(7, longUsername, "password");
        when(userRepo.findByUsername(longUsername)).thenReturn(longUser);

        // Act
        Users foundUser = userRepo.findByUsername(longUsername);

        // Assert
        assertNotNull(foundUser);
        assertEquals(longUsername, foundUser.getUsername());
        verify(userRepo, times(1)).findByUsername(longUsername);
    }

    @Test
    @DisplayName("Should handle very long password")
    void testFindByUsernameWithLongPassword() {
        // Arrange
        String longPassword = "p".repeat(1000);
        Users longPasswordUser = new Users(8, "longpassuser", longPassword);
        when(userRepo.findByUsername("longpassuser")).thenReturn(longPasswordUser);

        // Act
        Users foundUser = userRepo.findByUsername("longpassuser");

        // Assert
        assertNotNull(foundUser);
        assertEquals(longPassword, foundUser.getPassword());
        verify(userRepo, times(1)).findByUsername("longpassuser");
    }

    @Test
    @DisplayName("Should handle zero id")
    void testFindByIdWithZeroId() {
        // Arrange
        Users zeroIdUser = new Users(0, "zeroid", "password");
        when(userRepo.findById(0)).thenReturn(Optional.of(zeroIdUser));

        // Act
        Optional<Users> foundUser = userRepo.findById(0);

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("zeroid", foundUser.get().getUsername());
        verify(userRepo, times(1)).findById(0);
    }

    @Test
    @DisplayName("Should handle negative id")
    void testFindByIdWithNegativeId() {
        // Arrange
        Users negativeIdUser = new Users(-1, "negativeid", "password");
        when(userRepo.findById(-1)).thenReturn(Optional.of(negativeIdUser));

        // Act
        Optional<Users> foundUser = userRepo.findById(-1);

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("negativeid", foundUser.get().getUsername());
        verify(userRepo, times(1)).findById(-1);
    }
}