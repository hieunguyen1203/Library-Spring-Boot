package library.hieund.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import library.hieund.exception.CustomException;
import library.hieund.model.AppUser;
import library.hieund.model.Book;
import library.hieund.repository.BookRepository;
import library.hieund.repository.UserRepository;
import library.hieund.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

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
