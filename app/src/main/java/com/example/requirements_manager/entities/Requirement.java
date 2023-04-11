package com.example.requirements_manager.entities;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Requirement {

    private Integer id;
    private String title;
    private String desc;
    private Instant moment;
    private String importance;
    private String difficulty;
    private Integer hours;
    private Double latitude;
    private Double longitude;
    private final Set<RequirementImage> images = new HashSet<>(2);
    private Project project;

    public Requirement(){

    }

    public Requirement(Integer id, String title, String desc, Instant moment, String importance, String difficulty, Integer hours, Double latitude, Double longitude, Project project) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.moment = moment;
        this.importance = importance;
        this.difficulty = difficulty;
        this.hours = hours;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Set<RequirementImage> getImages() {
        return images;
    }

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
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", images=" + images +
                '}';
    }
}
