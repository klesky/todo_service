package com.ooisy.todo.service;

import com.ooisy.todo.entity.Todo;
import com.ooisy.todo.exception.PastDueException;
import com.ooisy.todo.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

import static com.ooisy.todo.entity.Todo.*;

@Service
@Slf4j
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public Todo getTodo(Long id) {
        return retrieveTodoAndCheckNotFound(id);
    }

    public List<Todo> getAllTodo() {
        return todoRepository.findAll();
    }

    public List<Todo> getAllNotDoneTodo() {
        return todoRepository.findAllByStatus(STATUS_NOT_DONE);
    }

    public Todo createTodo(Todo todo) {
        if (!StringUtils.hasLength(todo.getDescription())) {
            throw new IllegalArgumentException("Description cannot be empty");
        }

        if (todo.getDueDate() == null) {
            throw new IllegalArgumentException("Due date cannot be empty");
        }

        if (todo.getDueDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Due date cannot be earlier than now");
        }

        if (!StringUtils.hasLength(todo.getDescription())) {
            throw new IllegalArgumentException("Description cannot be empty");
        }

        todo.setId(null);
        todo.setStatus(STATUS_NOT_DONE);
        todo.setCreationTime(LocalDateTime.now());
        todo.setUpdateDoneTime(null);
        return todoRepository.save(todo);
    }

    public Todo updateTodoDescription(long id, String description) {
        if (!StringUtils.hasLength(description)) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        Todo todo = retrieveTodoAndCheckNotFound(id);
        verifyUpdatable(todo);
        todo.setDescription(description);
        return todoRepository.save(todo);
    }

    public Todo updateDoneTodo(Long todoId) {
        Todo todo = retrieveTodoAndCheckNotFound(todoId);
        verifyUpdatable(todo);
        todo.setStatus(Todo.STATUS_DONE);
        todo.setUpdateDoneTime(LocalDateTime.now());
        return todoRepository.save(todo);
    }

    public Todo updateNotDoneTodo(Long todoId) {
        Todo todo = retrieveTodoAndCheckNotFound(todoId);
        verifyUpdatable(todo);
        todo.setStatus(STATUS_NOT_DONE);
        todo.setUpdateDoneTime(null);
        return todoRepository.save(todo);
    }

    public void updateExpiredTodo() {
        LocalDateTime now = LocalDateTime.now();
        todoRepository.findAllByDueDateLessThanEqualAndStatus(now, STATUS_NOT_DONE).forEach(todo -> {
            log.info("Updating todo with id:{} as past due", todo.getId());
            todo.setStatus(STATUS_PAST_DUE);
            todoRepository.save(todo);
        });
    }

    private Todo retrieveTodoAndCheckNotFound(Long todoId) {
        Todo todo = todoRepository.findById(todoId).orElse(null);
        if (todo == null) {
            throw new EntityNotFoundException(String.format("Todo with id %s not found", todoId));
        }
        return todo;
    }

    private void verifyUpdatable(Todo todo) {
        if (todo.getStatus().equals(STATUS_PAST_DUE)) {
            throw new PastDueException(String.format("Todo with id %s is past due", todo.getId()));
        }
    }

}
