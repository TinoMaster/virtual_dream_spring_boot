package com.tinomaster.virtualdream.virtualdream.services;

import com.tinomaster.virtualdream.virtualdream.dtos.TaskDto;
import com.tinomaster.virtualdream.virtualdream.dtos.response.PagedResponse;
import com.tinomaster.virtualdream.virtualdream.entities.Task;
import com.tinomaster.virtualdream.virtualdream.enums.ENoteStatus;
import com.tinomaster.virtualdream.virtualdream.repositories.TaskRepository;
import com.tinomaster.virtualdream.virtualdream.services.interfaces.TaskServiceInterface;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService implements TaskServiceInterface {

    private final TaskRepository taskRepository;
    private final ModelMapper mapper;


    private Task findOrThrow(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
    }

    @Override
    public TaskDto createTask(TaskDto task) {
        try {
            Task newTask = mapper.map(task, Task.class);
            return mapper.map(taskRepository.save(newTask), TaskDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la tarea", e);
        }
    }

    @Override
    public TaskDto updateTask(TaskDto task) {
        try {
            findOrThrow(task.getId());
            Task newTask = mapper.map(task, Task.class);
            return mapper.map(taskRepository.save(newTask), TaskDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la tarea", e);
        }
    }

    @Override
    public void deleteTask(Long id) {
        try {
            findOrThrow(id);
            taskRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la tarea con id: " + id, e);
        }
    }

    @Override
    public Optional<TaskDto> getTask(Long id) {
        try {
            Task task = findOrThrow(id);
            return Optional.of(mapper.map(task, TaskDto.class));
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener la tarea con id: " + id, e);
        }
    }

    @Override
    public PagedResponse<TaskDto> getAllTasksByBusinessId(Long idBusinessId, Integer page, Integer size) {
        try {
            Page<Task> tasks = taskRepository.findAllByBusinessId(idBusinessId, PageRequest.of(page, size));
            List<TaskDto> content = tasks.stream().map(task -> mapper.map(task, TaskDto.class)).toList();

            return PagedResponse.<TaskDto>builder()
                    .content(content)
                    .page(page)
                    .size(size)
                    .totalPages(tasks.getTotalPages())
                    .totalElements(tasks.getTotalElements())
                    .hasNext(tasks.hasNext())
                    .hasPrevious(tasks.hasPrevious())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las tareas de la empresa con id: " + idBusinessId, e);
        }
    }

    @Override
    public PagedResponse<TaskDto> getAllTasksByBusinessIdAndByStatus(Long idBusinessId, ENoteStatus status, Integer page, Integer size) {
        try {
            Page<Task> tasks = taskRepository.findAllByBusinessIdAndStatus(idBusinessId, status, PageRequest.of(page, size));
            List<TaskDto> content = tasks.stream().map(task -> mapper.map(task, TaskDto.class)).toList();
            return PagedResponse.<TaskDto>builder()
                    .content(content)
                    .page(page)
                    .size(size)
                    .totalPages(tasks.getTotalPages())
                    .totalElements(tasks.getTotalElements())
                    .hasNext(tasks.hasNext())
                    .hasPrevious(tasks.hasPrevious())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las tareas de la empresa con id: " + idBusinessId, e);
        }
    }


}
