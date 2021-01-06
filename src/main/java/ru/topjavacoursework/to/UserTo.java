package ru.topjavacoursework.to;

import javax.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;


public class UserTo extends BaseTo implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @Length(min = 4)
    private String password;

    public UserTo() {
    }

    public UserTo(Integer id, String name, String email, String password) {
        super(id);
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
