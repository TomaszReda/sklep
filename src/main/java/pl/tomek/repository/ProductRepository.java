package pl.tomek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.tomek.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {


}
