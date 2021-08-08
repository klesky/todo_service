package com.ooisy.todo.controller;

import com.ooisy.todo.entity.Todo;
import com.ooisy.todo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @Operation(summary = "Create a to-do item")
    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        return todoService.createTodo(todo);
    }

    @Operation(summary = "Update a to-do item's description")
    @PutMapping("/{id}/description")
    public Todo updateTodoDescription(@PathVariable Long id, @RequestBody String description) {
        return todoService.updateTodoDescription(id, description);
    }

    @Operation(summary = "Update a to-do item as done")
    @PutMapping("/{id}/status/done")
    public Todo updateDoneTodo(@PathVariable Long id) {
        return todoService.updateDoneTodo(id);
    }

    @Operation(summary = "Update a to-do item as not done")
    @PutMapping("/{id}/status/not-done")
    public Todo updateNotDoneTodo(@PathVariable Long id) {
        return todoService.updateNotDoneTodo(id);
    }

    @Operation(summary = "Get all to-do items which are not done and with option to retrieve all")
    @GetMapping
    public List<Todo> getAllNotDoneTodo(@RequestParam(required = false) boolean getAll) {
        if (getAll) {
            return todoService.getAllTodo();
        }
        return todoService.getAllNotDoneTodo();
    }

    @Operation(summary = "Get a specific to-do item")
    @GetMapping("/{id}")
    public Todo getTodo(@PathVariable Long id) {
        return todoService.getTodo(id);
    }

}
