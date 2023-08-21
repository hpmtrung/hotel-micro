package com.fs.repositories;

import com.fs.domain.File;
import com.fs.domain.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Integer> {

  Optional<File> findByNameAndRelPathAndWorkspace(String name, String relPath, Workspace workspace);
}