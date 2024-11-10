package org.example;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ForumRepo extends JpaRepository<Forum, Long> {
    Page<Forum> findByName(String name, Pageable pageable);
}
