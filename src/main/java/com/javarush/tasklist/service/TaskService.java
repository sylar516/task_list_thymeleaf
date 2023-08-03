package com.javarush.tasklist.service;

import com.javarush.tasklist.dao.TaskDAO;
import com.javarush.tasklist.domain.Status;
import com.javarush.tasklist.domain.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class TaskService {
    private final TaskDAO taskDAO;

    public TaskService(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public List<Task> getAllTasks(int page, int limit) {
        return taskDAO.getAllTasks(page, limit);
    }

    public int getTasksCount() {
        return taskDAO.getTasksCount();
    }

    public Task createTask(String description, Status status) {
        Task task = new Task();
        task.setDescription(description);
        task.setStatus(status);

        taskDAO.saveOrUpdateTask(task);
        return task;
    }

    @Transactional
    public Task editTask(int id, String description, Status status) {
        Task task = taskDAO.getTask(id);
        if (isNull(task)) {
            throw new RuntimeException("task with id = " + id + " not found");
        }

        task.setDescription(description);
        task.setStatus(status);

        taskDAO.saveOrUpdateTask(task);
        return task;
    }

    @Transactional
    public void deleteTask(int id) {
        Task task = taskDAO.getTask(id);
        if (isNull(task)) {
            throw new RuntimeException("task with id = " + id + " not found");
        }

        taskDAO.deleteTask(task);
    }
}
