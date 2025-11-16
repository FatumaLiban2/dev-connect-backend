package org.devconnect.devconnectbackend.service;

import lombok.RequiredArgsConstructor;
import org.devconnect.devconnectbackend.dto.ProjectRequestDTO;
import org.devconnect.devconnectbackend.dto.ProjectResponseDTO;
import org.devconnect.devconnectbackend.model.Project;
import org.devconnect.devconnectbackend.model.ProjectStatus;
import org.devconnect.devconnectbackend.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    /**
     * Add a new project
     */
    @Transactional
    public ProjectResponseDTO addProject(ProjectRequestDTO requestDTO) {
        Project project = new Project();
        project.setProjectName(requestDTO.getProjectName());
        project.setDevId(requestDTO.getDevId());
        project.setClientId(requestDTO.getClientId());
        project.setDescription(requestDTO.getDescription());
        project.setProjectBudget(requestDTO.getProjectBudget());
        project.setTimeline(requestDTO.getTimeline());
        project.setImageUrls(requestDTO.getImageUrls());
        project.setStatus(ProjectStatus.PENDING);

        Project savedProject = projectRepository.save(project);
        return mapToResponseDTO(savedProject);
    }

    /**
     * Update an existing project
     */
    @Transactional
    public ProjectResponseDTO updateProject(Long projectId, ProjectRequestDTO requestDTO) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        if (requestDTO.getProjectName() != null) {
            project.setProjectName(requestDTO.getProjectName());
        }
        if (requestDTO.getDescription() != null) {
            project.setDescription(requestDTO.getDescription());
        }
        if (requestDTO.getProjectBudget() != null) {
            project.setProjectBudget(requestDTO.getProjectBudget());
        }
        if (requestDTO.getTimeline() != null) {
            project.setTimeline(requestDTO.getTimeline());
        }
        if (requestDTO.getImageUrls() != null) {
            project.setImageUrls(requestDTO.getImageUrls());
        }

        Project updatedProject = projectRepository.save(project);
        return mapToResponseDTO(updatedProject);
    }

    /**
     * Delete a project
     */
    @Transactional
    public void deleteProject(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("Project not found with id: " + projectId);
        }
        projectRepository.deleteById(projectId);
    }

    /**
     * Mark a project as completed
     */
    @Transactional
    public ProjectResponseDTO markProjectAsCompleted(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        project.setStatus(ProjectStatus.COMPLETED);
        Project updatedProject = projectRepository.save(project);
        return mapToResponseDTO(updatedProject);
    }

    /**
     * Update project status
     */
    @Transactional
    public ProjectResponseDTO updateProjectStatus(Long projectId, ProjectStatus status) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        project.setStatus(status);
        Project updatedProject = projectRepository.save(project);
        return mapToResponseDTO(updatedProject);
    }

    /**
     * Get project by ID
     */
    public ProjectResponseDTO getProjectById(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));
        return mapToResponseDTO(project);
    }

    /**
     * Get all projects
     */
    public List<ProjectResponseDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get projects by developer ID
     */
    public List<ProjectResponseDTO> getProjectsByDevId(Long devId) {
        return projectRepository.findByDevId(devId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get projects by client ID
     */
    public List<ProjectResponseDTO> getProjectsByClientId(Long clientId) {
        return projectRepository.findByClientId(clientId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get projects by status
     */
    public List<ProjectResponseDTO> getProjectsByStatus(ProjectStatus status) {
        return projectRepository.findByStatus(status).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Map Project entity to ProjectResponseDTO
     */
    private ProjectResponseDTO mapToResponseDTO(Project project) {
        ProjectResponseDTO dto = new ProjectResponseDTO();
        dto.setProjectId(project.getProjectId());
        dto.setProjectName(project.getProjectName());
        dto.setDevId(project.getDevId());
        dto.setClientId(project.getClientId());
        dto.setDescription(project.getDescription());
        dto.setStatus(project.getStatus());
        dto.setProjectBudget(project.getProjectBudget());
        dto.setTimeline(project.getTimeline());
        dto.setImageUrls(project.getImageUrls());
        dto.setCreatedAt(project.getCreatedAt());
        dto.setUpdatedAt(project.getUpdatedAt());
        return dto;
    }
}
