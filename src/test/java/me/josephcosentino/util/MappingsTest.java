package me.josephcosentino.util;

import me.josephcosentino.model.Role;
import me.josephcosentino.model.User;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertTrue;

/**
 * @author Joseph Cosentino.
 */
public class MappingsTest {

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

    @Test
    public void detailsFromNullUser() {
        exception.expect(RuntimeException.class);
        Mappings.detailsFromUser(null);
    }

    @Test
    public void detailsFromUserWithAllNullFields() {
        exception.expect(RuntimeException.class);
        User user = new User();
        Mappings.detailsFromUser(user);
    }

    @Test
    public void detailsFromUserWithNullUsername() {
        exception.expect(RuntimeException.class);
        User user = new User();
        user.setId(ID);
        user.setPassword(PASSWORD);
        user.setRoles(ROLES);
        Mappings.detailsFromUser(user);
    }

    @Test
    public void detailsFromUserWithNullPassword() {
        exception.expect(RuntimeException.class);
        User user = new User();
        user.setId(ID);
        user.setUsername(USERNAME);
        user.setRoles(ROLES);
        Mappings.detailsFromUser(user);
    }

    @Test
    public void authoritiesFromNullRoles() {
        exception.expect(RuntimeException.class);
        Mappings.detailsFromUser(null);
    }

    @Test
    public void authoritiesFromSingleRole() {
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setValue("ROLE");
        roles.add(role);

        User user = new User();
        user.setId(ID);
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setRoles(roles);

        UserDetails details = Mappings.detailsFromUser(user);

        assertTrue(details.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())
                .containsAll(user.getRoles().stream().map(Role::getValue).collect(Collectors.toList())));
    }

    @Test
    public void authoritiesFromMultipleRole() {
        User user = new User();
        user.setId(ID);
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setRoles(ROLES);

        UserDetails details = Mappings.detailsFromUser(user);

        assertTrue(details.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())
                .containsAll(user.getRoles().stream().map(Role::getValue).collect(Collectors.toList())));
    }

}
