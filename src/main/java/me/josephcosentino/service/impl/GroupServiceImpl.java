package me.josephcosentino.service.impl;

import me.josephcosentino.auth.Authorizer;
import me.josephcosentino.exception.NotFoundException;
import me.josephcosentino.model.Group;
import me.josephcosentino.model.User;
import me.josephcosentino.repository.GroupRepository;
import me.josephcosentino.repository.UserRepository;
import me.josephcosentino.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Joseph Cosentino.
 */
@Service
@CacheConfig(cacheNames = "group")
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Authorizer authorizer;

    @Override
    @Transactional(readOnly = true)
    @Cacheable
    public List<Group> findAll(Pageable pageable) {
        authorizer.authorizeAdmin();
        return groupRepository.findAll(pageable).getContent();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable
    public Group findById(Long group_id) {
        List<User> users = userRepository.findByGroupsId(group_id);
        authorizer.authorizeAdminOrLoggedInUser(users);

        Group group = groupRepository.findOne(group_id);
        if (group == null) {
            throw new NotFoundException(String.format("Group %d not found.", group_id));
        }
        return group;
    }

    @Override
    @CacheEvict(allEntries = true)
    public void clearCache() {

    }

    @Override
    @CacheEvict
    public void clearCacheById(Long group_id) {

    }
}
