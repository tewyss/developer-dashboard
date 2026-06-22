package io.github.tewyss.developer_dashboard.note;

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
@RequestMapping("/admin/notes")
public class NoteAdminController {

    private final NoteService noteService;

    public NoteAdminController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("notes", noteService.findAll());
        return "admin/notes/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("noteForm", new NoteForm());
        model.addAttribute("mode", "create");
        return "admin/notes/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("noteForm") NoteForm form,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("mode", "create");
            return "admin/notes/form";
        }
        noteService.create(form);
        redirect.addFlashAttribute("success", "Note created.");
        return "redirect:/admin/notes";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("noteForm", noteService.getEditForm(id));
        model.addAttribute("mode", "edit");
        return "admin/notes/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("noteForm") NoteForm form,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("mode", "edit");
            return "admin/notes/form";
        }
        noteService.update(id, form);
        redirect.addFlashAttribute("success", "Note updated.");
        return "redirect:/admin/notes";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        noteService.delete(id);
        redirect.addFlashAttribute("success", "Note deleted.");
        return "redirect:/admin/notes";
    }
}
