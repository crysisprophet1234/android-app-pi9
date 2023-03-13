package com.example.requirements_manager.dao;

import com.example.requirements_manager.entities.Project;
import com.example.requirements_manager.entities.User;

import java.util.List;

public interface UserDAO {

    void insert(User user);

    void update(User user);

    void delete(Integer id);

    User findById(Integer id);

    List<User> findByProject(Project project);

    List<User> findAll();

}
