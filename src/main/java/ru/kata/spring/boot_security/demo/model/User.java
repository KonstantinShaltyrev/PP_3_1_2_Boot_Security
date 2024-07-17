package ru.kata.spring.boot_security.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", columnDefinition = "bigint")
    private long id;

    @Column(name = "username", columnDefinition = "varchar(32)", nullable = false)
    @NotEmpty(message = "Username shouldn't be empty")
    @Size(min = 3, max = 32, message = "Username should be between 3 and 32 chars")
    private String username;

    @Column(name = "password", columnDefinition = "varchar(100)", nullable = false)
    @NotEmpty(message = "Password shouldn't be empty")
    @Size(min = 3, max = 100, message = "Password should be between 3 and 100 chars")
    private String password;

    @Column(name = "firstname", columnDefinition = "varchar(32)", nullable = false)
    @NotEmpty(message = "Firstname shouldn't be empty")
    @Size(min = 2, max = 30, message = "Firstname should be between 2 and 30 chars")
    private String firstName;

    @Column(name = "lastname", columnDefinition = "varchar(32)", nullable = false)
    @NotEmpty(message = "Lastname shouldn't be empty")
    @Size(min = 2, max = 30, message = "Lastname should be between 2 and 30 chars")
    private String lastName;

    @Column(name = "birthday", columnDefinition = "date")
    @PastOrPresent(message = "Birthday should be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Column(name = "email", columnDefinition = "varchar(32)", nullable = false)
    @Email(message = "Email should be valid")
    @NotEmpty(message = "Name shouldn't be empty")
    private String email;

    @Column(name = "phone_number", columnDefinition = "varchar(32)")
    @NotEmpty(message = "Phone number shouldn't be empty")
    @Size(min = 2, max = 32, message = "Phone number should be between 5 and 32 chars")
    @Pattern(regexp = "^(\\+)?(\\d{1,4})?[\\s-]?\\(?\\d{1,4}\\)?[\\s.-]?\\d{1,4}[\\s.-]?\\d{1,4}[\\s.-]?\\d{1,4}$",
            message = "Incorrect phone format")
    private String phoneNumber;

    @Column(name = "role", nullable = true)
    @ManyToMany (fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE})
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id"))
    private Set<Role> roles = new HashSet<>();


    public User() {
    }

    public User(String username, String password, String firstName, String lastName, LocalDate birthday, String email, String phoneNumber, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.roles = roles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthday=" + birthday +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", roles=" + roles +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(birthday, user.birthday) &&
                Objects.equals(email, user.email) &&
                Objects.equals(phoneNumber, user.phoneNumber) &&
                Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, firstName, lastName, birthday, email, phoneNumber, roles);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
//        return UserDetails.super.isAccountNonExpired();
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
//        return UserDetails.super.isAccountNonLocked();
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
//        return UserDetails.super.isCredentialsNonExpired();
        return true;
    }

    @Override
    public boolean isEnabled() {
//        return UserDetails.super.isEnabled();
        return true;
    }

    public User getUser() {
        return this;
    }
}
