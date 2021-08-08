package com.ooisy.todo.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Table
@Entity
public class Todo {

    public final static String STATUS_DONE = "done";
    public final static String STATUS_NOT_DONE = "not done";
    public final static String STATUS_PAST_DUE = "past due";

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String description;
    private String status;
    private LocalDateTime dueDate;
    private LocalDateTime creationTime;
    private LocalDateTime updateDoneTime;
}
