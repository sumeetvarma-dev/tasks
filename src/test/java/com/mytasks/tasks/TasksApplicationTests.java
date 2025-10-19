package com.mytasks.tasks;

import com.mytasks.tasks.controllers.TaskRestController;
import com.mytasks.tasks.dto.TaskDto;
import com.mytasks.tasks.models.Task;
import com.mytasks.tasks.services.TaskServices;
import com.mytasks.tasks.utility.TaskStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@SpringBootTest
// Loads only the components needed for MVC (Controllers, Filters, etc.)
@WebMvcTest(TaskRestController.class) // Specify the controller to test
class TasksApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private MockMvc mockMvc; // Used to perform simulated HTTP requests

	// Mock the Service layer dependency, as @WebMvcTest doesn't load it
	@MockitoBean
	private TaskServices taskService;

	// --- Test for POST API (Creating a Task) ---
	@Test
	void shouldCreateNewTask() throws Exception {
	// 1. ARRANGE: Define the input DTO and the output Task object
		TaskDto inputDto = new TaskDto();
		inputDto.setName("New Task");
		inputDto.setDescription("Desc");

		// Create the expected Task object that the service will "return"
		Task expectedTask = new Task(
				inputDto.getName(),
				inputDto.getDescription(),
				TaskStatus.TODO,
				LocalDateTime.now()
		);
		// Give it an ID to simulate persistence
		expectedTask.setId(1L);

		// Define the JSON payload matching the TaskDto structure
		String taskJson = "{\"name\":\"New Task\", \"description\":\"Desc\"}";

		// Mock the Service call: When service.createTask is called with *any* TaskDto,
		// return the expected Task.
		when(taskService.createTask(any(TaskDto.class)))
				.thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(expectedTask));
		// This line assumes your TaskService returns ResponseEntity<Task>

		// Act & Assert
		mockMvc.perform(post("/task") // The actual POST mapping path
						.contentType(String.valueOf(MediaType.APPLICATION_JSON))
						.content(taskJson))
				.andExpect(status().isCreated()) // Expect HTTP 201 Created
				.andExpect(jsonPath("$.name").value("New Task"));
	}

	// --- Test for GET API (Fetching All Tasks) ---
	@Test
	void shouldFetchAllTasks() throws Exception {
		// Arrange: Define the list of tasks the mocked service should return
		Task task1 = new Task("Task 1", "D1", TaskStatus.TODO, LocalDateTime.now());
		Task task2 = new Task("Task 2", "D2", TaskStatus.INPROGRESS, LocalDateTime.now());
		List<Task> allTasks = List.of(task1, task2);

		when(taskService.findAllTasks()).thenReturn(allTasks);

		// Act & Assert
		mockMvc.perform(get("/task/all")) // the actual GET mapping path
				.andExpect(status().isOk()) // Expect HTTP 200 OK
				.andExpect(content().contentType(String.valueOf(MediaType.APPLICATION_JSON)))
				.andExpect(jsonPath("$[0].name").value("Task 1"))
				.andExpect(jsonPath("$[1].status").value("INPROGRESS"));
	}

}
