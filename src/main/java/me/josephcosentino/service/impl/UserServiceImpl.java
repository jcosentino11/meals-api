package me.josephcosentino.service.impl;

import me.josephcosentino.auth.Authorizer;
import me.josephcosentino.exception.NotFoundException;
import me.josephcosentino.model.Group;
import me.josephcosentino.model.User;
import me.josephcosentino.repository.GroupRepository;
import me.josephcosentino.repository.UserRepository;
import me.josephcosentino.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Joseph Cosentino.
 */
@Service
@CacheConfig(cacheNames = "user")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private Authorizer authorizer;

    @Override
    @Transactional(readOnly = true)
    @Cacheable
    public List<User> findAll(Pageable pageable) {
        authorizer.authorizeAdmin();
        return userRepository.findAll(pageable).getContent();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable
    public User findById(Long user_id) {
        authorizer.authorizeAdminOrLoggedInUser(user_id);
        User user =  userRepository.findOne(user_id);
        if (user == null) {
            throw new NotFoundException(String.format("User %d not found.", user_id));
        }
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable
    public List<User> findByGroupId(Long group_id) {
        List<User> users = userRepository.findByGroupsId(group_id);
        authorizer.authorizeAdminOrLoggedInUser(users);

        Group group = groupRepository.findOne(group_id);
        if (group == null) {
            throw new NotFoundException(String.format("Group %d not found.", group_id));
        }

        return new ArrayList<>(users);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void clearCache() {

    }

    @Override
    @CacheEvict
    public void clearCacheById(Long user_id) {

    }
}
