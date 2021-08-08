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

    @PutMapping("/{id}/status/done")
    public Todo updateDoneTodo(@PathVariable Long id) {
        return todoService.updateDoneTodo(id);
    }

    @PutMapping("/{id}/status/not-done")
    public Todo updateNotDoneTodo(@PathVariable Long id) {
        return todoService.updateNotDoneTodo(id);
    }

    @GetMapping
    public List<Todo> getAllNotDoneTodo(@RequestParam(required = false) boolean getAll) {
        if (getAll) {
            return todoService.getAllTodo();
        }
        return todoService.getAllNotDoneTodo();
    }

    @GetMapping("/{id}")
    public Todo getTodo(@PathVariable Long id) {
        return todoService.getTodo(id);
    }

}
