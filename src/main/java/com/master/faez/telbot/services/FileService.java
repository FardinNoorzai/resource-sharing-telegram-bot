package com.master.faez.telbot.services;

import com.master.faez.telbot.models.File;
import com.master.faez.telbot.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FileService {

    @Autowired
    FileRepository fileRepository;

    public File save(File file) {
        return fileRepository.save(file);
    }
    public File findByName(String name) {

        return fileRepository.findByFileName(name).orElse(null);
    }
    public void delete(File file) {
        fileRepository.delete(file);
    }
}
