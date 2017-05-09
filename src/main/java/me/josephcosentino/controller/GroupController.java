package me.josephcosentino.controller;

import me.josephcosentino.model.Group;
import me.josephcosentino.model.User;
import me.josephcosentino.service.GroupService;
import me.josephcosentino.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Joseph Cosentino.
 */
@RestController
@RequestMapping("/api/v1")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService usersService;

    @GetMapping("/groups")
    public List<Group> getGroups(@PageableDefault(size = 50, sort = "id") Pageable pageable) {
        return groupService.findAll(pageable);
    }

    @GetMapping("/groups/{group_id}")
    public Group getGroup(@PathVariable("group_id") Long group_id) {
        return groupService.findById(group_id);
    }

    @GetMapping("/groups/{group_id}/users")
    public List<User> getUsers(@PathVariable("group_id") Long group_id) {
        return usersService.findByGroupId(group_id);
    }

    @DeleteMapping("/groups/cache")
    public void purgeUsers() {
        groupService.clearCache();
    }

    @DeleteMapping("/groups/{group_id}/cache")
    public void purgeUser(@PathVariable("group_id") Long group_id) {
        groupService.clearCacheById(group_id);
    }
}
