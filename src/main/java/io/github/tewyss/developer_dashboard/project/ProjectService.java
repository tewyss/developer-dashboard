package io.github.tewyss.developer_dashboard.project;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.github.tewyss.developer_dashboard.common.ResourceNotFoundException;
import io.github.tewyss.developer_dashboard.common.SlugUtil;
import io.github.tewyss.developer_dashboard.technology.Technology;
import io.github.tewyss.developer_dashboard.technology.TechnologyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TechnologyRepository technologyRepository;

    public ProjectService(ProjectRepository projectRepository,
                          TechnologyRepository technologyRepository) {
        this.projectRepository = projectRepository;
        this.technologyRepository = technologyRepository;
    }


    public List<Project> findAll() {
        return projectRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Project> findFeatured() {
        return projectRepository.findByFeaturedTrueOrderByCreatedAtDesc();
    }

    public Project getBySlug(String slug) {
        return projectRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + slug));
    }

    public Project getById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: id " + id));
    }

    public long count() {
        return projectRepository.count();
    }

    @Transactional(readOnly = true)
    public ProjectForm getEditForm(Long id) {
        Project project = getById(id);
        ProjectForm form = new ProjectForm();
        form.setId(project.getId());
        form.setTitle(project.getTitle());
        form.setSlug(project.getSlug());
        form.setShortDescription(project.getShortDescription());
        form.setLongDescription(project.getLongDescription());
        form.setGithubUrl(project.getGithubUrl());
        form.setLiveDemoUrl(project.getLiveDemoUrl());
        form.setImageUrl(project.getImageUrl());
        form.setFeatured(project.isFeatured());
        form.setTechnologyIds(project.getTechnologies().stream()
                .map(Technology::getId)
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        return form;
    }

    @Transactional
    public Project create(ProjectForm form) {
        Project project = new Project();
        applyForm(project, form);
        project.setSlug(uniqueSlug(slugBase(form), null));
        return projectRepository.save(project);
    }

    @Transactional
    public Project update(Long id, ProjectForm form) {
        Project project = getById(id);
        applyForm(project, form);
        project.setSlug(uniqueSlug(slugBase(form), id));
        return projectRepository.save(project);
    }

    @Transactional
    public void delete(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found: id " + id);
        }
        projectRepository.deleteById(id);
    }

    @Transactional
    public void toggleFeatured(Long id) {
        Project project = getById(id);
        project.setFeatured(!project.isFeatured());
        projectRepository.save(project);
    }

    private void applyForm(Project project, ProjectForm form) {
        project.setTitle(form.getTitle());
        project.setShortDescription(form.getShortDescription());
        project.setLongDescription(form.getLongDescription());
        project.setGithubUrl(emptyToNull(form.getGithubUrl()));
        project.setLiveDemoUrl(emptyToNull(form.getLiveDemoUrl()));
        project.setImageUrl(emptyToNull(form.getImageUrl()));
        project.setFeatured(form.isFeatured());
        project.setTechnologies(resolveTechnologies(form.getTechnologyIds()));
    }

    private Set<Technology> resolveTechnologies(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new LinkedHashSet<>();
        }
        return new LinkedHashSet<>(technologyRepository.findAllById(ids));
    }

    private String slugBase(ProjectForm form) {
        String base = (form.getSlug() != null && !form.getSlug().isBlank())
                ? SlugUtil.toSlug(form.getSlug())
                : SlugUtil.toSlug(form.getTitle());
        return base.isBlank() ? "project" : base;
    }

    private String uniqueSlug(String base, Long currentId) {
        String slug = base;
        int suffix = 2;
        while (isSlugTaken(slug, currentId)) {
            slug = base + "-" + suffix++;
        }
        return slug;
    }

    private boolean isSlugTaken(String slug, Long currentId) {
        return currentId == null
                ? projectRepository.existsBySlug(slug)
                : projectRepository.existsBySlugAndIdNot(slug, currentId);
    }

    private String emptyToNull(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }
}
