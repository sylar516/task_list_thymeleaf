package com.javarush.springmvc.dao;

import com.javarush.springmvc.domain.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskDAO {
    @Autowired
    private SessionFactory sessionFactory;

//    Read
    public Task getTask(Integer id) {
        Session currentSession = sessionFactory.getCurrentSession();
        Task task = currentSession.get(Task.class, id);
        return task;
    }

    public List<Task> getAllTasks() {
        Session currentSession = sessionFactory.getCurrentSession();
        Query<Task> from_task = currentSession.createQuery("from Task", Task.class);
        List<Task> resultList = from_task.getResultList();
        return resultList;
    }

    public long getTasksCount() {
        Session currentSession = sessionFactory.getCurrentSession();
        Query<Long> query = currentSession.createQuery("select count(t) from Task t", Long.class);
        return query.getSingleResult();
    }

    public List<Task> getAllTasksOnPage(int pageNumber, int pageSize) {
        Session currentSession = sessionFactory.getCurrentSession();
        Query<Task> from_task = currentSession.createQuery("from Task", Task.class);
        from_task.setFirstResult(pageNumber * pageSize);
        from_task.setMaxResults(pageSize);
        List<Task> resultList = from_task.getResultList();
        return resultList;
    }

//    Create and Update
    public Task saveOrUpdateTask(Task task) {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.merge(task);
    }

//    Delete
    public void deleteTask(Integer id) {
        Session currentSession = sessionFactory.getCurrentSession();
        Task task = getTask(id);
        currentSession.remove(task);
    }
}
