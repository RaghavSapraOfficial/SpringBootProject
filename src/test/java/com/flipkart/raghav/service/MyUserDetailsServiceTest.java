package com.flipkart.raghav.service;

import com.flipkart.raghav.model.UserPrincipal;
import com.flipkart.raghav.model.Users;
import com.flipkart.raghav.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MyUserDetailsService Tests")
class MyUserDetailsServiceTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private MyUserDetailsService userDetailsService;

    private Users testUser;

    @BeforeEach
    void setUp() {
        testUser = new Users(1, "testuser", "password123");
    }

    @Test
    @DisplayName("Should load user by username when user exists")
    void testLoadUserByUsernameWhenUserExists() {
        // Arrange
        when(userRepo.findByUsername("testuser")).thenReturn(testUser);

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        // Assert
        assertNotNull(userDetails);
        assertTrue(userDetails instanceof UserPrincipal);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());

        verify(userRepo, times(1)).findByUsername("testuser");
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user does not exist")
    void testLoadUserByUsernameWhenUserDoesNotExist() {
        // Arrange
        when(userRepo.findByUsername("nonexistent")).thenReturn(null);

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent");
        });

        assertEquals("No User Found!!!", exception.getMessage());
        verify(userRepo, times(1)).findByUsername("nonexistent");
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when username is null")
    void testLoadUserByUsernameWithNullUsername() {
        // Arrange
        when(userRepo.findByUsername(null)).thenReturn(null);

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(null);
        });

        assertEquals("No User Found!!!", exception.getMessage());
        verify(userRepo, times(1)).findByUsername(null);
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when username is empty")
    void testLoadUserByUsernameWithEmptyUsername() {
        // Arrange
        when(userRepo.findByUsername("")).thenReturn(null);

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("");
        });

        assertEquals("No User Found!!!", exception.getMessage());
        verify(userRepo, times(1)).findByUsername("");
    }

    @Test
    @DisplayName("Should load user with special characters in username")
    void testLoadUserByUsernameWithSpecialCharacters() {
        // Arrange
        Users specialUser = new Users(2, "user@domain.com", "password");
        when(userRepo.findByUsername("user@domain.com")).thenReturn(specialUser);

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("user@domain.com");

        // Assert
        assertNotNull(userDetails);
        assertEquals("user@domain.com", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());

        verify(userRepo, times(1)).findByUsername("user@domain.com");
    }

    @Test
    @DisplayName("Should load user with unicode characters in username")
    void testLoadUserByUsernameWithUnicodeCharacters() {
        // Arrange
        Users unicodeUser = new Users(3, "用户", "密码");
        when(userRepo.findByUsername("用户")).thenReturn(unicodeUser);

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("用户");

        // Assert
        assertNotNull(userDetails);
        assertEquals("用户", userDetails.getUsername());
        assertEquals("密码", userDetails.getPassword());

        verify(userRepo, times(1)).findByUsername("用户");
    }

    @Test
    @DisplayName("Should load user with null password")
    void testLoadUserByUsernameWithNullPassword() {
        // Arrange
        Users userWithNullPassword = new Users(4, "nullpassuser", null);
        when(userRepo.findByUsername("nullpassuser")).thenReturn(userWithNullPassword);

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("nullpassuser");

        // Assert
        assertNotNull(userDetails);
        assertEquals("nullpassuser", userDetails.getUsername());
        assertNull(userDetails.getPassword());

        verify(userRepo, times(1)).findByUsername("nullpassuser");
    }

    @Test
    @DisplayName("Should load user with empty password")
    void testLoadUserByUsernameWithEmptyPassword() {
        // Arrange
        Users userWithEmptyPassword = new Users(5, "emptypassuser", "");
        when(userRepo.findByUsername("emptypassuser")).thenReturn(userWithEmptyPassword);

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("emptypassuser");

        // Assert
        assertNotNull(userDetails);
        assertEquals("emptypassuser", userDetails.getUsername());
        assertEquals("", userDetails.getPassword());

        verify(userRepo, times(1)).findByUsername("emptypassuser");
    }

    @Test
    @DisplayName("Should load user with zero id")
    void testLoadUserByUsernameWithZeroId() {
        // Arrange
        Users zeroIdUser = new Users(0, "zeroiduser", "password");
        when(userRepo.findByUsername("zeroiduser")).thenReturn(zeroIdUser);

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("zeroiduser");

        // Assert
        assertNotNull(userDetails);
        assertEquals("zeroiduser", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());

        verify(userRepo, times(1)).findByUsername("zeroiduser");
    }

    @Test
    @DisplayName("Should load user with negative id")
    void testLoadUserByUsernameWithNegativeId() {
        // Arrange
        Users negativeIdUser = new Users(-1, "negativeiduser", "password");
        when(userRepo.findByUsername("negativeiduser")).thenReturn(negativeIdUser);

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("negativeiduser");

        // Assert
        assertNotNull(userDetails);
        assertEquals("negativeiduser", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());

        verify(userRepo, times(1)).findByUsername("negativeiduser");
    }

    @Test
    @DisplayName("Should load user with very long username")
    void testLoadUserByUsernameWithLongUsername() {
        // Arrange
        String longUsername = "a".repeat(1000);
        Users longUsernameUser = new Users(6, longUsername, "password");
        when(userRepo.findByUsername(longUsername)).thenReturn(longUsernameUser);

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(longUsername);

        // Assert
        assertNotNull(userDetails);
        assertEquals(longUsername, userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());

        verify(userRepo, times(1)).findByUsername(longUsername);
    }

    @Test
    @DisplayName("Should load user with very long password")
    void testLoadUserByUsernameWithLongPassword() {
        // Arrange
        String longPassword = "p".repeat(1000);
        Users longPasswordUser = new Users(7, "longpassuser", longPassword);
        when(userRepo.findByUsername("longpassuser")).thenReturn(longPasswordUser);

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("longpassuser");

        // Assert
        assertNotNull(userDetails);
        assertEquals("longpassuser", userDetails.getUsername());
        assertEquals(longPassword, userDetails.getPassword());

        verify(userRepo, times(1)).findByUsername("longpassuser");
    }

    @Test
    @DisplayName("Should load user with whitespace in username")
    void testLoadUserByUsernameWithWhitespace() {
        // Arrange
        Users whitespaceUser = new Users(8, " test user ", "password");
        when(userRepo.findByUsername(" test user ")).thenReturn(whitespaceUser);

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(" test user ");

        // Assert
        assertNotNull(userDetails);
        assertEquals(" test user ", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());

        verify(userRepo, times(1)).findByUsername(" test user ");
    }

    @Test
    @DisplayName("Should load user with numeric username")
    void testLoadUserByUsernameWithNumericUsername() {
        // Arrange
        Users numericUser = new Users(9, "12345", "password");
        when(userRepo.findByUsername("12345")).thenReturn(numericUser);

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("12345");

        // Assert
        assertNotNull(userDetails);
        assertEquals("12345", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());

        verify(userRepo, times(1)).findByUsername("12345");
    }

    @Test
    @DisplayName("Should load user with mixed case username")
    void testLoadUserByUsernameWithMixedCaseUsername() {
        // Arrange
        Users mixedCaseUser = new Users(10, "TestUser123", "password");
        when(userRepo.findByUsername("TestUser123")).thenReturn(mixedCaseUser);

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("TestUser123");

        // Assert
        assertNotNull(userDetails);
        assertEquals("TestUser123", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());

        verify(userRepo, times(1)).findByUsername("TestUser123");
    }

    @Test
    @DisplayName("Should verify UserPrincipal properties")
    void testUserPrincipalProperties() {
        // Arrange
        when(userRepo.findByUsername("testuser")).thenReturn(testUser);

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        // Assert
        assertTrue(userDetails instanceof UserPrincipal);
        UserPrincipal userPrincipal = (UserPrincipal) userDetails;

        // Test UserDetails interface methods
        assertTrue(userPrincipal.isAccountNonExpired());
        assertTrue(userPrincipal.isAccountNonLocked());
        assertTrue(userPrincipal.isCredentialsNonExpired());
        assertTrue(userPrincipal.isEnabled());

        // Test authorities
        assertEquals(1, userPrincipal.getAuthorities().size());
        assertEquals("USER", userPrincipal.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    @DisplayName("Should handle multiple calls to same username")
    void testMultipleCallsToSameUsername() {
        // Arrange
        when(userRepo.findByUsername("testuser")).thenReturn(testUser);

        // Act
        UserDetails userDetails1 = userDetailsService.loadUserByUsername("testuser");
        UserDetails userDetails2 = userDetailsService.loadUserByUsername("testuser");

        // Assert
        assertNotNull(userDetails1);
        assertNotNull(userDetails2);
        assertEquals(userDetails1.getUsername(), userDetails2.getUsername());
        assertEquals(userDetails1.getPassword(), userDetails2.getPassword());

        verify(userRepo, times(2)).findByUsername("testuser");
    }

    @Test
    @DisplayName("Should handle repository exception")
    void testRepositoryException() {
        // Arrange
        when(userRepo.findByUsername("testuser")).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            userDetailsService.loadUserByUsername("testuser");
        });

        verify(userRepo, times(1)).findByUsername("testuser");
    }
}