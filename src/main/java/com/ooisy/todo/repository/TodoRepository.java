package com.ooisy.todo.repository;

import com.ooisy.todo.entity.Todo;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TodoRepository extends CrudRepository<Todo, Long> {

    List<Todo> findAllByStatus(String status);

    List<Todo> findAll();

    List<Todo> findAllByDueDateLessThanEqualAndStatus(LocalDateTime localDateTime, String status);

}
