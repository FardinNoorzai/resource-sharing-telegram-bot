package com.master.faez.telbot.services;

import com.master.faez.telbot.models.Book;
import com.master.faez.telbot.models.Resource;
import com.master.faez.telbot.repositories.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResourceService {
    @Autowired
    ResourceRepository resourceRepository;


    public Resource save(Resource resource) {
        return resourceRepository.save(resource);
    }

    public List<String> findAllBooksNames() {
        List<Resource> resources = resourceRepository.findAll();
        List<String> booksNames = new ArrayList<>();
        resources.forEach(resource -> booksNames.add(resource.getName()));
        return booksNames;
    }
    public Resource findByName(String name) {
        return resourceRepository.findByName(name).orElse(null);
    }


    public void deleteById(Integer id) {
        resourceRepository.deleteById(id);
    }
}
