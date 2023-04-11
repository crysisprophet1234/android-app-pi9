package com.example.requirements_manager.repositories;

import com.example.requirements_manager.entities.Requirement;
import com.example.requirements_manager.entities.RequirementImage;

import java.util.List;
import java.util.Set;

public interface RequirementImageRepository {

    List<RequirementImage> findAll();

    List<RequirementImage> findByRequirementId(Long id);

    RequirementImage findById(Long id);

    RequirementImage save(RequirementImage requirementImage);

    Set<RequirementImage> saveAll(Set<RequirementImage> requirementImages);

    RequirementImage update(RequirementImage requirementImage);

    void delete(Long id);

    void deleteAll();

}
