package me.josephcosentino.repository;


import me.josephcosentino.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Joseph Cosentino.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM users u JOIN FETCH u.roles WHERE u.username = (:username)")
    User getUserByUsername(@Param("username") String username);

    List<User> findByGroupsId(@Param("group_id") Long group_id);

}
