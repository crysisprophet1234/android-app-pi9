package com.example.requirements_manager.dao.impl;

import com.example.requirements_manager.dao.DaoFactory;
import com.example.requirements_manager.dao.ProjectDAO;
import com.example.requirements_manager.db.DbConnection;
import com.example.requirements_manager.db.exceptions.DbException;
import com.example.requirements_manager.db.exceptions.ResourceNotFoundException;
import com.example.requirements_manager.entities.Project;
import com.example.requirements_manager.entities.User;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.List;

public class ProjectDaoImpl implements ProjectDAO {

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Connection conn;

    public ProjectDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Project project) {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            String sql = "INSERT INTO public.tb_projeto " +
                    "(nome, data_inicio, data_entrega, usuario_id) " +
                    "VALUES (?, ?, ?, ?, ?)";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, project.getName());
            stmt.setDate(2, new Date(project.getStartDate().toEpochDay()));
            stmt.setDate(3, new Date(project.getFinalDate().toEpochDay()));
            stmt.setInt(4, project.getUser().getId());

            int rowsAffected = stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();

            System.out.println("Project created, ID " + rs.getInt(1));

        } catch (NullPointerException ex) {

            throw new ResourceNotFoundException(project.getUser().getId());

        } catch (SQLException ex) {

            throw new DbException(ex);

        } finally {
            DbConnection.closeStatement(stmt);
        }

    }

    @Override
    public void update(Project project) {

        PreparedStatement stmt = null;

        try {

            String sql = "UPDATE tb_projeto " +
                         "SET nome = ?, " +
                         "SET data_inicio = ?, " +
                         "SET data_entrega = ? " +
                         "SET usuario_id = ? " +
                         "WHERE id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, project.getName());
            stmt.setDate(2, new Date(project.getStartDate().toEpochDay()));
            stmt.setDate(3, new Date(project.getFinalDate().toEpochDay()));
            stmt.setInt(4, project.getUser().getId());
            stmt.setInt(5, project.getId());

            stmt.executeUpdate();

            System.out.println("Project id " + project.getId() + " updated!");


        } catch (NullPointerException ex) {

            throw new ResourceNotFoundException(project.getId());

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

            String sql = "DELETE FROM tb_projeto WHERE id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            stmt.execute();

            System.out.println("Project id " + id + " deleted!");

        } catch (NullPointerException ex) {

            throw new ResourceNotFoundException(id);

        } catch (SQLException ex) {

            throw new DbException(ex);

        } finally {
            DbConnection.closeStatement(stmt);
        }

    }

    @Override
    public Project findById(Integer id) {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            String sql = "SELECT id, nome, data_inicio, data_entrega, usuario_id " +
                    "FROM tb_projeto " +
                    "WHERE id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            rs = stmt.executeQuery();

            Project project = new Project();
            project.setId(rs.getInt("id"));
            project.setName(rs.getString("nome"));
            project.setStartDate(LocalDate.parse(rs.getString("data_inicio"), dtf));
            project.setFinalDate(LocalDate.parse(rs.getString("data_entrega"), dtf));
            project.setUser(DaoFactory.createUserDao().findById(rs.getInt("usuario_id")));

            return project;

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
    public List<Project> findByUser(User user) {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            String sql = "SELECT p.id, p.nome, p.data_inicio, p.data_entrega, p.usuario_id " +
                    "FROM tb_projeto p LEFT JOIN tb_usuario u " +
                    "ON p.usuario_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, user.getId());

            rs = stmt.executeQuery();

            List<Project> projects = new ArrayList<>();

            Project project = new Project();

            while (rs.next()) {

                project.setId(rs.getInt("id"));
                project.setName(rs.getString("nome"));
                project.setStartDate(LocalDate.parse(rs.getString("data_inicio"), dtf));
                project.setFinalDate(LocalDate.parse(rs.getString("data_entrega"), dtf));
                project.setUser(DaoFactory.createUserDao().findById(rs.getInt("usuario_id")));

                projects.add(project);

            }

            return projects;

        } catch (SQLException ex) {

            throw new DbException(ex);

        } finally {

            DbConnection.closeStatement(stmt);
            DbConnection.closeResultSet(rs);

        }

    }

    @Override
    public List<Project> findAll() {

        Statement stmt = null;
        ResultSet rs = null;

        try {

            String sql = "SELECT id, nome, data_inicio, data_entrega, usuario_id FROM tb_projeto";

            stmt = conn.createStatement();

            rs = stmt.executeQuery(sql);

            List<Project> projects = new ArrayList<>();

            Project project = new Project();

            while (rs.next()) {

                project.setId(rs.getInt("id"));
                project.setName(rs.getString("nome"));
                project.setStartDate(LocalDate.parse(rs.getString("data_inicio"), dtf));
                project.setFinalDate(LocalDate.parse(rs.getString("data_entrega"), dtf));
                project.setUser(DaoFactory.createUserDao().findById(rs.getInt("usuario_id")));

                projects.add(project);

            }

            return projects;

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
