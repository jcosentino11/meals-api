package me.josephcosentino.service;

import me.josephcosentino.model.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Joseph Cosentino.
 */
public interface UserService {

    List<User> findAll(Pageable pageable);

    User findById(Long user_id);

    List<User> findByGroupId(Long group_id);

    void clearCache();

    void clearCacheById(Long user_id);

}
