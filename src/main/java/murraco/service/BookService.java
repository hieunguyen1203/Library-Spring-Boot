package murraco.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import murraco.exception.CustomException;
import murraco.model.AppUser;
import murraco.model.Book;
import murraco.repository.BookRepository;
import murraco.repository.UserRepository;
import murraco.security.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class BookService {
	
	private final BookRepository bookRepository;
	
	public String test() {
		  return "testoi";
	  }
	  
	
	public String addBook(Book book) {
	    if (!bookRepository.existsByTitle(book.getTitle())) {
	      bookRepository.save(book);
	      
	      return "Success";
	    } else {
	    	return "ERROR";
//	      throw new CustomException("Book is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
	    }
	  }
}
