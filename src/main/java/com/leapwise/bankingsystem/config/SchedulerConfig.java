package com.leapwise.bankingsystem.config;

import com.leapwise.bankingsystem.service.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import static lombok.AccessLevel.PRIVATE;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class SchedulerConfig {

    AccountService service;

    @Scheduled(cron = "0 0 0 1 * ?")
    public void calculatePastMonthTurnover() {
        service.calculatePastMonthTurnover();
    }
}
