package ru.yandex.practicum.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.dao.PostImageStorage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Slf4j
@Repository
public class LocalFileStorage implements PostImageStorage {

    private final String UPLOAD_DIR = System.getenv("CATALINA_HOME") + "/uploads/images/";

    public String upload(MultipartFile file) throws IOException {
        Path path = Paths.get(UPLOAD_DIR);

        if(!Files.exists(path)){
            Files.createDirectories(path);
        }

        Path filePath = path.resolve(Objects.requireNonNull(file.getOriginalFilename()));
        file.transferTo(filePath);

        return file.getOriginalFilename();

    }

    public Resource download(String filename) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
        byte[] fileContent = Files.readAllBytes(filePath);

        return new ByteArrayResource(fileContent);

    }

    @Override
    public void delete(String filename) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
        Files.delete(filePath);
    }

}
