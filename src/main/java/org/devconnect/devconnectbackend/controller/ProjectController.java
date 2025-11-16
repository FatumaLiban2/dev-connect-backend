package org.devconnect.devconnectbackend.controller;

import lombok.RequiredArgsConstructor;
import org.devconnect.devconnectbackend.dto.ProjectRequestDTO;
import org.devconnect.devconnectbackend.dto.ProjectResponseDTO;
import org.devconnect.devconnectbackend.model.ProjectStatus;
import org.devconnect.devconnectbackend.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    /**
     * Create a new project
     * POST /api/projects
     */
    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createProject(@RequestBody ProjectRequestDTO requestDTO) {
        try {
            ProjectResponseDTO response = projectService.addProject(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Update an existing project
     * PUT /api/projects/{projectId}
     */
    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDTO> updateProject(
            @PathVariable Long projectId,
            @RequestBody ProjectRequestDTO requestDTO) {
        try {
            ProjectResponseDTO response = projectService.updateProject(projectId, requestDTO);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Delete a project
     * DELETE /api/projects/{projectId}
     */
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Map<String, String>> deleteProject(@PathVariable Long projectId) {
        try {
            projectService.deleteProject(projectId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Project deleted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Mark a project as completed
     * PATCH /api/projects/{projectId}/complete
     */
    @PatchMapping("/{projectId}/complete")
    public ResponseEntity<ProjectResponseDTO> markProjectAsCompleted(@PathVariable Long projectId) {
        try {
            ProjectResponseDTO response = projectService.markProjectAsCompleted(projectId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Update project status
     * PATCH /api/projects/{projectId}/status
     */
    @PatchMapping("/{projectId}/status")
    public ResponseEntity<ProjectResponseDTO> updateProjectStatus(
            @PathVariable Long projectId,
            @RequestParam ProjectStatus status) {
        try {
            ProjectResponseDTO response = projectService.updateProjectStatus(projectId, status);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get project by ID
     * GET /api/projects/{projectId}
     */
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable Long projectId) {
        try {
            ProjectResponseDTO response = projectService.getProjectById(projectId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get all projects
     * GET /api/projects
     */
    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        List<ProjectResponseDTO> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    /**
     * Get projects by developer ID
     * GET /api/projects/developer/{devId}
     */
    @GetMapping("/developer/{devId}")
    public ResponseEntity<List<ProjectResponseDTO>> getProjectsByDevId(@PathVariable Long devId) {
        List<ProjectResponseDTO> projects = projectService.getProjectsByDevId(devId);
        return ResponseEntity.ok(projects);
    }

    /**
     * Get projects by client ID
     * GET /api/projects/client/{clientId}
     */
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ProjectResponseDTO>> getProjectsByClientId(@PathVariable Long clientId) {
        List<ProjectResponseDTO> projects = projectService.getProjectsByClientId(clientId);
        return ResponseEntity.ok(projects);
    }

    /**
     * Get projects by status
     * GET /api/projects/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ProjectResponseDTO>> getProjectsByStatus(@PathVariable ProjectStatus status) {
        List<ProjectResponseDTO> projects = projectService.getProjectsByStatus(status);
        return ResponseEntity.ok(projects);
    }
}
