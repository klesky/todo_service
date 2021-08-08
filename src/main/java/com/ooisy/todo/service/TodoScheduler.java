package com.ooisy.todo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TodoScheduler {

    @Autowired
    private TodoService todoService;

    private static volatile boolean isRunning = false;

    @Scheduled(cron = "0 * * * * *") // runs every minute
    public void runUpdateExpiredTodoJob() {
        log.info("Starting update expired todo job");
        if (isRunning) {
            log.error("Previous job is still running therefore job will not start");
            return;
        }
        isRunning = true;
        todoService.updateExpiredTodo();
        isRunning = false;
        log.info("Finished update expired todo job");
    }
}
