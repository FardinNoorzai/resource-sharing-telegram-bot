package com.master.faez.telbot.repositories;

import com.master.faez.telbot.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
}
