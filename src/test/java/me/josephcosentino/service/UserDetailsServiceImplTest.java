package me.josephcosentino.service;

import me.josephcosentino.dao.UserDao;
import me.josephcosentino.model.Role;
import me.josephcosentino.model.User;
import me.josephcosentino.service.impl.UserDetailsServiceImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Joseph Cosentino.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceImplTest {

    private static final long ID = 1L;
    private static final String USERNAME = "user";
    private static final String PASSWORD = "pwd";
    private static final Set<Role> ROLES;

    static {
        Role userRole = new Role();
        userRole.setValue("USER");

        Role actuatorRole = new Role();
        actuatorRole.setValue("ACTUATOR");

        ROLES = new HashSet<>();
        ROLES.add(userRole);
        ROLES.add(actuatorRole);
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    public void failOnNullUser() {
        when(userDao.getUserByUsername(USERNAME)).thenReturn(null);
        exception.expect(UsernameNotFoundException.class);
        userDetailsService.loadUserByUsername(USERNAME);
    }

    @Test
    public void validUser() {
        User user = new User();
        user.setId(ID);
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setRoles(ROLES);

        when(userDao.getUserByUsername(USERNAME)).thenReturn(user);
        UserDetails details = userDetailsService.loadUserByUsername(USERNAME);

        assertEquals(details.getUsername(), user.getUsername());
        assertEquals(details.getPassword(), user.getPassword());
        assertTrue(details.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())
                .containsAll(user.getRoles().stream().map(Role::getValue).collect(Collectors.toList())));
    }


}
