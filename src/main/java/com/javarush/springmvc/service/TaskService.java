package com.javarush.springmvc.service;

import com.javarush.springmvc.dao.TaskDAO;
import com.javarush.springmvc.domain.Status;
import com.javarush.springmvc.domain.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskService {
    @Autowired
    private TaskDAO taskDAO;

    public List<Task> getAllTasks() {
        return taskDAO.getAllTasks();
    }

    public long getTasksCount() {
        return taskDAO.getTasksCount();
    }

    public List<Task> getAllTasksOnPage(int pageNumber, int pageSize) {
        return taskDAO.getAllTasksOnPage(pageNumber, pageSize);
    }

    //дописать метод по сазданию задачи в базу данных
    public Task createTask(String description, Status status) {
        Task task = new Task();
        task.setDescription(description);
        task.setStatus(status);
        return taskDAO.saveOrUpdateTask(task);
    }
    public Task getTask(Integer id) {
        return taskDAO.getTask(id);
    }

    public Task saveOrUpdateTask(Integer id, String description, Status status) {
        Task task = taskDAO.getTask(id);
        task.setDescription(description);
        task.setStatus(status);

        return taskDAO.saveOrUpdateTask(task);
    }

    public void deleteTask(Integer id) {
        taskDAO.deleteTask(id);
    }
}
