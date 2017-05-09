package me.josephcosentino.service;

import me.josephcosentino.model.Group;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Joseph Cosentino.
 */
public interface GroupService {

    List<Group> findAll(Pageable pageable);

    Group findById(Long group_id);

    void clearCache();

    void clearCacheById(Long group_id);

}
