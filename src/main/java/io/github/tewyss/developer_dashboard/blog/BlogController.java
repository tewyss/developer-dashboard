package io.github.tewyss.developer_dashboard.blog;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/blog")
    public String list(Model model) {
        model.addAttribute("posts", blogService.findPublished());
        return "blog/list";
    }

    @GetMapping("/blog/{slug}")
    public String detail(@PathVariable String slug, Model model) {
        model.addAttribute("post", blogService.getPublishedBySlug(slug));
        return "blog/detail";
    }
}
