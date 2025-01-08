package com.master.faez.telbot.services;

import com.master.faez.telbot.models.About;
import com.master.faez.telbot.repositories.AboutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AboutService {
    @Autowired
    AboutRepository aboutRepository;

    public List<About> getAllAbouts() {
        return aboutRepository.findAll();
    }
    public About saveAbout(About about) {
        return aboutRepository.save(about);
    }
    public void deleteAllAbout() {
        aboutRepository.deleteAll();
    }
}
