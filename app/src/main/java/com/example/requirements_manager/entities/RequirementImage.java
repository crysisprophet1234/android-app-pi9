package com.example.requirements_manager.entities;

import java.util.Objects;

public class RequirementImage {

    private Integer id;
    private String url;
    private Requirement requirement;

    public RequirementImage() {

    }

    public RequirementImage(Integer id, String url, Requirement requirement) {
        this.id = id;
        this.url = url;
        this.requirement = requirement;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Requirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequirementImage that = (RequirementImage) o;
        return Objects.equals(id, that.id) && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url);
    }

    @Override
    public String toString() {
        return "RequirementImage{" +
                "id=" + id +
                ", url='" + url + '\'' +
                '}';
    }
}
