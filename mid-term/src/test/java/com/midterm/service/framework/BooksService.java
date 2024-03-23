package com.midterm.service.framework;

import java.util.List;
import com.midterm.dto.BooksDto;

public interface BooksService {
    List<BooksDto> findAll();
    BooksDto findById(Long id);
    BooksDto create(BooksDto bookDto);
    BooksDto update(Long id, BooksDto bookDto);
    void delete(Long id);
}
