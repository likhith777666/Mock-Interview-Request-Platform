package com.example.mockinterview.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path root;

    public FileStorageService(@Value("${app.upload-dir:uploads/resumes}") String uploadDir) throws IOException {
        this.root = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(root);
    }

    public StoredFile store(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Resume file is required.");
        }

        String original = StringUtils.cleanPath(file.getOriginalFilename() == null ? "resume" : file.getOriginalFilename());
        String extension = "";
        int dot = original.lastIndexOf('.');
        if (dot >= 0) {
            extension = original.substring(dot).toLowerCase();
        }

        if (!extension.equals(".pdf") && !extension.equals(".doc") && !extension.equals(".docx")) {
            throw new IllegalArgumentException("Only PDF, DOC and DOCX resumes are allowed.");
        }

        String storedName = UUID.randomUUID() + extension;
        Path destination = root.resolve(storedName).normalize();

        if (!destination.startsWith(root)) {
            throw new IllegalArgumentException("Invalid file name.");
        }

        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return new StoredFile(original, destination.toString());
    }

    public record StoredFile(String originalName, String storedPath) {}
}
