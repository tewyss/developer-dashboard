package io.github.tewyss.developer_dashboard.technology;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TechnologyForm {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Pattern(
            regexp = "^$|^#([0-9a-fA-F]{3}|[0-9a-fA-F]{6})$",
            message = "Use a hex color like #6db33f (or leave blank)"
    )
    private String color;
}
