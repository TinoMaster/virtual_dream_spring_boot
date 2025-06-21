package com.tinomaster.virtualdream.virtualdream.services.interfaces;

import com.tinomaster.virtualdream.virtualdream.dtos.TaskDto;
import com.tinomaster.virtualdream.virtualdream.dtos.response.PagedResponse;
import com.tinomaster.virtualdream.virtualdream.enums.ENoteStatus;

import java.util.List;
import java.util.Optional;

public interface TaskServiceInterface {

    TaskDto createTask(TaskDto task);

    TaskDto updateTask(TaskDto task);

    void deleteTask(Long id);

    Optional<TaskDto> getTask(Long id);

    PagedResponse<TaskDto> getAllTasksByBusinessId(Long idBusinessId, Integer page, Integer size);

    PagedResponse<TaskDto> getAllTasksByBusinessIdAndByStatus(Long idBusinessId, ENoteStatus status, Integer page, Integer size);
}
