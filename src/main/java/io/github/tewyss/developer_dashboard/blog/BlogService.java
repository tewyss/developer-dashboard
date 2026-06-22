package io.github.tewyss.developer_dashboard.blog;

import java.time.LocalDateTime;
import java.util.List;

import io.github.tewyss.developer_dashboard.common.ResourceNotFoundException;
import io.github.tewyss.developer_dashboard.common.SlugUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BlogService {

    private final BlogPostRepository blogPostRepository;

    public BlogService(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    public List<BlogPost> findAllForAdmin() {
        return blogPostRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<BlogPost> findPublished() {
        return blogPostRepository.findByPublishedTrueOrderByPublishedAtDesc();
    }

    public BlogPost getPublishedBySlug(String slug) {
        return blogPostRepository.findBySlugAndPublishedTrue(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found: " + slug));
    }

    public BlogPost getById(Long id) {
        return blogPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found: id " + id));
    }

    public long count() {
        return blogPostRepository.count();
    }

    public BlogPostForm getEditForm(Long id) {
        BlogPost post = getById(id);
        BlogPostForm form = new BlogPostForm();
        form.setId(post.getId());
        form.setTitle(post.getTitle());
        form.setSlug(post.getSlug());
        form.setExcerpt(post.getExcerpt());
        form.setContent(post.getContent());
        form.setPublished(post.isPublished());
        return form;
    }

    @Transactional
    public BlogPost create(BlogPostForm form) {
        BlogPost post = new BlogPost();
        applyForm(post, form);
        post.setSlug(uniqueSlug(slugBase(form), null));
        return blogPostRepository.save(post);
    }

    @Transactional
    public BlogPost update(Long id, BlogPostForm form) {
        BlogPost post = getById(id);
        applyForm(post, form);
        post.setSlug(uniqueSlug(slugBase(form), id));
        return blogPostRepository.save(post);
    }

    @Transactional
    public void delete(Long id) {
        if (!blogPostRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post not found: id " + id);
        }
        blogPostRepository.deleteById(id);
    }

    @Transactional
    public void togglePublished(Long id) {
        BlogPost post = getById(id);
        setPublished(post, !post.isPublished());
        blogPostRepository.save(post);
    }

    private void applyForm(BlogPost post, BlogPostForm form) {
        post.setTitle(form.getTitle());
        post.setExcerpt(emptyToNull(form.getExcerpt()));
        post.setContent(form.getContent());
        setPublished(post, form.isPublished());
    }

    private void setPublished(BlogPost post, boolean published) {
        post.setPublished(published);
        if (published && post.getPublishedAt() == null) {
            post.setPublishedAt(LocalDateTime.now());
        }
    }

    private String slugBase(BlogPostForm form) {
        String base = (form.getSlug() != null && !form.getSlug().isBlank())
                ? SlugUtil.toSlug(form.getSlug())
                : SlugUtil.toSlug(form.getTitle());
        return base.isBlank() ? "post" : base;
    }

    private String uniqueSlug(String base, Long currentId) {
        String slug = base;
        int suffix = 2;
        while (isSlugTaken(slug, currentId)) {
            slug = base + "-" + suffix++;
        }
        return slug;
    }

    private boolean isSlugTaken(String slug, Long currentId) {
        return currentId == null
                ? blogPostRepository.existsBySlug(slug)
                : blogPostRepository.existsBySlugAndIdNot(slug, currentId);
    }

    private String emptyToNull(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }
}
