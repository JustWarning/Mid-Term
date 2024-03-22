package com.midterm.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.midterm.dto.BooksDto;
import com.midterm.exception.BooksNotFoundException;
import com.midterm.service.framework.BooksService;

@RestController
@RequestMapping("/api/books")
public class BooksController {
    private final BooksService booksService;

    @Autowired
    public BooksController(BooksService booksService) {
        this.booksService = booksService;
    }

    @GetMapping
    public ResponseEntity<List<BooksDto>> getAllBooks() {
        List<BooksDto> books = booksService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<BooksDto> getBooksById(@PathVariable("id") @Min(1) Long id) {
        BooksDto book = booksService.findById(id);
        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            throw new BooksNotFoundException("Books with " + id + " is not found!");
        }
    }

    @PostMapping
    public ResponseEntity<BooksDto> addBooks(@Valid @RequestBody BooksDto bookDto) {
        BooksDto savedBook = booksService.save(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<BooksDto> updateBooks(@PathVariable("id") @Min(1) Long id, @Valid @RequestBody BooksDto newBookDto) {
        BooksDto existingBook = booksService.findById(id);
        if (existingBook != null) {
            newBookDto.setId(id);
            BooksDto updatedBook = booksService.save(newBookDto);
            return ResponseEntity.ok(updatedBook);
        } else {
            throw new BooksNotFoundException("Books with " + id + " is not found!");
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteBooks(@PathVariable("id") @Min(1) Long id) {
        BooksDto book = booksService.findById(id);
        if (book != null) {
            booksService.deleteById(id);
            return ResponseEntity.ok("Books with ID " + id + " is deleted");
        } else {
            throw new BooksNotFoundException("Books with " + id + " is not found!");
        }
    }
}

