package com.javarush.springmvc.controller;

import com.javarush.springmvc.domain.Task;
import com.javarush.springmvc.domain.TaskDTO;
import com.javarush.springmvc.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/tasks")
@SessionAttributes(value = "taskList")
public class TaskController {
    @Autowired
    private TaskService taskService;

//    @GetMapping
//    public List<TaskInfo> getAllTasks(@RequestParam(required = false) Integer pageNumber,
//                                  @RequestParam(required = false) Integer pageSize) {
//        List<Task> allTasksOnPage = taskService.getAllTasksOnPage(pageNumber, pageSize);
//        return allTasksOnPage.stream().map(this::toTaskInfo).collect(Collectors.toList());
//    }
    @GetMapping("/")
    public List<TaskDTO> getAllTasks(@RequestParam(required = false) Integer pageNumber,
                                     @RequestParam(required = false) Integer pageSize, Model model) {
        List<Task> allTasksOnPage = taskService.getAllTasksOnPage(0, 10);
        model.addAttribute("taskList", allTasksOnPage);
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
    public TaskDTO createTask(@RequestBody TaskDTO taskDTO) {
        Task task = taskService.createTask(taskDTO.description, taskDTO.status);
        return toTaskInfo(task);
    }

    @PostMapping("/{ID}")
    public TaskDTO saveTask(@PathVariable("ID") Integer id, @RequestBody TaskDTO taskDTO) {
        Task task = taskService.saveOrUpdateTask(id, taskDTO.description, taskDTO.status);
        return toTaskInfo(task);
    }

    @DeleteMapping("/{ID}")
    public void deleteTask(@PathVariable("ID") Integer id) {
        taskService.deleteTask(id);
    }

    private TaskDTO toTaskInfo(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.id = task.getId();
        taskDTO.description = task.getDescription();
        taskDTO.status = task.getStatus();
        return taskDTO;
    }
}
