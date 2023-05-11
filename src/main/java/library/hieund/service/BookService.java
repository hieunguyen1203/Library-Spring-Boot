package library.hieund.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import library.hieund.exception.CustomException;
import library.hieund.model.User;
import library.hieund.model.Book;
import library.hieund.repository.BookRepository;
import library.hieund.repository.UserRepository;
import library.hieund.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    int code = 0;
    String msg = "";

    public Map<String, Object> addBook(Book book) {
	if (!bookRepository.existsByTitle(book.getTitle())) {
	    bookRepository.save(book);
	    code = 1;
	    msg = "Success";
	} else {
	    code = 0;
	    msg = "Book is already exists";
//	      throw new CustomException("Book is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
	}
	return getResponseJson();
    }

    public Map<String, Object> deleteById(int id) {

	Book book = bookRepository.findById(id);
	if (book != null) {
	    bookRepository.delete(book);
	    code = 1;
	    msg = "Success";
	} else {
	    code = 0;
	    msg = "Book not found";
	}
	return getResponseJson();
    }

    public Map<String, Object> deleteByTitleAndAuthor(String title, String author) {

	Book book = bookRepository.findByTitleAndAuthor(title, author);
	if (book != null) {
	    bookRepository.delete(book);
	    code = 1;
	    msg = "Success";
	} else {
	    code = 0;
	    msg = "Book not found";
	}
	return getResponseJson();
    }

    private Map<String, Object> getResponseJson() {
	Map<String, Object> map = new HashMap<>();
	map.put("code", code);
	map.put("message", msg);
	return map;

    }
}
