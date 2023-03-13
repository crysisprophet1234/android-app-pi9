package com.example.requirements_manager.dao;

import com.example.requirements_manager.entities.Requirement;
import com.example.requirements_manager.entities.User;

import java.util.List;

public interface RequirementDAO {

    void insert(Requirement requirement);

    void update(Requirement requirement);

    void delete(Integer id);

    Requirement findById(Integer id);

    List<Requirement> findByProject(User user);

    List<Requirement> findAll();

}
