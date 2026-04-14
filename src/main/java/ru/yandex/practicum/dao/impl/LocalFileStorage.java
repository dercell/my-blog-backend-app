package ru.yandex.practicum.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.dao.PostImageStorage;
import ru.yandex.practicum.util.exceptions.StorageException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
public class LocalFileStorage implements PostImageStorage {

    private final String UPLOAD_DIR = Objects.requireNonNullElse(System.getenv("CATALINA_HOME"), ".")
            + "/uploads/images/";

    private final List<String> ALLOWED_CONTENT_TYPES = List.of("image/jpeg", "image/png", "image/webp");

    public String upload(MultipartFile file) throws IOException {

        if (!ALLOWED_CONTENT_TYPES.contains(Optional.ofNullable(file.getContentType()).orElse("empty filetype"))) {
            throw new StorageException("Unsupported file type: " + file.getContentType());
        }


        Path path = Paths.get(UPLOAD_DIR);

        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        UUID fileUuid = UUID.randomUUID();
        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String generatedFileName = String.join("", fileUuid.toString(), ".", fileExtension);

        Path filePath = path.resolve(Objects.requireNonNull(generatedFileName));
        file.transferTo(filePath);

        return generatedFileName;

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
