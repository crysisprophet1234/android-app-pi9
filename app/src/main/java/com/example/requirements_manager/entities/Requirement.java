package com.example.requirements_manager.entities;

import java.time.Instant;
import java.util.Objects;

public class Requirement {

    private Integer id;
    private String title;
    private String desc;
    private Instant moment;
    private String importance;
    private String difficulty;
    private Integer hours;

    private Project project;

    public Requirement(){

    }

    public Requirement(Integer id, String title, String desc, String importance, String difficulty, Integer hours, Project project) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.moment = Instant.now();
        this.importance = importance;
        this.difficulty = difficulty;
        this.hours = hours;
        this.project = project;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Instant getMoment() {
        return moment;
    }

    public void setMoment(Instant moment) {
        this.moment = moment;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Requirement that = (Requirement) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public String toString() {
        return "Requirement{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", moment=" + moment +
                ", importance='" + importance + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", hours=" + hours +
                ", project.id=" + project.getId() +
                ", project.name=" + project.getName() +
                '}';
    }
}
