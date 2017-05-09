package me.josephcosentino.auth.impl;

import me.josephcosentino.auth.AuthenticationProvider;
import me.josephcosentino.auth.Authorizer;
import me.josephcosentino.exception.UnauthorizedException;
import me.josephcosentino.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Joseph Cosentino.
 */
@Component
public class SpringSecurityAuthorizer implements Authorizer {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Override
    public void authorizeAdminOrLoggedInUser(Long user_id) {
        if (!(isAdmin() || isLoggedInUser(user_id))) {
            throw new UnauthorizedException();
        }
    }

    @Override
    public void authorizeAdminOrLoggedInUser(List<User> users) {
        if (isAdmin()) return;
        if (users.stream().map(User::getId).noneMatch(this::isLoggedInUser)) {
            throw new UnauthorizedException();
        }
    }

    @Override
    public void authorizeAdmin() {
        if (!isAdmin()) {
            throw new UnauthorizedException();
        }
    }

    private List<String> getAuthorities() {
        return authenticationProvider
                .getAuthentication()
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    private User getLoggedInUser() {
        return (User) authenticationProvider
                .getAuthentication()
                .getPrincipal();
    }

    private boolean isLoggedInUser(Long user_id) {
        return getLoggedInUser().getId().equals(user_id);
    }

    private boolean isAdmin() {
        return getAuthorities()
                .stream()
                .anyMatch(authority -> authority.equalsIgnoreCase("ROLE_ADMIN"));
    }
}
