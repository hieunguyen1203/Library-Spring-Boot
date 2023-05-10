package murraco.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import murraco.model.AppUser;
import murraco.model.Book;

public interface BookRepository extends JpaRepository<Book, Integer>{
//	Books findBooks(String title);
	boolean existsByTitle(String title);
}
