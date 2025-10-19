package com.mytasks.tasks.controllers;

import com.mytasks.tasks.dto.TaskDto;
import com.mytasks.tasks.models.Task;
import com.mytasks.tasks.services.TaskServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskRestController {

    public TaskServices taskServices;

    public TaskRestController(TaskServices taskServices) {
        this.taskServices = taskServices;
    }

    @GetMapping
    public String launch(){
        return "Task Management System is accessible successfully!";
    }

    @GetMapping("/all")
    public List<Task> findAllTasks(){
        return taskServices.findAllTasks();
    }

    @GetMapping("/{id}")
    public Task findTaskById(@PathVariable int id){
        return taskServices.findTaskById(id);
    }

    @PostMapping
    public ResponseEntity<Task> createNewTask(@RequestBody TaskDto taskDto){
        return taskServices.createTask(taskDto);
    }
}
