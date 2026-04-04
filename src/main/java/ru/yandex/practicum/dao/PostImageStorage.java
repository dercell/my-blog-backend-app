package ru.yandex.practicum.dao;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PostImageStorage {

    String upload(MultipartFile file) throws IOException;

    Resource download(String filename) throws IOException;

}
