package io.github.tewyss.developer_dashboard.technology;

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
@RequestMapping("/admin/technologies")
public class TechnologyAdminController {

    private final TechnologyService technologyService;

    public TechnologyAdminController(TechnologyService technologyService) {
        this.technologyService = technologyService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("technologies", technologyService.findAll());
        return "admin/technologies/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("technologyForm", new TechnologyForm());
        model.addAttribute("mode", "create");
        return "admin/technologies/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("technologyForm") TechnologyForm form,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirect) {
        rejectDuplicateName(form, null, result);
        if (result.hasErrors()) {
            model.addAttribute("mode", "create");
            return "admin/technologies/form";
        }
        technologyService.create(form);
        redirect.addFlashAttribute("success", "Technology created.");
        return "redirect:/admin/technologies";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("technologyForm", technologyService.getEditForm(id));
        model.addAttribute("mode", "edit");
        return "admin/technologies/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("technologyForm") TechnologyForm form,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirect) {
        rejectDuplicateName(form, id, result);
        if (result.hasErrors()) {
            model.addAttribute("mode", "edit");
            return "admin/technologies/form";
        }
        technologyService.update(id, form);
        redirect.addFlashAttribute("success", "Technology updated.");
        return "redirect:/admin/technologies";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            technologyService.delete(id);
            redirect.addFlashAttribute("success", "Technology deleted.");
        } catch (IllegalStateException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/technologies";
    }

    private void rejectDuplicateName(TechnologyForm form, Long id, BindingResult result) {
        if (form.getName() != null && !form.getName().isBlank()
                && technologyService.isNameTaken(form.getName().trim(), id)) {
            result.rejectValue("name", "duplicate", "A technology with this name already exists");
        }
    }
}
