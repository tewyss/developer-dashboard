package io.github.tewyss.developer_dashboard.blog;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);

    List<BlogPost> findAllByOrderByCreatedAtDesc();

    List<BlogPost> findByPublishedTrueOrderByPublishedAtDesc();

    Optional<BlogPost> findBySlugAndPublishedTrue(String slug);

    long countByPublishedTrue();
}
