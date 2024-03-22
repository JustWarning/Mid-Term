package com.midterm.service.framework;

import java.util.List;
import com.midterm.dto.BooksDto;

public interface BooksService {
    List<BooksDto> getAllBooks();

    BooksDto findById(Long id);

    BooksDto findByAuthor(String author);

    BooksDto save(BooksDto bookDto);

    void deleteById(Long id);
}
