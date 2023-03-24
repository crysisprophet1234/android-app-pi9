package com.example.requirements_manager.repositories;

import com.example.requirements_manager.entities.Requirement;
import com.example.requirements_manager.entities.User;

import java.util.List;

public interface UserRepository {

    List<User> findAll();

    User findById(Integer id);

    User save(User user);

    List<User> saveAll(List<User> user);

    User update(User user);

    void delete(Integer id);

}
