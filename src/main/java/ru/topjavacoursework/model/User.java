package ru.topjavacoursework.model;

import org.hibernate.annotations.BatchSize;
import javax.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.Set;


@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email", name = "users_unique_email_idx")})
public class User extends NamedEntity {

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotBlank
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank
    @Length(min = 4)
    private String password;

    @Column(name = "registered", columnDefinition = "timestamp default now()")
    private Date registered = new Date();

    @Column(name = "enabled", nullable = false, columnDefinition = "bool default true")
    private boolean enabled = true;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    @BatchSize(size = 200)
    private Set<Role> roles;

    public User() {
    }

    public User(User u) {
        this(u.getId(), u.getName(), u.getEmail(), u.getPassword(), u.isEnabled(), u.getRoles());
    }

    public User(Integer id, String name, String email, String password, Role role, Role... roles) {
        this(id, name, email, password, true, EnumSet.of(role, roles));
    }

    public User(Integer id, String name, String email, String password, boolean enabled, Set<Role> roles) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegistered() {
        return registered;
    }

    public void setRegistered(Date registered) {
        this.registered = registered;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = CollectionUtils.isEmpty(roles) ? Collections.emptySet() : EnumSet.copyOf(roles);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", email=" + email +
                ", name=" + name +
                ", enabled=" + enabled +
                ", roles=" + roles +
                '}';
    }

}
