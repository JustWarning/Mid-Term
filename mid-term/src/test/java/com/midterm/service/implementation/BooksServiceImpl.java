package com.midterm.service.implementation;

import com.midterm.dto.BooksDto;
import com.midterm.entity.Books;
import com.midterm.repository.BooksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class BooksServiceImplTest {

    @Mock
    private BooksRepository booksRepository;

    @InjectMocks
    private BooksServiceImpl booksService;

    private Books book;
    private BooksDto bookDto;

    @BeforeEach
    void setUp() {
        book = new Books();
        book.setId(1L);
        book.setTitle("The Hobbit");
        book.setAuthor("J.R.R. Tolkien");
        book.setDescription("A fantasy novel");
        book.setPrice(10);
        book.setStock(100);

        bookDto = new BooksDto(book.getId(), book.getTitle(), book.getAuthor(), book.getDescription(), book.getPrice(), book.getStock());
    }

    @Test
    void whenGetAllBooks_thenReturnBooksList() {
        when(booksRepository.findAll()).thenReturn(List.of(book));

        List<BooksDto> result = booksService.getAllBooks();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    void whenFindById_thenReturnBookDto() {
        given(booksRepository.findById(any(Long.class))).willReturn(Optional.of(book));

        BooksDto result = booksService.findById(book.getId());

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    void whenFindByAuthor_thenReturnBookDto() {
        given(booksRepository.findByAuthor(any(String.class))).willReturn(Optional.of(book));

        BooksDto result = booksService.findByAuthor(book.getAuthor());

        assertThat(result).isNotNull();
        assertThat(result.getAuthor()).isEqualTo(book.getAuthor());
    }

    @Test
    void whenSave_thenReturnSavedBookDto() {
        given(booksRepository.save(any(Books.class))).willReturn(book);

        BooksDto savedBook = booksService.save(bookDto);

        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getId()).isEqualTo(book.getId());
    }

    @Test
    void whenDeleteById_thenBookShouldBeDeleted() {
        booksService.deleteById(book.getId());

        verify(booksRepository, times(1)).deleteById(book.getId());
    }

}

