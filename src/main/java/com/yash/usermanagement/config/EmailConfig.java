package com.yash.usermanagement.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@ConfigurationProperties("micronaut.email")
@Requires(property = "micronaut.email")
public class EmailConfig {
    @NotBlank
    @Email
    private String from;

    @NotBlank
    private String host;

    private int port;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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
} 