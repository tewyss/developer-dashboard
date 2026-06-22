package io.github.tewyss.developer_dashboard.blog;

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
@RequestMapping("/admin/blog")
public class BlogAdminController {

    private final BlogService blogService;

    public BlogAdminController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("posts", blogService.findAllForAdmin());
        return "admin/blog/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("blogPostForm", new BlogPostForm());
        model.addAttribute("mode", "create");
        return "admin/blog/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("blogPostForm") BlogPostForm form,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("mode", "create");
            return "admin/blog/form";
        }
        blogService.create(form);
        redirect.addFlashAttribute("success", "Post created.");
        return "redirect:/admin/blog";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("blogPostForm", blogService.getEditForm(id));
        model.addAttribute("mode", "edit");
        return "admin/blog/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("blogPostForm") BlogPostForm form,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("mode", "edit");
            return "admin/blog/form";
        }
        blogService.update(id, form);
        redirect.addFlashAttribute("success", "Post updated.");
        return "redirect:/admin/blog";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        blogService.delete(id);
        redirect.addFlashAttribute("success", "Post deleted.");
        return "redirect:/admin/blog";
    }

    @PostMapping("/{id}/publish")
    public String togglePublished(@PathVariable Long id, RedirectAttributes redirect) {
        blogService.togglePublished(id);
        redirect.addFlashAttribute("success", "Publish status updated.");
        return "redirect:/admin/blog";
    }
}
