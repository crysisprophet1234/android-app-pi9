package com.example.requirements_manager.dao.impl;

import com.example.requirements_manager.dao.RequirementDAO;
import com.example.requirements_manager.entities.Requirement;
import com.example.requirements_manager.entities.User;

import java.sql.Connection;
import java.util.List;

public class RequirementDaoImpl implements RequirementDAO {

    private Connection conn;

    public RequirementDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Requirement requirement) {

    }

    @Override
    public void update(Requirement requirement) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public Requirement findById(Integer id) {
        return null;
    }

    @Override
    public List<Requirement> findByProject(User user) {
        return null;
    }

    @Override
    public List<Requirement> findAll() {
        return null;
    }
}
