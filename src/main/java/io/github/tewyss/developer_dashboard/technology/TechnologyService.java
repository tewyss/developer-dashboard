package io.github.tewyss.developer_dashboard.technology;

import java.util.List;

import io.github.tewyss.developer_dashboard.common.ResourceNotFoundException;
import io.github.tewyss.developer_dashboard.project.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TechnologyService {

    private final TechnologyRepository technologyRepository;
    private final ProjectRepository projectRepository;

    public TechnologyService(TechnologyRepository technologyRepository,
                             ProjectRepository projectRepository) {
        this.technologyRepository = technologyRepository;
        this.projectRepository = projectRepository;
    }


    public List<Technology> findAll() {
        return technologyRepository.findAllByOrderByNameAsc();
    }

    public Technology getById(Long id) {
        return technologyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Technology not found: id " + id));
    }

    public long count() {
        return technologyRepository.count();
    }

    public TechnologyForm getEditForm(Long id) {
        Technology tech = getById(id);
        TechnologyForm form = new TechnologyForm();
        form.setId(tech.getId());
        form.setName(tech.getName());
        form.setColor(tech.getColor());
        return form;
    }

    public boolean isNameTaken(String name, Long id) {
        return id == null
                ? technologyRepository.existsByNameIgnoreCase(name)
                : technologyRepository.existsByNameIgnoreCaseAndIdNot(name, id);
    }

    public boolean isInUse(Long id) {
        return projectRepository.existsByTechnologies_Id(id);
    }

    @Transactional
    public Technology create(TechnologyForm form) {
        Technology tech = new Technology();
        tech.setName(form.getName().trim());
        tech.setColor(emptyToNull(form.getColor()));
        return technologyRepository.save(tech);
    }

    @Transactional
    public Technology update(Long id, TechnologyForm form) {
        Technology tech = getById(id);
        tech.setName(form.getName().trim());
        tech.setColor(emptyToNull(form.getColor()));
        return technologyRepository.save(tech);
    }

    @Transactional
    public void delete(Long id) {
        Technology tech = getById(id);
        if (projectRepository.existsByTechnologies_Id(id)) {
            throw new IllegalStateException(
                    "Cannot delete \"" + tech.getName() + "\" because it is still assigned to one or more projects.");
        }
        technologyRepository.delete(tech);
    }

    private String emptyToNull(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }
}
