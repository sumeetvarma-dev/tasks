package com.mytasks.tasks.services;

import com.mytasks.tasks.dto.TaskDto;
import com.mytasks.tasks.models.Task;
import com.mytasks.tasks.repositories.TaskRepository;
import com.mytasks.tasks.utility.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class TaskServices {

    @Autowired
    public ConfigurableApplicationContext context;

    @Autowired
    public TaskRepository taskRepository;

    public TaskServices(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public ResponseEntity<Task> createTask(TaskDto taskDto){
        Task task = context.getBean(Task.class);
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setStatus(TaskStatus.TODO);
        Task savedTask = taskRepository.save(task);
        return ResponseEntity.ok(savedTask);
    }

    public Task findTaskById(long id){
         Optional<Task> optionalTask = taskRepository.findById(id);
        if(optionalTask.isPresent()){
            return optionalTask.get();
        }
        return optionalTask.orElseThrow();
    }

    public String deleteTaskById(int id){
        return "Task id:"+id+" Deleted Successfully";
    }

    public String UpdateTask(int id, Task updateThisTaskData){
        return "Task id:"+id+" Updated Successfully";
    }

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }
}
