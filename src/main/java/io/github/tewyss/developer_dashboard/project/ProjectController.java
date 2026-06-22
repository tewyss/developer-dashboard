package io.github.tewyss.developer_dashboard.project;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/projects")
    public String list(Model model) {
        model.addAttribute("projects", projectService.findAll());
        return "projects/list";
    }

    @GetMapping("/projects/{slug}")
    public String detail(@PathVariable String slug, Model model) {
        model.addAttribute("project", projectService.getBySlug(slug));
        return "projects/detail";
    }
}
