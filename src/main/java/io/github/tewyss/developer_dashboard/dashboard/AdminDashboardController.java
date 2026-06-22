package io.github.tewyss.developer_dashboard.dashboard;

import io.github.tewyss.developer_dashboard.blog.BlogService;
import io.github.tewyss.developer_dashboard.note.NoteService;
import io.github.tewyss.developer_dashboard.project.ProjectService;
import io.github.tewyss.developer_dashboard.technology.TechnologyService;
import io.github.tewyss.developer_dashboard.todo.TodoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final ProjectService projectService;
    private final TechnologyService technologyService;
    private final BlogService blogService;
    private final NoteService noteService;
    private final TodoService todoService;

    public AdminDashboardController(ProjectService projectService,
                                    TechnologyService technologyService,
                                    BlogService blogService,
                                    NoteService noteService,
                                    TodoService todoService) {
        this.projectService = projectService;
        this.technologyService = technologyService;
        this.blogService = blogService;
        this.noteService = noteService;
        this.todoService = todoService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("projectCount", projectService.count());
        model.addAttribute("technologyCount", technologyService.count());
        model.addAttribute("blogCount", blogService.count());
        model.addAttribute("noteCount", noteService.count());
        model.addAttribute("todoCount", todoService.count());
        model.addAttribute("todoOpenCount", todoService.countOpen());
        model.addAttribute("todoDoneCount", todoService.countDone());
        return "admin/dashboard";
    }
}
