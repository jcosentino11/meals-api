package me.josephcosentino.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Joseph Cosentino.
 */
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToMany(mappedBy = "users")
    private Set<Role> roles;

    @ManyToOne
    @JoinTable(name = "users_groups",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "group_id",
                    referencedColumnName = "group_id"))
    private Group group;

    @OneToMany(mappedBy = "user")
    private Set<PlannedMeal> plannedMeals;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        if (roles == null) {
            roles = new HashSet<>();
        }
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Set<PlannedMeal> getPlannedMeals() {
        return plannedMeals;
    }

    public void setPlannedMeals(Set<PlannedMeal> plannedMeals) {
        this.plannedMeals = plannedMeals;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", group=" + group +
                ", plannedMeals=" + plannedMeals +
                '}';
    }
}
