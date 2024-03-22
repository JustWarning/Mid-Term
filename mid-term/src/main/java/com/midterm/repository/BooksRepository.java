package com.midterm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.midterm.entity.Books;

public interface BooksRepository extends JpaRepository<Books, Long> {
    Optional<Books> findByAuthor(String author);
}
