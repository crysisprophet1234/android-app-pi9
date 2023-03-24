package com.example.requirements_manager.repositories;

import com.example.requirements_manager.entities.Project;

import java.util.List;

public interface ProjectRepository {

    List<Project> findAll();

    Project findById(Integer id);

    Project save(Project project);

    Project update(Project project);

    void delete(Long id);

}
