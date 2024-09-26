package escuelaing.edu.co.Patrones.repository;


import escuelaing.edu.co.Patrones.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
}
