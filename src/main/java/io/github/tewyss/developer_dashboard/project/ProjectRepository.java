package io.github.tewyss.developer_dashboard.project;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @EntityGraph(attributePaths = "technologies")
    Optional<Project> findBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);

    boolean existsByTechnologies_Id(Long technologyId);

    @EntityGraph(attributePaths = "technologies")
    List<Project> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = "technologies")
    List<Project> findByFeaturedTrueOrderByCreatedAtDesc();
}
