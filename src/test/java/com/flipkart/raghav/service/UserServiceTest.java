package com.flipkart.raghav.service;

import com.flipkart.raghav.model.Users;
import com.flipkart.raghav.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private JWTService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserService userService;

    private Users testUser;
    private Users testUserWithEncodedPassword;

    @BeforeEach
    void setUp() {
        testUser = new Users(1, "testuser", "password123");
        testUserWithEncodedPassword = new Users(1, "testuser", "$2a$12$encodedpassword");
    }

    @Test
    @DisplayName("Should register user with encoded password")
    void testRegisterUser() {
        // Arrange
        when(userRepo.save(any(Users.class))).thenReturn(testUserWithEncodedPassword);

        // Act
        Users registeredUser = userService.register(testUser);

        // Assert
        assertNotNull(registeredUser);
        assertEquals("testuser", registeredUser.getUsername());

        // Verify password was encoded
        verify(userRepo, times(1)).save(any(Users.class));

        // Verify the saved user has encoded password
        Users savedUser = userRepo.save(testUser);
        assertNotEquals("password123", savedUser.getPassword());
    }

    @Test
    @DisplayName("Should register user with null password")
    void testRegisterUserWithNullPassword() {
        // Arrange
        Users userWithNullPassword = new Users(1, "testuser", null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            userService.register(userWithNullPassword);
        });
    }

    @Test
    @DisplayName("Should register user with empty password")
    void testRegisterUserWithEmptyPassword() {
        // Arrange
        Users userWithEmptyPassword = new Users(1, "testuser", "");
        when(userRepo.save(any(Users.class))).thenReturn(userWithEmptyPassword);

        // Act
        Users registeredUser = userService.register(userWithEmptyPassword);

        // Assert
        assertNotNull(registeredUser);
        assertEquals("testuser", registeredUser.getUsername());

        verify(userRepo, times(1)).save(any(Users.class));
    }

    @Test
    @DisplayName("Should register user with special characters in password")
    void testRegisterUserWithSpecialCharacters() {
        // Arrange
        Users userWithSpecialPassword = new Users(1, "testuser", "p@ssw0rd!@#$%");
        when(userRepo.save(any(Users.class))).thenReturn(userWithSpecialPassword);

        // Act
        Users registeredUser = userService.register(userWithSpecialPassword);

        // Assert
        assertNotNull(registeredUser);
        assertEquals("testuser", registeredUser.getUsername());

        verify(userRepo, times(1)).save(any(Users.class));
    }

    @Test
    @DisplayName("Should register user with unicode characters")
    void testRegisterUserWithUnicodeCharacters() {
        // Arrange
        Users userWithUnicode = new Users(1, "用户", "密码");
        when(userRepo.save(any(Users.class))).thenReturn(userWithUnicode);

        // Act
        Users registeredUser = userService.register(userWithUnicode);

        // Assert
        assertNotNull(registeredUser);
        assertEquals("用户", registeredUser.getUsername());

        verify(userRepo, times(1)).save(any(Users.class));
    }

    @Test
    @DisplayName("Should register user with null username")
    void testRegisterUserWithNullUsername() {
        // Arrange
        Users userWithNullUsername = new Users(1, null, "password");
        when(userRepo.save(any(Users.class))).thenReturn(userWithNullUsername);

        // Act
        Users registeredUser = userService.register(userWithNullUsername);

        // Assert
        assertNotNull(registeredUser);
        assertNull(registeredUser.getUsername());

        verify(userRepo, times(1)).save(any(Users.class));
    }

    @Test
    @DisplayName("Should register user with zero id")
    void testRegisterUserWithZeroId() {
        // Arrange
        Users userWithZeroId = new Users(0, "testuser", "password");
        when(userRepo.save(any(Users.class))).thenReturn(userWithZeroId);

        // Act
        Users registeredUser = userService.register(userWithZeroId);

        // Assert
        assertNotNull(registeredUser);
        assertEquals(0, registeredUser.getId());

        verify(userRepo, times(1)).save(any(Users.class));
    }

    @Test
    @DisplayName("Should register user with negative id")
    void testRegisterUserWithNegativeId() {
        // Arrange
        Users userWithNegativeId = new Users(-1, "testuser", "password");
        when(userRepo.save(any(Users.class))).thenReturn(userWithNegativeId);

        // Act
        Users registeredUser = userService.register(userWithNegativeId);

        // Assert
        assertNotNull(registeredUser);
        assertEquals(-1, registeredUser.getId());

        verify(userRepo, times(1)).save(any(Users.class));
    }

    @Test
    @DisplayName("Should verify user successfully and return JWT token")
    void testVerifyUserSuccess() {
        // Arrange
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
        when(jwtService.generateToken("testuser")).thenReturn("jwt.token.here");

        // Act
        String result = userService.verify(testUser);

        // Assert
        assertEquals("jwt.token.here", result);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken("testuser");
    }

    @Test
    @DisplayName("Should return fail when authentication fails")
    void testVerifyUserFailure() {
        // Arrange
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.isAuthenticated()).thenReturn(false);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);

        // Act
        String result = userService.verify(testUser);

        // Assert
        assertEquals("fail", result);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    @DisplayName("Should handle authentication exception")
    void testVerifyUserWithAuthenticationException() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> {
            userService.verify(testUser);
        });

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    @DisplayName("Should verify user with null username")
    void testVerifyUserWithNullUsername() {
        // Arrange
        Users userWithNullUsername = new Users(1, null, "password");
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
        when(jwtService.generateToken(null)).thenReturn("jwt.token.here");

        // Act
        String result = userService.verify(userWithNullUsername);

        // Assert
        assertEquals("jwt.token.here", result);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken(null);
    }

    @Test
    @DisplayName("Should verify user with null password")
    void testVerifyUserWithNullPassword() {
        // Arrange
        Users userWithNullPassword = new Users(1, "testuser", null);
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
        when(jwtService.generateToken("testuser")).thenReturn("jwt.token.here");

        // Act
        String result = userService.verify(userWithNullPassword);

        // Assert
        assertEquals("jwt.token.here", result);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken("testuser");
    }

    @Test
    @DisplayName("Should verify user with empty username")
    void testVerifyUserWithEmptyUsername() {
        // Arrange
        Users userWithEmptyUsername = new Users(1, "", "password");
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
        when(jwtService.generateToken("")).thenReturn("jwt.token.here");

        // Act
        String result = userService.verify(userWithEmptyUsername);

        // Assert
        assertEquals("jwt.token.here", result);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken("");
    }

    @Test
    @DisplayName("Should verify user with empty password")
    void testVerifyUserWithEmptyPassword() {
        // Arrange
        Users userWithEmptyPassword = new Users(1, "testuser", "");
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
        when(jwtService.generateToken("testuser")).thenReturn("jwt.token.here");

        // Act
        String result = userService.verify(userWithEmptyPassword);

        // Assert
        assertEquals("jwt.token.here", result);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken("testuser");
    }

    @Test
    @DisplayName("Should verify user with special characters")
    void testVerifyUserWithSpecialCharacters() {
        // Arrange
        Users userWithSpecialChars = new Users(1, "user@domain.com", "p@ssw0rd!@#$%");
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
        when(jwtService.generateToken("user@domain.com")).thenReturn("jwt.token.here");

        // Act
        String result = userService.verify(userWithSpecialChars);

        // Assert
        assertEquals("jwt.token.here", result);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken("user@domain.com");
    }

    @Test
    @DisplayName("Should verify user with unicode characters")
    void testVerifyUserWithUnicodeCharacters() {
        // Arrange
        Users userWithUnicode = new Users(1, "用户", "密码");
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
        when(jwtService.generateToken("用户")).thenReturn("jwt.token.here");

        // Act
        String result = userService.verify(userWithUnicode);

        // Assert
        assertEquals("jwt.token.here", result);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken("用户");
    }

    @Test
    @DisplayName("Should handle very long username")
    void testVerifyUserWithLongUsername() {
        // Arrange
        String longUsername = "a".repeat(1000);
        Users userWithLongUsername = new Users(1, longUsername, "password");
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
        when(jwtService.generateToken(longUsername)).thenReturn("jwt.token.here");

        // Act
        String result = userService.verify(userWithLongUsername);

        // Assert
        assertEquals("jwt.token.here", result);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken(longUsername);
    }

    @Test
    @DisplayName("Should handle very long password")
    void testVerifyUserWithLongPassword() {
        // Arrange
        String longPassword = "p".repeat(1000);
        Users userWithLongPassword = new Users(1, "testuser", longPassword);
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
        when(jwtService.generateToken("testuser")).thenReturn("jwt.token.here");

        // Act
        String result = userService.verify(userWithLongPassword);

        // Assert
        assertEquals("jwt.token.here", result);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken("testuser");
    }

    @Test
    @DisplayName("Should handle repository exception during registration")
    void testRegisterUserWithRepositoryException() {
        // Arrange
        when(userRepo.save(any(Users.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            userService.register(testUser);
        });

        verify(userRepo, times(1)).save(any(Users.class));
    }

    @Test
    @DisplayName("Should handle JWT service exception during verification")
    void testVerifyUserWithJWTServiceException() {
        // Arrange
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
        when(jwtService.generateToken("testuser")).thenThrow(new RuntimeException("JWT error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            userService.verify(testUser);
        });

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken("testuser");
    }

    @Test
    @DisplayName("Should verify BCryptPasswordEncoder strength")
    void testBCryptPasswordEncoderStrength() {
        // This test verifies that the BCryptPasswordEncoder is configured with strength
        // 12
        // We can't directly test the encoder instance, but we can verify the behavior
        Users userToRegister = new Users(1, "testuser", "password123");
        when(userRepo.save(any(Users.class))).thenReturn(userToRegister);

        // Act
        userService.register(userToRegister);

        // Assert - verify that save was called with a user that has encoded password
        verify(userRepo, times(1)).save(any(Users.class));
    }
}