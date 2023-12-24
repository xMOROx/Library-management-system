package pl.edu.agh.managementlibrarysystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.managementlibrarysystem.model.IssuedBook;
import pl.edu.agh.managementlibrarysystem.model.keys.IssuedBooksKey;

import java.util.List;

@Repository
public interface IssuedBooksRepository extends JpaRepository<IssuedBook, IssuedBooksKey> {
    void updateFee();

    List<IssuedBook> findByUserId(Long id);

    void issueBook(String isbn, Long id, Integer days);
}
