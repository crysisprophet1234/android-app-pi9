package com.example.requirements_manager.dao;

import com.example.requirements_manager.entities.Project;
import com.example.requirements_manager.entities.User;

import java.util.List;

public interface ProjectDAO {

    void insert(Project project);

    void update(Project project);

    void delete(Integer id);

    Project findById(Integer id);

    List<Project> findByUser(User user);

    List<Project> findAll();

}
