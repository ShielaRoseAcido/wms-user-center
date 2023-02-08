package com.example.librarymanagementsystem;

import com.example.librarymanagementsystem.entity.Book;
import com.example.librarymanagementsystem.service.BookService;
import com.example.librarymanagementsystem.service.impl.BookServiceImpl;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LibraryManagementSystemApplication implements ApplicationRunner {

	@Autowired
	private BookService bookService;

	public static void main(String[] args) {
		SpringApplication.run(LibraryManagementSystemApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		List<Book> books = bookService.findAllBooks();

		if(books != null && books.isEmpty()) {
			Book book1 = Book.builder().name("Book 1").description("description of book 1").price(12.5)
					.build();
			Book book2 = Book.builder().name("Book 2").description("description of book 2").price(15)
					.build();
			Book book3 = Book.builder().name("Book 3").description("description of book 3").price(20)
					.build();

			Arrays.asList(book1, book2, book3).forEach(b -> bookService.createBook(b));

			System.out.println("New books added in database");
		}
	}

}
