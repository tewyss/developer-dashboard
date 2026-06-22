package io.github.tewyss.developer_dashboard.note;

import java.util.List;

import io.github.tewyss.developer_dashboard.common.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> findAll() {
        return noteRepository.findAllByOrderByUpdatedAtDesc();
    }

    public Note getById(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found: id " + id));
    }

    public long count() {
        return noteRepository.count();
    }

    public NoteForm getEditForm(Long id) {
        Note note = getById(id);
        NoteForm form = new NoteForm();
        form.setId(note.getId());
        form.setTitle(note.getTitle());
        form.setContent(note.getContent());
        return form;
    }

    @Transactional
    public Note create(NoteForm form) {
        Note note = new Note();
        applyForm(note, form);
        return noteRepository.save(note);
    }

    @Transactional
    public Note update(Long id, NoteForm form) {
        Note note = getById(id);
        applyForm(note, form);
        return noteRepository.save(note);
    }

    @Transactional
    public void delete(Long id) {
        if (!noteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Note not found: id " + id);
        }
        noteRepository.deleteById(id);
    }

    private void applyForm(Note note, NoteForm form) {
        note.setTitle(form.getTitle());
        note.setContent(emptyToNull(form.getContent()));
    }

    private String emptyToNull(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }
}
