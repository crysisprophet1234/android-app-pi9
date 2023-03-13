package com.example.requirements_manager.dao.impl;

import com.example.requirements_manager.dao.DaoFactory;
import com.example.requirements_manager.dao.UserDAO;
import com.example.requirements_manager.db.DbConnection;
import com.example.requirements_manager.db.exceptions.DbException;
import com.example.requirements_manager.db.exceptions.DbIntegrityException;
import com.example.requirements_manager.db.exceptions.ResourceNotFoundException;
import com.example.requirements_manager.entities.Project;
import com.example.requirements_manager.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

public class UserDaoImpl implements UserDAO {

    private Connection conn;

    public UserDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(User user) {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            String sql = "INSERT INTO tb_usuario " +
                    "(nome, empresa, email, senha) " +
                    "VALUES (?, ?, ?, ?)";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getCompany());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());

            int rowsAffected = stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();

            System.out.println("User created, ID " + rs.getInt(1));

        } catch (SQLException ex) {

            throw new DbException(ex);

        } finally {
            DbConnection.closeStatement(stmt);
        }

    }

    @Override
    public void update(User user) {

        PreparedStatement stmt = null;

        try {

            String sql = "UPDATE tb_user " +
                    "SET nome = ?, " +
                    "SET empresa = ?, " +
                    "SET email = ? " +
                    "WHERE id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getCompany());
            stmt.setString(3, user.getEmail());
            stmt.setInt(4, user.getId());

            stmt.executeUpdate();

            System.out.println("User id " + user.getId() + " updated!");


        } catch (NullPointerException ex) {

            throw new ResourceNotFoundException(user.getId());

        } catch (SQLException ex) {

            throw new DbException(ex);

        } finally {
            DbConnection.closeStatement(stmt);
        }

    }

    @Override
    public void delete(Integer id) {

        PreparedStatement stmt = null;

        try {

            String sql = "DELETE FROM tb_usuario WHERE id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            stmt.execute();

            System.out.println("User id " + id + " deleted!");

        } catch (NullPointerException ex) {

            throw new ResourceNotFoundException(id);

        } catch (SQLException ex) {

            throw new DbException(ex);

        } finally {
            DbConnection.closeStatement(stmt);
        }

    }

    @Override
    public User findById(Integer id) {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            String sql = "SELECT id, nome, empresa, email, senha " +
                    "FROM tb_usuario " +
                    "WHERE id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            rs = stmt.executeQuery();

            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("nome"));
            user.setCompany(rs.getString("empresa"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            DaoFactory.createProjectDao().findByUser(user).forEach(x -> user.getProjects().add(x));

            return user;

        } catch (NullPointerException ex) {

            throw new ResourceNotFoundException(id);

        } catch (SQLException ex) {

            throw new DbException(ex);

        } finally {

            DbConnection.closeStatement(stmt);
            DbConnection.closeResultSet(rs);

        }

    }

    @Override
    public List<User> findByProject(Project project) {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            String sql = "SELECT u.id, u.nome, u.empresa, u.email, u.senha " +
                         "FROM tb_usuario u LEFT JOIN tb_projeto p " +
                         "ON u.id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, project.getUser().getId());

            rs = stmt.executeQuery();

            List<User> users = new ArrayList<>();

            User user = new User();

            while (rs.next()) {

                user.setId(rs.getInt("id"));
                user.setName(rs.getString("nome"));
                user.setCompany(rs.getString("empresa"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                DaoFactory.createProjectDao().findByUser(user).forEach(x -> user.getProjects().add(x));

                users.add(user);

            }

            return users;

        } catch (SQLException ex) {

            throw new DbException(ex);

        } finally {

            DbConnection.closeStatement(stmt);
            DbConnection.closeResultSet(rs);

        }

    }

    @Override
    public List<User> findAll() {

        Statement stmt = null;
        ResultSet rs = null;

        try {

            String sql = "SELECT id, nome, empresa, email, senha FROM tb_usuario";

            stmt = conn.createStatement();

            rs = stmt.executeQuery(sql);

            List<User> users = new ArrayList<>();

            User user = new User();

            while (rs.next()) {

                user.setId(rs.getInt("id"));
                user.setName(rs.getString("nome"));
                user.setCompany(rs.getString("empresa"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                DaoFactory.createProjectDao().findByUser(user).forEach(x -> user.getProjects().add(x));

                users.add(user);

            }

            return users;

        } catch (SQLException ex) {

            throw new DbException(ex);

        } catch (Exception ex) {

            throw new RuntimeException(ex.getMessage());

        } finally {

            DbConnection.closeStatement(stmt);
            DbConnection.closeResultSet(rs);

        }

    }

}
