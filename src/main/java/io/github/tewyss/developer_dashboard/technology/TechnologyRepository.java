package io.github.tewyss.developer_dashboard.technology;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TechnologyRepository extends JpaRepository<Technology, Long> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    List<Technology> findAllByOrderByNameAsc();
}
