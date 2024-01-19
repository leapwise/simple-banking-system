package com.leapwise.bankingsystem.config.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.mail")
@FieldDefaults(level = PRIVATE)
public class EmailProperties {

    String from;
    String host;
    int port;
    String username;
    String password;
}
