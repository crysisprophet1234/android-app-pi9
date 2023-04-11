package com.example.requirements_manager.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Project {

    private Integer id;
    private String name;
    private LocalDate startDate;
    private LocalDate finalDate;
    private String documentacaoUrl;
    private User user;

    private Set<Requirement> requirements = new HashSet<>();

    public Project(){

    }

    public Project(Integer id, String name, LocalDate startDate, LocalDate finalDate, User user, String documentacaoUrl) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.finalDate = finalDate;
        this.documentacaoUrl = documentacaoUrl;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(LocalDate finalDate) {
        this.finalDate = finalDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDocumentacaoUrl() {
        return documentacaoUrl;
    }

    public void setDocumentacaoUrl(String documentacaoUrl) {
        this.documentacaoUrl = documentacaoUrl;
    }

    public Set<Requirement> getRequirements() {
        return requirements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project projeto = (Project) o;
        return id.equals(projeto.id) && name.equals(projeto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", finalDate=" + finalDate +
                ", documentacaoUrl='" + documentacaoUrl + '\'' +
                ", user=" + user +
                ", requirements=" + requirements +
                '}';
    }
}
