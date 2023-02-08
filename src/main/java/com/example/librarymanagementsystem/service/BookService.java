package com.example.librarymanagementsystem.service;

import com.example.librarymanagementsystem.entity.Book;
import java.util.List;
import java.util.Optional;

public interface BookService {

  List<Book> findAllBooks();

  Book createBook(Book book);

  void updateBook(Book book);

  void deleteBook(Long id);

  Optional<Book> findBookById(Long id);
}
