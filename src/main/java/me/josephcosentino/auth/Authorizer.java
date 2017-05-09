package me.josephcosentino.auth;

import me.josephcosentino.model.User;

import java.util.List;

/**
 * @author Joseph Cosentino.
 */
public interface Authorizer {

    void authorizeAdminOrLoggedInUser(Long user_id);

    void authorizeAdminOrLoggedInUser(List<User> users);

    void authorizeAdmin();

}
