package me.josephcosentino.auth;

import org.springframework.security.core.Authentication;

/**
 * @author Joseph Cosentino.
 */
public interface AuthenticationProvider {

    Authentication getAuthentication();

}
