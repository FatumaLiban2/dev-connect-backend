package org.devconnect.devconnectbackend.repository;

import org.devconnect.devconnectbackend.model.Project;
import org.devconnect.devconnectbackend.model.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByDevId(Long devId);
    List<Project> findByClientId(Long clientId);
    List<Project> findByStatus(ProjectStatus status);
    List<Project> findByDevIdAndStatus(Long devId, ProjectStatus status);
    List<Project> findByClientIdAndStatus(Long clientId, ProjectStatus status);
}
