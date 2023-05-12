package library.hieund.service;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
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

    public String addBook(Book book) {
	if (bookRepository.existsByTitle(book.getTitle()) && bookRepository.existsByAuthor(book.getAuthor())) {
	    return returnError("Book is already exists");

	}
	bookRepository.save(book);
	return returnSuccess();

    }

    public String deleteById(int id) {

	Book book = bookRepository.findById(id);
	if (book != null) {
	    bookRepository.delete(book);
	    return returnSuccess();

	} else {
	    return returnError("Book not found");

	}

    }

    public String deleteByTitleAndAuthor(String title, String author) {

	Book book = bookRepository.findByTitleAndAuthor(title, author);
	if (book != null) {
	    bookRepository.delete(book);
	    return returnSuccess();

	} else {
	    return returnError("Book not found");

	}

    }

    private String returnSuccess() {
	JSONObject jsonObject = new JSONObject();
	jsonObject.put("success", true);
	return jsonObject.toString();

    }

    private String returnError(String msg) {
	JSONObject jsonObject = new JSONObject();
	JSONObject errors = new JSONObject();
	errors.put("message", msg);
	jsonObject.put("error", errors);
	return jsonObject.toString();
    }
}
