package escuelaing.edu.co.Patrones.service;

import escuelaing.edu.co.Patrones.model.Property;
import escuelaing.edu.co.Patrones.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    // Crear una nueva propiedad
    public Property createProperty(Property property) {
        return propertyRepository.save(property);
    }

    // Obtener todas las propiedades
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    // Obtener una propiedad por ID
    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));
    }

    // Actualizar una propiedad existente
    public Property updateProperty(Long id, Property propertyDetails) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));

        property.setAddress(propertyDetails.getAddress());
        property.setPrice(propertyDetails.getPrice());
        property.setSize(propertyDetails.getSize());
        property.setDescription(propertyDetails.getDescription());

        return propertyRepository.save(property);
    }

    // Eliminar una propiedad
    public void deleteProperty(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));
        propertyRepository.delete(property);
    }
}

