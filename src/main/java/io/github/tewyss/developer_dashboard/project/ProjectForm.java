package io.github.tewyss.developer_dashboard.project;

import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;


@Data
@NoArgsConstructor
public class ProjectForm {

    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    private String slug;

    @NotBlank(message = "Short description is required")
    @Size(max = 500, message = "Short description must be at most 500 characters")
    private String shortDescription;

    @NotBlank(message = "Long description is required")
    private String longDescription;

    @URL(message = "Must be a valid URL")
    private String githubUrl;

    @URL(message = "Must be a valid URL")
    private String liveDemoUrl;

    @URL(message = "Must be a valid URL")
    private String imageUrl;

    private boolean featured;

    private Set<Long> technologyIds = new LinkedHashSet<>();
}
