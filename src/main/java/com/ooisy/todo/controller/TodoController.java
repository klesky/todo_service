package com.ooisy.todo.controller;

import com.ooisy.todo.entity.Todo;
import com.ooisy.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        return todoService.createTodo(todo);
    }

    @PutMapping("/{id}/description")
    public Todo updateTodoDescription(@PathVariable Long id, @RequestBody String description) {
        return todoService.updateTodoDescription(id, description);
    }

    @PutMapping("/{id}/_done")
    public Todo updateDoneTodo(@PathVariable Long id) {
        return todoService.updateDoneTodo(id);
    }

    @PutMapping("/{id}/_not_done")
    public Todo updateNotDoneTodo(@PathVariable Long id) {
        return todoService.updateNotDoneTodo(id);
    }

    @GetMapping("/not_done")
    public List<Todo> getAllNotDoneTodo() {
        return todoService.getAllNotDoneTodo();
    }

    @GetMapping
    public List<Todo> getAllTodo() {
        return todoService.getAllTodo();
    }

    @GetMapping("/{id}")
    public Todo getTodo(@PathVariable Long id) {
        return todoService.getTodo(id);
    }

}
