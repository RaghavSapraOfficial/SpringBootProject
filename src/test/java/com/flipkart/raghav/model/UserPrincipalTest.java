package com.flipkart.raghav.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserPrincipal Tests")
class UserPrincipalTest {

    private Users testUser;
    private UserPrincipal userPrincipal;

    @BeforeEach
    void setUp() {
        testUser = new Users(1, "testuser", "testpassword");
        userPrincipal = new UserPrincipal(testUser);
    }

    @Test
    @DisplayName("Should create UserPrincipal with valid user")
    void testConstructor() {
        assertNotNull(userPrincipal);
    }

    @Test
    @DisplayName("Should return correct authorities")
    void testGetAuthorities() {
        var authorities = userPrincipal.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());

        GrantedAuthority authority = authorities.iterator().next();
        assertTrue(authority instanceof SimpleGrantedAuthority);
        assertEquals("USER", authority.getAuthority());
    }

    @Test
    @DisplayName("Should return user password")
    void testGetPassword() {
        assertEquals("testpassword", userPrincipal.getPassword());
    }

    @Test
    @DisplayName("Should return user username")
    void testGetUsername() {
        assertEquals("testuser", userPrincipal.getUsername());
    }

    @Test
    @DisplayName("Should return true for account non-expired")
    void testIsAccountNonExpired() {
        assertTrue(userPrincipal.isAccountNonExpired());
    }

    @Test
    @DisplayName("Should return true for account non-locked")
    void testIsAccountNonLocked() {
        assertTrue(userPrincipal.isAccountNonLocked());
    }

    @Test
    @DisplayName("Should return true for credentials non-expired")
    void testIsCredentialsNonExpired() {
        assertTrue(userPrincipal.isCredentialsNonExpired());
    }

    @Test
    @DisplayName("Should return true for enabled account")
    void testIsEnabled() {
        assertTrue(userPrincipal.isEnabled());
    }

    @Test
    @DisplayName("Should handle null password")
    void testGetPasswordWithNullPassword() {
        Users userWithNullPassword = new Users(1, "testuser", null);
        UserPrincipal principal = new UserPrincipal(userWithNullPassword);
        assertNull(principal.getPassword());
    }

    @Test
    @DisplayName("Should handle null username")
    void testGetUsernameWithNullUsername() {
        Users userWithNullUsername = new Users(1, null, "testpassword");
        UserPrincipal principal = new UserPrincipal(userWithNullUsername);
        assertNull(principal.getUsername());
    }

    @Test
    @DisplayName("Should handle empty password")
    void testGetPasswordWithEmptyPassword() {
        Users userWithEmptyPassword = new Users(1, "testuser", "");
        UserPrincipal principal = new UserPrincipal(userWithEmptyPassword);
        assertEquals("", principal.getPassword());
    }

    @Test
    @DisplayName("Should handle empty username")
    void testGetUsernameWithEmptyUsername() {
        Users userWithEmptyUsername = new Users(1, "", "testpassword");
        UserPrincipal principal = new UserPrincipal(userWithEmptyUsername);
        assertEquals("", principal.getUsername());
    }

    @Test
    @DisplayName("Should handle special characters in username")
    void testGetUsernameWithSpecialCharacters() {
        Users userWithSpecialChars = new Users(1, "user@domain.com", "password");
        UserPrincipal principal = new UserPrincipal(userWithSpecialChars);
        assertEquals("user@domain.com", principal.getUsername());
    }

    @Test
    @DisplayName("Should handle special characters in password")
    void testGetPasswordWithSpecialCharacters() {
        Users userWithSpecialPassword = new Users(1, "username", "p@ssw0rd!@#$%");
        UserPrincipal principal = new UserPrincipal(userWithSpecialPassword);
        assertEquals("p@ssw0rd!@#$%", principal.getPassword());
    }

    @Test
    @DisplayName("Should handle very long username")
    void testGetUsernameWithLongUsername() {
        String longUsername = "a".repeat(1000);
        Users userWithLongUsername = new Users(1, longUsername, "password");
        UserPrincipal principal = new UserPrincipal(userWithLongUsername);
        assertEquals(longUsername, principal.getUsername());
    }

    @Test
    @DisplayName("Should handle very long password")
    void testGetPasswordWithLongPassword() {
        String longPassword = "p".repeat(1000);
        Users userWithLongPassword = new Users(1, "username", longPassword);
        UserPrincipal principal = new UserPrincipal(userWithLongPassword);
        assertEquals(longPassword, principal.getPassword());
    }

    @Test
    @DisplayName("Should handle unicode characters in username")
    void testGetUsernameWithUnicodeCharacters() {
        Users userWithUnicode = new Users(1, "用户", "password");
        UserPrincipal principal = new UserPrincipal(userWithUnicode);
        assertEquals("用户", principal.getUsername());
    }

    @Test
    @DisplayName("Should handle unicode characters in password")
    void testGetPasswordWithUnicodeCharacters() {
        Users userWithUnicodePassword = new Users(1, "username", "密码");
        UserPrincipal principal = new UserPrincipal(userWithUnicodePassword);
        assertEquals("密码", principal.getPassword());
    }

    @Test
    @DisplayName("Should handle zero id user")
    void testWithZeroIdUser() {
        Users zeroIdUser = new Users(0, "testuser", "testpassword");
        UserPrincipal principal = new UserPrincipal(zeroIdUser);
        assertEquals("testuser", principal.getUsername());
        assertEquals("testpassword", principal.getPassword());
    }

    @Test
    @DisplayName("Should handle negative id user")
    void testWithNegativeIdUser() {
        Users negativeIdUser = new Users(-1, "testuser", "testpassword");
        UserPrincipal principal = new UserPrincipal(negativeIdUser);
        assertEquals("testuser", principal.getUsername());
        assertEquals("testpassword", principal.getPassword());
    }

    @Test
    @DisplayName("Should handle maximum integer id user")
    void testWithMaxIdUser() {
        Users maxIdUser = new Users(Integer.MAX_VALUE, "testuser", "testpassword");
        UserPrincipal principal = new UserPrincipal(maxIdUser);
        assertEquals("testuser", principal.getUsername());
        assertEquals("testpassword", principal.getPassword());
    }

    @Test
    @DisplayName("Should return consistent authorities across multiple calls")
    void testGetAuthoritiesConsistency() {
        var authorities1 = userPrincipal.getAuthorities();
        var authorities2 = userPrincipal.getAuthorities();

        assertEquals(authorities1.size(), authorities2.size());
        assertEquals(authorities1.iterator().next().getAuthority(),
                authorities2.iterator().next().getAuthority());
    }

    @Test
    @DisplayName("Should return immutable authorities collection")
    void testGetAuthoritiesImmutability() {
        var authorities = userPrincipal.getAuthorities();
        assertThrows(UnsupportedOperationException.class, () -> {
            ((java.util.Collection<GrantedAuthority>) authorities).add(new SimpleGrantedAuthority("ADMIN"));
        });
    }

    @Test
    @DisplayName("Should handle multiple UserPrincipal instances with same user")
    void testMultipleInstancesWithSameUser() {
        UserPrincipal principal1 = new UserPrincipal(testUser);
        UserPrincipal principal2 = new UserPrincipal(testUser);

        assertEquals(principal1.getUsername(), principal2.getUsername());
        assertEquals(principal1.getPassword(), principal2.getPassword());
        assertEquals(principal1.getAuthorities().size(), principal2.getAuthorities().size());
    }

    @Test
    @DisplayName("Should handle multiple UserPrincipal instances with different users")
    void testMultipleInstancesWithDifferentUsers() {
        Users user1 = new Users(1, "user1", "pass1");
        Users user2 = new Users(2, "user2", "pass2");

        UserPrincipal principal1 = new UserPrincipal(user1);
        UserPrincipal principal2 = new UserPrincipal(user2);

        assertNotEquals(principal1.getUsername(), principal2.getUsername());
        assertNotEquals(principal1.getPassword(), principal2.getPassword());
        assertEquals(principal1.getAuthorities().size(), principal2.getAuthorities().size());
    }
}