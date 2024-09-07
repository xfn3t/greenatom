package com.caselab.greenatom.repository;

import com.caselab.greenatom.dto.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    Boolean existsByTitle(String title);
    Boolean existsByFile(String file);
}
