package com.tutuka.reconciliation.trxcompare.service;

import com.tutuka.reconciliation.infrastructure.exception.StorageException;
import com.tutuka.reconciliation.infrastructure.exception.StorageFileNotFoundException;
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
public class FileSystemStorageService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Path rootLocation;

//    @Value("${upload.dir}:")
    private String location = "uploads/";

    public FileSystemStorageService() {
        this.rootLocation = Paths.get(location);
    }

    
    public void store(MultipartFile file) {
    	logger.debug("Inside store method of StorageService");
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
            	logger.error("Failed to store empty file " + filename);
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
            	logger.error("Cannot store file with relative path outside current directory "+ filename);
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            Files.copy(file.getInputStream(), this.rootLocation.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
            logger.info("Successfully stored the file "+filename);
        }
        catch (IOException e) {
        	logger.error("IOException, Failed to store file " + filename);
            throw new StorageException("Failed to store file " + filename, e);
        }
       
    }

    
    public Stream<Path> loadAll() {
        try {
        	logger.debug("Inside loadAll method");
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));
        }
        catch (IOException e) {
        	logger.error("Failed to read stored files");
            throw new StorageException("Failed to read stored files", e);
        }

    }

    
    public Path load(String filename) {
    	logger.debug("Inside load method");
        return rootLocation.resolve(filename);
    }

    
    public Resource loadAsResource(String filename) {
    	logger.debug("Inside loadAsResource method");
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
            	logger.error("Could not read file: " + filename);
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
        	logger.error("Could not read file: " + filename);
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    
    public void deleteAll() {
    	logger.debug("Inside deleteAll method");
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
        	logger.error("Could not initialize storage");
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
