package io.github.tewyss.developer_dashboard.blog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BlogPostForm {

    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    private String slug;

    @Size(max = 300, message = "Excerpt must be at most 300 characters")
    private String excerpt;

    @NotBlank(message = "Content is required")
    private String content;

    private boolean published;
}
