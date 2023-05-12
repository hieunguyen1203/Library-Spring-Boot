package library.hieund.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import library.hieund.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);

    User findByEmail(String email);

    @Transactional
    void deleteByUsername(String username);

}
