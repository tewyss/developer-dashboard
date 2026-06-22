package io.github.tewyss.developer_dashboard.project;

import java.util.List;

import io.github.tewyss.developer_dashboard.technology.Technology;
import io.github.tewyss.developer_dashboard.technology.TechnologyService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/admin/projects")
public class ProjectAdminController {

    private final ProjectService projectService;
    private final TechnologyService technologyService;

    public ProjectAdminController(ProjectService projectService,
                                  TechnologyService technologyService) {
        this.projectService = projectService;
        this.technologyService = technologyService;
    }

    @ModelAttribute("allTechnologies")
    public List<Technology> allTechnologies() {
        return technologyService.findAll();
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("projects", projectService.findAll());
        return "admin/projects/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("projectForm", new ProjectForm());
        model.addAttribute("mode", "create");
        return "admin/projects/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("projectForm") ProjectForm form,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("mode", "create");
            return "admin/projects/form";
        }
        projectService.create(form);
        redirect.addFlashAttribute("success", "Project created.");
        return "redirect:/admin/projects";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("projectForm", projectService.getEditForm(id));
        model.addAttribute("mode", "edit");
        return "admin/projects/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("projectForm") ProjectForm form,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("mode", "edit");
            return "admin/projects/form";
        }
        projectService.update(id, form);
        redirect.addFlashAttribute("success", "Project updated.");
        return "redirect:/admin/projects";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        projectService.delete(id);
        redirect.addFlashAttribute("success", "Project deleted.");
        return "redirect:/admin/projects";
    }

    @PostMapping("/{id}/feature")
    public String toggleFeatured(@PathVariable Long id, RedirectAttributes redirect) {
        projectService.toggleFeatured(id);
        redirect.addFlashAttribute("success", "Featured status updated.");
        return "redirect:/admin/projects";
    }
}
