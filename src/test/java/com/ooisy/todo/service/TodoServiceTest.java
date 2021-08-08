package com.ooisy.todo.service;

import com.ooisy.todo.entity.Todo;
import com.ooisy.todo.exception.PastDueException;
import com.ooisy.todo.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

import static com.ooisy.todo.entity.Todo.STATUS_DONE;
import static com.ooisy.todo.entity.Todo.STATUS_NOT_DONE;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigurationPackage
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb"
})
@Import(TodoService.class)
class TodoServiceTest {

    @Autowired
    private TodoService todoService;

    @Autowired
    private TodoRepository todoRepository;

    @Test
    void getTodo() {
        Todo createdTodo = todoService.createTodo(Todo.builder().description("test 1").dueDate(LocalDateTime.now().plusDays(1)).build());
        Todo getTodo = todoService.getTodo(createdTodo.getId());
        assertTrue(createdTodo.equals(getTodo));
    }

    @Test
    void getTodo_notFound() {
        Exception exception = assertThrows(EntityNotFoundException.class, () -> todoService.getTodo(2L));
        assertTrue(exception.getMessage().contains("Todo with id 2 not found"));
    }

    @Test
    void getAllTodo() {
        todoService.createTodo(Todo.builder().description("test 1").dueDate(LocalDateTime.now().plusDays(1)).build());
        todoService.createTodo(Todo.builder().description("test 2").dueDate(LocalDateTime.now().plusDays(2)).build());
        todoService.createTodo(Todo.builder().description("test 3").dueDate(LocalDateTime.now().plusDays(3)).build());
        List<Todo> todos = todoService.getAllTodo();
        assertEquals(3, todos.size());
    }

    @Test
    void getAllNotDoneTodo() {
        todoService.createTodo(Todo.builder().description("test 1").dueDate(LocalDateTime.now().plusDays(1)).build());
        todoService.createTodo(Todo.builder().description("test 2").dueDate(LocalDateTime.now().plusDays(2)).build());
        Long todoId3 = todoService.createTodo(Todo.builder().description("test 3").dueDate(LocalDateTime.now().plusDays(3)).build()).getId();
        todoService.updateDoneTodo(todoId3);
        List<Todo> todos = todoService.getAllNotDoneTodo();
        assertEquals(2, todos.size());
    }

    @Test
    void createTodo() {
        Todo todo = Todo.builder().description("test 1").dueDate(LocalDateTime.now().plusDays(1)).build();
        Todo createdTodo = todoService.createTodo(todo);
        assertNotNull(createdTodo.getId());
    }

    @Test
    void updateTodoDescription() {
        Todo createdTodo = todoService.createTodo(Todo.builder().description("test 1").dueDate(LocalDateTime.now().plusDays(1)).build());
        Todo updatedTodo = todoService.updateTodoDescription(createdTodo.getId(), "test update description");
        assertEquals("test update description", updatedTodo.getDescription());
    }

    @Test
    void updateTodoDescription_pastDue() {
        long todoId1 = todoRepository.save(Todo.builder().description("test 1").status(STATUS_NOT_DONE).dueDate(LocalDateTime.now().minusDays(1)).build()).getId();
        todoService.updateExpiredTodo();
        Exception exception = assertThrows(PastDueException.class, () -> todoService.updateTodoDescription(todoId1, "update description"));
        assertTrue(exception.getMessage().contains(String.format("Todo with id %s is past due", todoId1)));
    }

    @Test
    void updateDoneTodo() {
        Todo createdTodo = todoService.createTodo(Todo.builder().description("test 1").dueDate(LocalDateTime.now().plusDays(1)).build());
        Todo updatedTodo = todoService.updateDoneTodo(createdTodo.getId());
        assertEquals(STATUS_DONE, updatedTodo.getStatus());
        assertNotNull(updatedTodo.getUpdateDoneTime());
    }

    @Test
    void updateDoneTodo_pastDue() {
        long todoId1 = todoRepository.save(Todo.builder().description("test 1").status(STATUS_NOT_DONE).dueDate(LocalDateTime.now().minusDays(1)).build()).getId();
        todoService.updateExpiredTodo();
        Exception exception = assertThrows(PastDueException.class, () -> todoService.updateDoneTodo(todoId1));
        assertTrue(exception.getMessage().contains(String.format("Todo with id %s is past due", todoId1)));
    }

    @Test
    void updateNotDoneTodo() {
        Todo createdTodo = todoService.createTodo(Todo.builder().description("test 1").dueDate(LocalDateTime.now().plusDays(1)).build());
        todoService.updateDoneTodo(createdTodo.getId());
        Todo updatedTodo = todoService.updateNotDoneTodo(createdTodo.getId());
        assertEquals(STATUS_NOT_DONE, updatedTodo.getStatus());
        assertNull(updatedTodo.getUpdateDoneTime());
    }

    @Test
    void updateNotDoneTodo_pastDue() {
        long todoId1 = todoRepository.save(Todo.builder().description("test 1").status(STATUS_NOT_DONE).dueDate(LocalDateTime.now().minusDays(1)).build()).getId();
        todoService.updateExpiredTodo();
        Exception exception = assertThrows(PastDueException.class, () -> todoService.updateNotDoneTodo(todoId1));
        assertTrue(exception.getMessage().contains(String.format("Todo with id %s is past due", todoId1)));
    }

    @Test
    void updateExpiredTodo() {
        long todoId1 = todoRepository.save(Todo.builder().description("test 1").status(STATUS_NOT_DONE).dueDate(LocalDateTime.now().minusDays(1)).build()).getId();
        long todoId2 = todoRepository.save(Todo.builder().description("test 2").status(STATUS_NOT_DONE).dueDate(LocalDateTime.now().minusDays(1)).build()).getId();
        long todoId3 = todoRepository.save(Todo.builder().description("test 3").status(STATUS_NOT_DONE).dueDate(LocalDateTime.now().minusDays(1)).build()).getId();
        long todoId4 = todoRepository.save(Todo.builder().description("test 4").status(STATUS_NOT_DONE).dueDate(LocalDateTime.now().plusDays(1)).build()).getId();
        long todoId5 = todoRepository.save(Todo.builder().description("test 5").status(STATUS_NOT_DONE).dueDate(LocalDateTime.now().plusDays(1)).build()).getId();

        todoService.updateDoneTodo(todoId5);
        todoService.updateExpiredTodo();

        assertEquals(Todo.STATUS_PAST_DUE, todoService.getTodo(todoId1).getStatus());
        assertEquals(Todo.STATUS_PAST_DUE, todoService.getTodo(todoId2).getStatus());
        assertEquals(Todo.STATUS_PAST_DUE, todoService.getTodo(todoId3).getStatus());
        assertEquals(STATUS_NOT_DONE, todoService.getTodo(todoId4).getStatus());
        assertEquals(STATUS_DONE, todoService.getTodo(todoId5).getStatus());
    }
}