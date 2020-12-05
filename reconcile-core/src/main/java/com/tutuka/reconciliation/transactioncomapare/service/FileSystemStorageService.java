package com.tutuka.reconciliation.transactioncomapare.service;

import com.tutuka.lib.logger.annotation.Loggable;
import com.tutuka.lib.audit.Auditable;
import com.tutuka.reconciliation.infrastructure.exception.StorageException;
import com.tutuka.reconciliation.infrastructure.exception.StorageFileNotFoundException;
import com.tutuka.reconciliation.infrastructure.internationalization.Translator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
@Slf4j
public class FileSystemStorageService {

    private final Path rootLocation;
    private String location = "uploads/";

    public FileSystemStorageService() {
        this.rootLocation = Paths.get(location);
    }

    @Loggable
    @Auditable
    @Retryable
    public void store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageException(Translator.toLocale("exception.failed-to-store-file") + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        Translator.toLocale("exception.file-store-with-relative-path-outside-directory") + filename);
            }
            Files.copy(file.getInputStream(), this.rootLocation.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) {
            throw new StorageException(Translator.toLocale("exception.failed-to-store-file") + filename, e);
        }
       
    }

    
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));
        }
        catch (IOException e) {
        	log.error("Failed to read stored files");
            throw new StorageException(Translator.toLocale("exception.file-failed-to-read"), e);
        }

    }

    
    public Path load(String filename) {
    	log.debug("Inside load method");
        return rootLocation.resolve(filename);
    }

    @Loggable
    @Auditable
    @Retryable
    public Resource loadAsResource(String filename) {
    	log.debug("Inside loadAsResource method");
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
            	log.error("Could not read file: " + filename);
                throw new StorageFileNotFoundException(
                        Translator.toLocale("exception.file-not-found-exception") + filename);

            }
        }
        catch (MalformedURLException e) {
        	log.error("Could not read file: " + filename);
            throw new StorageFileNotFoundException(Translator.toLocale("exception.file-not-found-exception") + filename, e);
        }
    }

    @Loggable
    @Auditable
    @Retryable
    public void deleteAll() {
    	log.debug("Inside deleteAll method");
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
        	log.error("Could not initialize storage");
            throw new StorageException(Translator.toLocale("exception.directory-initialization"), e);
        }
    }
}
