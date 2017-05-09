package me.josephcosentino.dao;


import me.josephcosentino.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Joseph Cosentino.
 */
@Repository
public interface UserDao extends CrudRepository<User, String> {

    @Query("SELECT u FROM users u JOIN FETCH u.roles WHERE u.username = (:username)")
    User getUserByUsername(@Param("username") String username);

}
