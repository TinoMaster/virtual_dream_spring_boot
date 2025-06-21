package com.tinomaster.virtualdream.virtualdream.controllers;

import com.tinomaster.virtualdream.virtualdream.dtos.TaskDto;
import com.tinomaster.virtualdream.virtualdream.dtos.response.PagedResponse;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseBody;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseType;
import com.tinomaster.virtualdream.virtualdream.enums.ENoteStatus;
import com.tinomaster.virtualdream.virtualdream.services.TaskService;
import com.tinomaster.virtualdream.virtualdream.utils.Log;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/private/tasks")
    public ResponseEntity<ResponseBody<TaskDto>> createTask(@RequestBody TaskDto task) {
        try {
            return ResponseType.ok("successfullySaved", taskService.createTask(task));
        } catch (Exception e) {
            Log.info("Error al crear la tarea: " + e.getMessage());
            return ResponseType.badRequest("Error al crear la tarea", null);
        }
    }

    @PutMapping("/private/tasks")
    public ResponseEntity<ResponseBody<TaskDto>> updateTask(@RequestBody TaskDto task) {
        try {
            return ResponseType.ok("successfullyUpdated", taskService.updateTask(task));
        } catch (Exception e) {
            Log.info("Error al actualizar la tarea: " + e.getMessage());
            return ResponseType.badRequest("Error al actualizar la tarea", null);
        }
    }

    @DeleteMapping("/private/tasks/{id}")
    public ResponseEntity<ResponseBody<Void>> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseType.ok("successfullyDeleted", null);
        } catch (Exception e) {
            Log.info("Error al eliminar la tarea: " + e.getMessage());
            return ResponseType.badRequest("Error al eliminar la tarea", null);
        }
    }

    @GetMapping("/private/tasks/{id}")
    public ResponseEntity<ResponseBody<TaskDto>> getTask(@PathVariable Long id) {
        try {
            return ResponseType.ok("successfullyRetrieved", taskService.getTask(id).orElse(null));
        } catch (Exception e) {
            Log.info("Error al obtener la tarea: " + e.getMessage());
            return ResponseType.badRequest("Error al obtener la tarea", null);
        }
    }

    @GetMapping("/private/tasks/by_business")
    public ResponseEntity<ResponseBody<PagedResponse<TaskDto>>> getAllTasksByBusinessId(@RequestParam Long businessId, @RequestParam Integer page, @RequestParam Integer size) {
        try {
            return ResponseType.ok("successfullyRetrieved", taskService.getAllTasksByBusinessId(businessId, page, size));
        } catch (Exception e) {
            Log.info("Error al obtener las tareas de la empresa con id: " + businessId);
            return ResponseType.badRequest("Error al obtener las tareas de la empresa", null);
        }
    }

    @GetMapping("/private/tasks/by_business_and_status")
    public ResponseEntity<ResponseBody<PagedResponse<TaskDto>>> getAllTasksByBusinessIdAndByStatus(@RequestParam Long businessId, @RequestParam String status, @RequestParam Integer page, @RequestParam Integer size) {
        try {
            return ResponseType.ok("successfullyRetrieved", taskService.getAllTasksByBusinessIdAndByStatus(businessId, ENoteStatus.valueOf(status), page, size));
        } catch (Exception e) {
            Log.info("Error al obtener las tareas de la empresa con id: " + businessId);
            return ResponseType.badRequest("Error al obtener las tareas de la empresa", null);
        }
    }
}
