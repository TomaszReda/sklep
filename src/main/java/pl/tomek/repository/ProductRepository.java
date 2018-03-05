package pl.tomek.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.tomek.model.Product;

import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findByOwner(String owner,Pageable pageRequest);
    Set<Product> findByOwner(String owner);

}
