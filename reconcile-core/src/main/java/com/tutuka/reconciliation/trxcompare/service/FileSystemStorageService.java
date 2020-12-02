package com.tutuka.reconciliation.trxcompare.service;

import com.tutuka.reconciliation.infrastructure.exception.StorageException;
import com.tutuka.reconciliation.infrastructure.exception.StorageFileNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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

    
    public void store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            Files.copy(file.getInputStream(), this.rootLocation.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
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
            throw new StorageException("Failed to read stored files", e);
        }

    }

    
    public Path load(String filename) {
    	log.debug("Inside load method");
        return rootLocation.resolve(filename);
    }

    
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
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
        	log.error("Could not read file: " + filename);
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    
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
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
