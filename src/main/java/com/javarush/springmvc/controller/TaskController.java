package com.javarush.springmvc.controller;

import com.javarush.springmvc.domain.Task;
import com.javarush.springmvc.domain.TaskDTO;
import com.javarush.springmvc.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Controller
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/")
    public String getAllTasks(@RequestParam(required = false) Integer pageNumber,
                              @RequestParam(required = false) Integer pageSize,
                              Model model) {
        pageNumber = isNull(pageNumber) ? 0 : pageNumber;
        pageSize = isNull(pageSize) ? 10 : pageSize;

        List<Task> allTasksOnPage = taskService.getAllTasksOnPage(pageNumber, pageSize);
        int countPages = (int) Math.ceil(1.0 * getCountPages()/pageSize);
        List<Integer> pageNumbers = generateNumbers(countPages);

        model.addAttribute("taskList", allTasksOnPage);
        model.addAttribute("pageNumbers", pageNumbers);

        return "task_list";
    }

    @PostMapping("/{ID}")
    public String saveOrUpdateTask(@PathVariable Integer ID, @RequestBody TaskDTO taskDTO, Model model) {
        taskService.saveOrUpdateTask(ID, taskDTO.description, taskDTO.status);
        return getAllTasks(0, 10, model);
    }

    @DeleteMapping("/{ID}")
    public String deleteTask(@PathVariable Integer ID, Model model) {
        taskService.deleteTask(ID);
        return getAllTasks(0, 10, model);
    }

    private long getCountPages() {
        return taskService.getTasksCount();
    }

    private List<Integer> generateNumbers(long count) {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            numbers.add(i);
        }
        return numbers;
    }
}
