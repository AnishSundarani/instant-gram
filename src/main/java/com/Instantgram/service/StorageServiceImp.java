package com.Instantgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.Instantgram.config.StorageProperties;

import javax.annotation.PostConstruct;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class StorageServiceImp implements StorageService {

    private final Path rootLocation;

    @Autowired
    public StorageServiceImp(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    @PostConstruct
    public void init() throws Exception {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new Exception("Could not initialize storage location", e);
        }
    }

    @Override
    public String store(MultipartFile file) throws Exception {
        String filename = System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new Exception("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new Exception(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new Exception("Failed to store file " + filename, e);
        }

        return filename;
    }

    @Override
    public Stream<Path> loadAll() throws Exception {
        try {
			try {
			    return Files.walk(this.rootLocation, 1)
			            .filter(path -> !path.equals(this.rootLocation))
			            .map(this.rootLocation::relativize);
			}
			catch (IOException e) {
			    throw new Exception("Failed to read stored files", e);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) throws FileNotFoundException {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new FileNotFoundException(
                        "Could not read file: " + filename);
            }
        }
        catch (MalformedURLException e) {
            throw new FileNotFoundException("Could not read file: " + filename);
        }
    }

    @Override
    public void delete(String filename) throws IOException {
    	FileSystemUtils.deleteRecursively(rootLocation.resolve(filename));
    }
    
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
}