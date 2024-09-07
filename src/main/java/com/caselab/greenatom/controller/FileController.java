package com.caselab.greenatom.controller;

import com.caselab.greenatom.dto.File;
import com.caselab.greenatom.repository.FileRepository;
import com.caselab.greenatom.response.FileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class FileController {

    private final FileRepository fileRepository;

    @GetMapping
    public ResponseEntity<?> getAll() {

        log.info("Get all files");

        List<FileResponse> fileResponses = fileRepository.findAll().stream()
                .map(file -> new FileResponse(
                        file.getTitle(),
                        file.getCreationDate(),
                        file.getDescription()
                ))
                .toList();

        return ResponseEntity.ok().body(fileResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        log.info("Get by ID: {}", id);
        return fileRepository.findById(id)
                .map(file -> ResponseEntity.ok(file))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> addFile(@RequestBody File file) {

        log.info("Add file Title: {}, Description: {}", file.getTitle(), file.getDescription());

        if (file.getTitle() == null || file.getFile() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid file data"));
        }

        if (fileRepository.existsByTitle(file.getTitle()) || fileRepository.existsByFile(file.getFile())) {
            return ResponseEntity.badRequest().body(Map.of("error", "File exists"));
        }

        File savedFile = fileRepository.save(file);
        return ResponseEntity.status(201).body(Map.of("fileId", savedFile.getFileId()));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFileById(@PathVariable Long id) {

        log.info("Delete by ID: {}", id);

        if (fileRepository.existsById(id)) {
            fileRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
