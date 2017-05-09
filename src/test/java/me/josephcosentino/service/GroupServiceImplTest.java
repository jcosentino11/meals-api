package me.josephcosentino.service;

import me.josephcosentino.InvalidateCache;
import me.josephcosentino.exception.NotFoundException;
import me.josephcosentino.exception.UnauthorizedException;
import me.josephcosentino.model.Group;
import me.josephcosentino.model.Role;
import me.josephcosentino.model.User;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Joseph Cosentino.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan("me.josephcosentino")
@WithMockUser(username = "admin", roles = "ADMIN")
public class GroupServiceImplTest {

    @Configuration
    @EnableCaching
    static class CacheConfig {

        @Bean
        CacheManager cacheManager() {
            return new ConcurrentMapCacheManager("group");
        }

    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Rule
    @Autowired
    public InvalidateCache invalidateCache;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private GroupService groupService;

    @Test
    @Transactional
    @Sql("/load-group.sql")
    public void findGroupById() {
        Group group = groupService.findById(1L);
        assertEquals(group.getName(), "test-group");
    }

    @Test
    public void findNonExistentGroupById() {
        exception.expect(NotFoundException.class);
        groupService.findById(1L);
    }

    @Test
    @Transactional
    @Sql("/load-groups.sql")
    public void findByIdCache() {
        Group expectedGroup = new Group();
        expectedGroup.setId(1L);
        expectedGroup.setName("test-group");

        Group group = groupService.findById(1L);
        assertNotNull(group);
        assertEquals(group, expectedGroup);

        Group cachedGroup = (Group) cacheManager.getCache("group").get(1L).get();
        assertNotNull(cachedGroup);
        assertEquals(cachedGroup, expectedGroup);
    }

    @Test
    public void findNonExistantUsers() {
        List<Group> grouops = groupService.findAll(null);
        assertTrue(grouops.isEmpty());
    }

    @Test
    @Transactional
    @Sql("/load-group.sql")
    public void findGroup() {
        List<Group> groups = groupService.findAll(null);
        assertEquals(groups.size(), 1);
        assertEquals(groups.get(0).getName(), "test-group");
    }

    @Test
    @Transactional
    @Sql("/load-groups.sql")
    public void findGroups() {
        List<Group> groups = groupService.findAll(null);
        assertEquals(groups.size(), 2);
        assertEquals(groups.get(0).getName(), "test-group");
        assertEquals(groups.get(1).getName(), "test-group-2");
    }

    @Test
    @Transactional
    @Sql("/load-groups.sql")
    @SuppressWarnings("unchecked")
    public void findAllCache() {
        Group expectedGroup1 = new Group();
        expectedGroup1.setId(1L);
        expectedGroup1.setName("test-group");

        Group expectedGroup2 = new Group();
        expectedGroup2.setId(2L);
        expectedGroup2.setName("test-group-2");

        Pageable page = new PageRequest(0, Integer.MAX_VALUE);

        List<Group> groups = groupService.findAll(page);
        assertNotNull(groups);
        assertEquals(groups.size(), 2);
        assertEquals(groups.get(0), expectedGroup1);
        assertEquals(groups.get(1), expectedGroup2);

        List<Group> cachedGroups = (List<Group>) cacheManager.getCache("group").get(page).get();
        assertNotNull(cachedGroups);
        assertEquals(cachedGroups.size(), 2);
        assertEquals(cachedGroups.get(0), expectedGroup1);
        assertEquals(cachedGroups.get(1), expectedGroup2);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void adminAccessFindAll() {
        groupService.findAll(new PageRequest(0, Integer.MAX_VALUE));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void userAccessFindAll() {
        exception.expect(UnauthorizedException.class);
        groupService.findAll(new PageRequest(0, Integer.MAX_VALUE));
    }

    @Test
    @Transactional
    @Sql("/load-group.sql")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void adminAccessFindById() {
        groupService.findById(1L);
    }

    @Test
    @Transactional
    @Sql("/load-users-group.sql")
    public void validUserAccessFindById() {
        Role role = new Role();
        role.setValue("ROLE_USER");

        User user = new User();
        user.setUsername("test-user");
        user.setPassword("pwd");
        user.getRoles().add(role);
        user.setId(1L);

        setSpringSecurityUser(user);

        groupService.findById(1L);
    }

    @Test
    @Transactional
    @Sql("/load-users-group.sql")
    public void invalidUserAccessFindById() {
        exception.expect(UnauthorizedException.class);

        Role role = new Role();
        role.setValue("ROLE_USER");

        User user = new User();
        user.setUsername("test-user-3");
        user.setPassword("pwd");
        user.getRoles().add(role);
        user.setId(3L);

        setSpringSecurityUser(user);

        groupService.findById(1L);
    }

    private void setSpringSecurityUser(User user) {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }

    @Test
    @Transactional
    @Sql("/load-groups.sql")
    @SuppressWarnings("unchecked")
    public void clearCache() {
        Group group1 = new Group();
        group1.setId(1L);
        group1.setName("test-group");

        Group group2 = new Group();
        group2.setId(2L);
        group2.setName("test-group-2");

        groupService.findById(1L);
        groupService.findById(2L);

        Group cachedGroup1 = cacheManager.getCache("group").get(1L, Group.class);
        Group cachedGroup2 = cacheManager.getCache("group").get(2L, Group.class);

        assertNotNull(cachedGroup1);
        assertNotNull(cachedGroup2);

        groupService.clearCache();

        assertNull(cacheManager.getCache("group").get(1L));
        assertNull(cacheManager.getCache("group").get(2L));
    }

    @Test
    @Transactional
    @Sql("/load-groups.sql")
    @SuppressWarnings("unchecked")
    public void clearCacheById() {
        Group group1 = new Group();
        group1.setId(1L);
        group1.setName("test-group");

        Group group2 = new Group();
        group2.setId(2L);
        group2.setName("test-group-2");

        groupService.findById(1L);
        groupService.findById(2L);

        Group cachedGroup1 = cacheManager.getCache("group").get(1L, Group.class);
        Group cachedGroup2 = cacheManager.getCache("group").get(2L, Group.class);

        assertNotNull(cachedGroup1);
        assertNotNull(cachedGroup2);

        groupService.clearCacheById(1L);

        assertNull(cacheManager.getCache("group").get(1L));
        assertNotNull(cacheManager.getCache("group").get(2L));
    }

}
