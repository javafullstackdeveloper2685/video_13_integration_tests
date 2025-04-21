package org.game.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name="sform")
public class SForm {

    @Id
    @Column(columnDefinition = "BINARY(16)", nullable = false)
    private UUID uuid;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "message", nullable = false)
    private String message;

    // Конструкторы
    public SForm() {
        uuid = UUID.randomUUID();
    }

    public SForm(String username, String email, String message) {
        uuid = UUID.randomUUID();
        this.username = username;
        this.email = email;
        this.message = message;
    }

    // Геттеры и сеттеры
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SForm{" +
                "uuid=" + uuid +
                ", name='" + username + '\'' +
                ", email='" + email + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
