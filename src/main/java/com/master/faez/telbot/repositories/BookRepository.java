package com.master.faez.telbot.repositories;

import com.master.faez.telbot.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    public Optional<Book> findBookByName(String name);
}
