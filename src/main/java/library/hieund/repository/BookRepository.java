package library.hieund.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import library.hieund.model.User;
import library.hieund.model.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {

    boolean existsByTitle(String title);

    boolean existsByAuthor(String author);

    @Transactional
    void deleteById(int id);

    Book findByTitleAndAuthor(String title, String author);

    Book findById(int id);

}
