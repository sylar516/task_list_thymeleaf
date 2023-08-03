package com.javarush.tasklist.dao;

import com.javarush.tasklist.domain.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class TaskDAO {
    private final SessionFactory sessionFactory;

    public TaskDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Task getTask(int id) {
        return getSession().find(Task.class,id);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<Task> getAllTasks(int page, int limit) {
        Query<Task> query = getSession().createQuery("from Task", Task.class);
        query.setFirstResult((page - 1) * limit);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public int getTasksCount() {
        Query<Long> query = getSession().createQuery("select count(t) from Task t", Long.class);
        return Math.toIntExact(query.getSingleResult());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveOrUpdateTask(Task task) {
        getSession().merge(task);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteTask(Task task) {
        getSession().remove(task);
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
