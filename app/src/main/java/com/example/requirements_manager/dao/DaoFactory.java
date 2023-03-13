package com.example.requirements_manager.dao;

import com.example.requirements_manager.dao.impl.ProjectDaoImpl;
import com.example.requirements_manager.dao.impl.RequirementDaoImpl;
import com.example.requirements_manager.dao.impl.UserDaoImpl;
import com.example.requirements_manager.db.DbConnection;

public class DaoFactory {

    public static UserDAO createUserDao() {
        return new UserDaoImpl(DbConnection.getConnection());
    }

    public static ProjectDAO createProjectDao() {
        return new ProjectDaoImpl(DbConnection.getConnection());
    }

    public static RequirementDAO createRequirementDao() {
        return new RequirementDaoImpl(DbConnection.getConnection());
    }

}
