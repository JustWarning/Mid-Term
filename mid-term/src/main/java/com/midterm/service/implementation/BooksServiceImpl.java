package com.midterm.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.midterm.dto.BooksDto;
import com.midterm.entity.Books;
import com.midterm.repository.BooksRepository;
import com.midterm.service.framework.BooksService;

@Service
public class BooksServiceImpl implements BooksService {
    private final BooksRepository booksRepository;

    @Autowired
    public BooksServiceImpl(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Override
    public List<BooksDto> getAllBooks() {
        return booksRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public BooksDto findById(Long id) {
        Books book = booksRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        return convertToDto(book);
    }

    @Override
    public BooksDto findByAuthor(String author) {
        Books book = booksRepository.findByAuthor(author)
                .orElseThrow(() -> new RuntimeException("Book not found by author"));
        return convertToDto(book);
    }

    @Override
    public BooksDto save(BooksDto bookDto) {
        Books book = convertToEntity(bookDto);
        book = booksRepository.save(book);
        return convertToDto(book);
    }

    @Override
    public void deleteById(Long id) {
        booksRepository.deleteById(id);
    }

    private BooksDto convertToDto(Books book) {
        return new BooksDto(book.getId(), book.getTitle(), book.getAuthor(), book.getDescription(), book.getPrice(), book.getStock());
    }
    private Books convertToEntity(BooksDto bookDto) {
        Books book = new Books();
        book.setId(bookDto.getId());
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setDescription(bookDto.getDescription());
        book.setPrice(bookDto.getPrice());
        book.setStock(bookDto.getStock());
        return book;
    }
}
