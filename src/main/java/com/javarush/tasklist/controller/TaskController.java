package com.javarush.tasklist.controller;

import com.javarush.tasklist.domain.Task;
import com.javarush.tasklist.domain.TaskDTO;
import com.javarush.tasklist.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;

@Controller
@SessionAttributes({"currentPage", "lastPage", "tasksCount"})
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public String getAllTasks(Model model,
                                  @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                  @RequestParam(value = "limit", required = false, defaultValue = "5") int limit) {
        List<Task> tasks = taskService.getAllTasks(page, limit);
        int tasksCount = taskService.getTasksCount();
        int pageCount = (int) Math.ceil(1.0 * tasksCount/limit);
        List<Integer> pageButtons = IntStream.rangeClosed(1, pageCount).boxed().toList();

        model.addAttribute("currentPage", page);
        model.addAttribute("limit", limit);
        model.addAttribute("tasks", tasks);
        model.addAttribute("tasksCount", tasksCount);
        model.addAttribute("lastPage", pageCount);
        model.addAttribute("pageButtons", pageButtons);

        return "task_list";
    }

    @PostMapping("/")
    public String createTask(@ModelAttribute("lastPage") int lastPage,
                             @RequestBody TaskDTO taskDTO) {
        taskService.createTask(taskDTO.getDescription(), taskDTO.getStatus());
        return "task_list";
    }

    @PostMapping("/{id}")
    public String editTask(@PathVariable Integer id,
                           @RequestBody TaskDTO taskDTO) {
        if (isNull(id) || id <= 0) {
            throw new RuntimeException("invalid id");
        }

        taskService.editTask(id, taskDTO.getDescription(), taskDTO.getStatus());
        return "task_list";
    }

    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable Integer id) {
        if (isNull(id) || id <= 0) {
            throw new RuntimeException("invalid id");
        }

        taskService.deleteTask(id);
        return "task_list";
    }

}
