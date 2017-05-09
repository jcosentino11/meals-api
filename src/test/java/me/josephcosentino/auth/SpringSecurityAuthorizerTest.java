package me.josephcosentino.auth;

import me.josephcosentino.auth.impl.SpringSecurityAuthorizer;
import me.josephcosentino.exception.UnauthorizedException;
import me.josephcosentino.model.Role;
import me.josephcosentino.model.User;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * @author Joseph Cosentino.
 */
@RunWith(SpringRunner.class)
public class SpringSecurityAuthorizerTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private AuthenticationProvider authenticationProvider;

    @InjectMocks
    private SpringSecurityAuthorizer springSecurityAuthorizer;

    @Test
    public void authorizeAdminAsAdmin() {
        Role role = new Role();
        role.setId(1L);
        role.setValue("ROLE_ADMIN");

        User loggedInAdminUser = new User();
        loggedInAdminUser.setId(1L);
        loggedInAdminUser.setUsername("test-user");
        loggedInAdminUser.setPassword("pwd");
        loggedInAdminUser.getRoles().add(role);

        when(authenticationProvider.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(loggedInAdminUser, null, loggedInAdminUser.getAuthorities()));

        springSecurityAuthorizer.authorizeAdmin();
    }

    @Test
    public void authorizeNonAdminAsAdmin() {
        Role role = new Role();
        role.setId(1L);
        role.setValue("ROLE_USER");

        User loggedInUser = new User();
        loggedInUser.setId(1L);
        loggedInUser.setUsername("test-user");
        loggedInUser.setPassword("pwd");
        loggedInUser.getRoles().add(role);

        when(authenticationProvider.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(loggedInUser, null, loggedInUser.getAuthorities()));

        exception.expect(UnauthorizedException.class);
        springSecurityAuthorizer.authorizeAdmin();
    }

    @Test
    public void authorizeAdminAsAdminOrLoggedInUserById() {
        Role role = new Role();
        role.setId(1L);
        role.setValue("ROLE_ADMIN");

        User loggedInAdminUser = new User();
        loggedInAdminUser.setId(1L);
        loggedInAdminUser.setUsername("test-user");
        loggedInAdminUser.setPassword("pwd");
        loggedInAdminUser.getRoles().add(role);

        when(authenticationProvider.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(loggedInAdminUser, null, loggedInAdminUser.getAuthorities()));
        springSecurityAuthorizer.authorizeAdminOrLoggedInUser((Long) null);
    }

    @Test
    public void authorizeLoggedInUserAsAdminOrLoggedInUserById() {
        Role role = new Role();
        role.setId(1L);
        role.setValue("ROLE_USER");

        User loggedInUser = new User();
        loggedInUser.setId(1L);
        loggedInUser.setUsername("test-user");
        loggedInUser.setPassword("pwd");
        loggedInUser.getRoles().add(role);

        when(authenticationProvider.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(loggedInUser, null, loggedInUser.getAuthorities()));
        springSecurityAuthorizer.authorizeAdminOrLoggedInUser(1L);
    }

    @Test
    public void authorizeNonLoggedInUserAsAdminOrLoggedInUserById() {
        Role role = new Role();
        role.setId(1L);
        role.setValue("ROLE_USER");

        User loggedInUser = new User();
        loggedInUser.setId(1L);
        loggedInUser.setUsername("test-user");
        loggedInUser.setPassword("pwd");
        loggedInUser.getRoles().add(role);

        when(authenticationProvider.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(loggedInUser, null, loggedInUser.getAuthorities()));
        exception.expect(UnauthorizedException.class);
        springSecurityAuthorizer.authorizeAdminOrLoggedInUser(2L);
    }

    @Test
    public void authorizeAdminAsAdminOrLoggedInUser() {
        Role role = new Role();
        role.setId(1L);
        role.setValue("ROLE_ADMIN");

        User loggedInAdminUser = new User();
        loggedInAdminUser.setId(1L);
        loggedInAdminUser.setUsername("test-user");
        loggedInAdminUser.setPassword("pwd");
        loggedInAdminUser.getRoles().add(role);

        when(authenticationProvider.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(loggedInAdminUser, null, loggedInAdminUser.getAuthorities()));
        springSecurityAuthorizer.authorizeAdminOrLoggedInUser((List<User>) null);
    }

    @Test
    public void authorizeLoggedInUserAsAdminOrLoggedInUser() {
        Role role = new Role();
        role.setId(1L);
        role.setValue("ROLE_USER");

        User loggedInUser = new User();
        loggedInUser.setId(1L);
        loggedInUser.setUsername("test-user");
        loggedInUser.setPassword("pwd");
        loggedInUser.getRoles().add(role);

        User userToAuthorize = new User();
        userToAuthorize.setId(1L);
        userToAuthorize.setUsername("test-user");
        userToAuthorize.setPassword("pwd");
        userToAuthorize.getRoles().add(role);

        List<User> usersToAuthorize = new ArrayList<>();
        usersToAuthorize.add(userToAuthorize);

        when(authenticationProvider.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(loggedInUser, null, loggedInUser.getAuthorities()));
        springSecurityAuthorizer.authorizeAdminOrLoggedInUser(usersToAuthorize);
    }

    @Test
    public void authorizeNonLoggedInUserAsAdminOrLoggedInUser() {
        Role role = new Role();
        role.setId(1L);
        role.setValue("ROLE_USER");

        User loggedInUser = new User();
        loggedInUser.setId(1L);
        loggedInUser.setUsername("test-user");
        loggedInUser.setPassword("pwd");
        loggedInUser.getRoles().add(role);

        User userToAuthorize = new User();
        userToAuthorize.setId(2L);
        userToAuthorize.setUsername("test-user-2");
        userToAuthorize.setPassword("pwd");
        userToAuthorize.getRoles().add(role);

        List<User> usersToAuthorize = new ArrayList<>();
        usersToAuthorize.add(userToAuthorize);

        when(authenticationProvider.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(loggedInUser, null, loggedInUser.getAuthorities()));
        exception.expect(UnauthorizedException.class);
        springSecurityAuthorizer.authorizeAdminOrLoggedInUser(usersToAuthorize);
    }

}
