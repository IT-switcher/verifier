package com.isadounikau.sqiverifier.web.rest;

import com.isadounikau.sqiverifier.config.springdoc.OpenApiAvailable;
import com.isadounikau.sqiverifier.repository.TaskRepository;
import com.isadounikau.sqiverifier.service.TaskService;
import com.isadounikau.sqiverifier.service.dto.task.TaskCreateRequestDTO;
import com.isadounikau.sqiverifier.service.dto.task.TaskDTO;
import com.isadounikau.sqiverifier.web.rest.errors.BadRequestAlertException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.isadounikau.sqiverifier.domain.Task}.
 */
@RestController
@RequestMapping("/api")
public class TaskResource {

    private final Logger log = LoggerFactory.getLogger(TaskResource.class);

    private static final String ENTITY_NAME = "task";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaskService taskService;

    private final TaskRepository taskRepository;

    public TaskResource(TaskService taskService, TaskRepository taskRepository) {
        this.taskService = taskService;
        this.taskRepository = taskRepository;
    }

    /**
     * {@code POST  /tasks} : Create a new task.
     *
     * @param task the task to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new task, or with
     * status {@code 400 (Bad Request)} if the task has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @OpenApiAvailable
    @Operation(summary = "Get Product Selection Billing FAQ data")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201", description = "Task has been created successfully", content = {
                @Content(schema = @Schema(implementation = TaskDTO.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal Service Error", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Client Error, can be fixed by changing request", content =
                {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "Unauthorized, update access token", content = {@Content(schema = @Schema())}),
        }
    )
    @PostMapping("/tasks")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskCreateRequestDTO task) throws URISyntaxException {
        log.debug("REST request to save Task : {}", task);
        TaskDTO saveTask = getTaskDTO(task);
        TaskDTO result = taskService.save(saveTask);
        return ResponseEntity
            .created(new URI("/api/tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
                result.getId().toString()))
            .body(result);
    }

    private TaskDTO getTaskDTO(TaskCreateRequestDTO task) {
        TaskDTO saveTask = new TaskDTO();
        saveTask.setText(task.getText());
        saveTask.setTitle(task.getTitle());
        saveTask.setAnswer(task.getAnswer());
        return saveTask;
    }

    /**
     * {@code PUT  /tasks/:id} : Updates an existing task.
     *
     * @param id   the id of the task to save.
     * @param task the task to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated task,
     * or with status {@code 400 (Bad Request)} if the task is not valid,
     * or with status {@code 500 (Internal Server Error)} if the task couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @OpenApiAvailable
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "Task has been updated successfully", content = {
                @Content(schema = @Schema(implementation = TaskDTO.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal Service Error", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Client Error, can be fixed by changing request", content =
                {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "Unauthorized, update access token", content = {@Content(schema = @Schema())}),
        }
    )
    @PutMapping("/tasks/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable(value = "id", required = false) final Long id,
                                              @RequestBody TaskDTO task) throws URISyntaxException {
        log.debug("REST request to update Task : {}, {}", id, task);
        if (task.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, task.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TaskDTO result = taskService.update(task);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, task.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tasks/:id} : Partial updates given fields of an existing task, field will ignore if it is null
     *
     * @param id   the id of the task to save.
     * @param task the task to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated task,
     * or with status {@code 400 (Bad Request)} if the task is not valid,
     * or with status {@code 404 (Not Found)} if the task is not found,
     * or with status {@code 500 (Internal Server Error)} if the task couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @OpenApiAvailable
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "Task update", content = {
                @Content(schema = @Schema(implementation = TaskDTO.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal Service Error", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Client Error, can be fixed by changing request", content =
                {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "Unauthorized, update access token", content = {@Content(schema = @Schema())}),
        }
    )
    @PatchMapping(value = "/tasks/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<TaskDTO> partialUpdateTask(@PathVariable(value = "id", required = false) final Long id,
                                                     @RequestBody TaskDTO task) throws URISyntaxException {
        log.debug("REST request to partial update Task partially : {}, {}", id, task);
        if (task.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, task.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TaskDTO> result = taskService.partialUpdate(task);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, task.getId().toString())
        );
    }

    /**
     * {@code GET  /tasks} : get all the tasks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tasks in body.
     */
    @OpenApiAvailable
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "Task information", content = {
                @Content(array = @ArraySchema(schema = @Schema(implementation = TaskDTO.class)))
            }),
            @ApiResponse(responseCode = "500", description = "Internal Service Error", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Client Error, can be fixed by changing request", content =
                {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "Unauthorized, update access token", content = {@Content(schema = @Schema())}),
        }
    )
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDTO>> getAllTasks(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Tasks");
        Page<TaskDTO> page = taskService.findAll(pageable);
        HttpHeaders headers =
            PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tasks/:id} : get the "id" task.
     *
     * @param id the id of the task to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the task, or with status {@code
     * 404 (Not Found)}.
     */
    @OpenApiAvailable
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "Task information", content = {
                @Content(schema = @Schema(implementation = TaskDTO.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal Service Error", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Client Error, can be fixed by changing request", content =
                {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "Unauthorized, update access token", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Task is not found", content = {@Content(schema = @Schema())}),
        }
    )
    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {
        log.debug("REST request to get Task : {}", id);
        Optional<TaskDTO> task = taskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(task);
    }

    /**
     * {@code DELETE  /tasks/:id} : delete the "id" task.
     *
     * @param id the id of the task to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @OpenApiAvailable
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "204", description = "Task deleted", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Service Error", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Client Error, can be fixed by changing request", content =
                {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "Unauthorized, update access token", content = {@Content(schema = @Schema())}),
        }
    )
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        log.debug("REST request to delete Task : {}", id);
        taskService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
