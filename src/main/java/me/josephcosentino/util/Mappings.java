package me.josephcosentino.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Joseph Cosentino.
 */
public final class Mappings {

    private Mappings() {

    }

    public static UserDetails detailsFromUser(@NotNull me.josephcosentino.model.User user) {
        Collection<? extends GrantedAuthority> grantedAuthorities = authoritiesFromRoles(user.getRoles());
        return new User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }

    public static Collection<? extends GrantedAuthority> authoritiesFromRoles(@NotNull List<String> roles) {
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }
}
