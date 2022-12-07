package entities;

import annotations.Column;
import annotations.Entity;
import annotations.Id;

import java.time.LocalDate;
import java.util.Arrays;

@Entity(name = "users")
public class User {
    @Id
    @Column
    private long id;
@Column
    private String username;
@Column
    private int age;
@Column
    private LocalDate registration;

    public User(String username, int age) {
        this.username = username;
        this.age = age;
        this.registration = LocalDate.now();
    }

    public long getId() {
      return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDate getRegistration() {
        return registration;
    }

    public void setRegistration(LocalDate registration) {
        this.registration = registration;
    }
}
