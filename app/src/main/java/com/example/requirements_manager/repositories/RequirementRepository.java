package com.example.requirements_manager.repositories;

import com.example.requirements_manager.entities.Project;
import com.example.requirements_manager.entities.Requirement;

import java.util.List;

public interface RequirementRepository {

    List<Requirement> findAll();

    List<Requirement> findByProjectId(Long id);

    Requirement findById(Long id);

    Requirement save(Requirement requirement);

    List<Requirement> saveAll(List<Requirement> requirements);

    Requirement update(Requirement requirement);

    void delete(Long id);

}
