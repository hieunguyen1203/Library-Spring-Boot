package library.hieund.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import library.hieund.model.User;
import library.hieund.model.Book;

public interface BookRepository extends JpaRepository<Book, Integer>{
//	Books findBooks(String title);
	boolean existsByTitle(String title);
}
