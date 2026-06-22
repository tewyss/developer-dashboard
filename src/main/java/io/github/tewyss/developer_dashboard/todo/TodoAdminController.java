package io.github.tewyss.developer_dashboard.todo;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/todos")
public class TodoAdminController {

    private final TodoService todoService;

    public TodoAdminController(TodoService todoService) {
        this.todoService = todoService;
    }

    @ModelAttribute("statuses")
    public TodoStatus[] statuses() {
        return TodoStatus.values();
    }

    @ModelAttribute("priorities")
    public TodoPriority[] priorities() {
        return TodoPriority.values();
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("tasks", todoService.findAll());
        return "admin/todos/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("todoTaskForm", new TodoTaskForm());
        model.addAttribute("mode", "create");
        return "admin/todos/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("todoTaskForm") TodoTaskForm form,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("mode", "create");
            return "admin/todos/form";
        }
        todoService.create(form);
        redirect.addFlashAttribute("success", "Task created.");
        return "redirect:/admin/todos";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("todoTaskForm", todoService.getEditForm(id));
        model.addAttribute("mode", "edit");
        return "admin/todos/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("todoTaskForm") TodoTaskForm form,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("mode", "edit");
            return "admin/todos/form";
        }
        todoService.update(id, form);
        redirect.addFlashAttribute("success", "Task updated.");
        return "redirect:/admin/todos";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        todoService.delete(id);
        redirect.addFlashAttribute("success", "Task deleted.");
        return "redirect:/admin/todos";
    }

    @PostMapping("/{id}/status")
    public String changeStatus(@PathVariable Long id,
                               @RequestParam TodoStatus status,
                               RedirectAttributes redirect) {
        todoService.changeStatus(id, status);
        redirect.addFlashAttribute("success", "Task moved to " + status.getLabel() + ".");
        return "redirect:/admin/todos";
    }
}
