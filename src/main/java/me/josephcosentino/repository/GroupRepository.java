package me.josephcosentino.repository;

import me.josephcosentino.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Joseph Cosentino.
 */
public interface GroupRepository extends JpaRepository<Group, Long> {

}
