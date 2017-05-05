package me.josephcosentino.util;

import me.josephcosentino.model.User;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertTrue;

/**
 * @author Joseph Cosentino.
 */
public class MappingsTest {

    private static final long ID = 1L;
    private static final String USERNAME = "user";
    private static final String PASSWORD = "pwd";
    private static final List<String> ROLES;

    static {
        ROLES = new ArrayList<>();
        ROLES.add("USER");
        ROLES.add("ACTUATOR");
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
        List<String> roles = new ArrayList<>();
        roles.add("ROLE");

        User user = new User();
        user.setId(ID);
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setRoles(roles);

        UserDetails details = Mappings.detailsFromUser(user);

        assertTrue(details.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())
                .containsAll(user.getRoles()));
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
                .containsAll(user.getRoles()));
    }

}
