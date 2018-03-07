package pl.tomek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.tomek.model.Zdjecia;

@Repository
public interface ZdjeciaRepositoru extends JpaRepository<Zdjecia,Long> {
}
