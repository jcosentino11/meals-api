package me.josephcosentino.auth.impl;

import me.josephcosentino.auth.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author Joseph Cosentino.
 */
@Component
public class AuthenticationProviderImpl implements AuthenticationProvider {

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication();
    }

}
