package com.javarush.springmvc.controller;

import com.javarush.springmvc.domain.Task;
import com.javarush.springmvc.domain.TaskInfo;
import com.javarush.springmvc.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping()
    public List<TaskInfo> getAllTasks(@RequestParam(required = false) Integer pageNumber,
                                  @RequestParam(required = false) Integer pageSize) {
        List<Task> allTasksOnPage = taskService.getAllTasksOnPage(pageNumber, pageSize);
        return allTasksOnPage.stream().map(this::toTaskInfo).collect(Collectors.toList());
    }

    @GetMapping("/count")
    public long getTasksCount() {
        return taskService.getTasksCount();
    }

    @GetMapping("/{ID}")
    public Task getTask(@PathVariable("ID") Integer id) {
        return taskService.getTask(id);
    }

    //дописать метод по сазданию задачи в базу данных
    @PostMapping
    public TaskInfo createTask(@RequestBody TaskInfo taskInfo) {
        Task task = taskService.createTask(taskInfo.description, taskInfo.status);
        return toTaskInfo(task);
    }

    @PostMapping("/{ID}")
    public TaskInfo saveTask(@PathVariable("ID") Integer id, @RequestBody TaskInfo taskInfo) {
        Task task = taskService.saveOrUpdateTask(id, taskInfo.description, taskInfo.status);
        return toTaskInfo(task);
    }

    @DeleteMapping("/{ID}")
    public void deleteTask(@PathVariable("ID") Integer id) {
        taskService.deleteTask(id);
    }

    private TaskInfo toTaskInfo(Task task) {
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.id = task.getId();
        taskInfo.description = task.getDescription();
        taskInfo.status = task.getStatus();
        return taskInfo;
    }
}
