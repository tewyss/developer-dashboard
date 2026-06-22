package io.github.tewyss.developer_dashboard.note;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NoteForm {

    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    private String content;
}
