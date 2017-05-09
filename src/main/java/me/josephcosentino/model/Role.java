package me.josephcosentino.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Joseph Cosentino.
 */
@Entity(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String value;

    @JsonBackReference
    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "role_id",
                    referencedColumnName = "role_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "user_id"))
    private Set<User> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Set<User> getUsers() {
        if (users == null) {
            users = new HashSet<>();
        }
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", users=" + users +
                '}';
    }
}
