package com.gym.fitcliff.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "customer")
public class CustomerConfig {

    private int daysBeforeExpiry;

    // Getter and Setter
    public int getDaysBeforeExpiry() {
        return daysBeforeExpiry;
    }

    public void setDaysBeforeExpiry(int daysBeforeExpiry) {
        this.daysBeforeExpiry = daysBeforeExpiry;
    }
}
