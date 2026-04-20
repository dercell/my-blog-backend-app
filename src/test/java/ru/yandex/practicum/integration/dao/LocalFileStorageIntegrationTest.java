package ru.yandex.practicum.integration.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.dao.impl.LocalFileStorage;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

@Tag("dao")
@Tag("integration")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = LocalFileStorage.class)
class LocalFileStorageIntegrationTest {

    @Autowired
    private LocalFileStorage localFileStorage;

    @AfterEach
    void cleanUpDirectory() throws IOException {
        Path path = Path.of("./uploads/images");
        Files.walkFileTree(path, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                Files.delete(file);  // Удаляем файлы
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                    throws IOException {
                Files.delete(dir);  // Удаляем пустые папки
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Test
    void upload() throws IOException {
        MultipartFile file = new MockMultipartFile("image.png", "image.png", "image/png", new byte[]{1, 2, 3, 4});

        String result = localFileStorage.upload(file);
        assertTrue(result.endsWith(".png"));
    }

    @Test
    void download() throws IOException {
        MultipartFile file = new MockMultipartFile("image.png", "image.png", "image/png", new byte[]{1, 2, 3, 4});

        String savedFilename = localFileStorage.upload(file);
        Resource result = localFileStorage.download(savedFilename);

        assertArrayEquals(file.getBytes(), result.getContentAsByteArray());
    }

    @Test
    void delete() throws IOException {
        MultipartFile file = new MockMultipartFile("image.png", "image.png", "image/png", new byte[]{1, 2, 3, 4});

        String savedFilename = localFileStorage.upload(file);

        localFileStorage.delete(savedFilename);
        assertThrows(
                NoSuchFileException.class,
                () -> localFileStorage.download(savedFilename)
        );

    }

}
