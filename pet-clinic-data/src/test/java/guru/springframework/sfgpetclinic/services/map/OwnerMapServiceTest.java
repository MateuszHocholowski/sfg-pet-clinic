package guru.springframework.sfgpetclinic.services.map;

import guru.springframework.sfgpetclinic.model.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OwnerMapServiceTest {

    OwnerMapService ownerMapService;
    final Long ownerId = 1L;
    final String LAST_NAME = "Smith";
    @BeforeEach
    public void setUp() {
        ownerMapService = new OwnerMapService(new PetTypeMapService(), new PetMapService());
        ownerMapService.save(Owner.builder().lastName(LAST_NAME).build()).setId(ownerId);

    }
    @Test
    void findAll() {
        Set<Owner> owners = ownerMapService.findAll();

        assertEquals(owners.size(),1);
    }

    @Test
    void deleteById() {
        ownerMapService.deleteById(ownerId);

        assertEquals(ownerMapService.findAll().size(),0);
    }

    @Test
    void delete() {
        ownerMapService.delete(ownerMapService.findById(ownerId));

        assertEquals(ownerMapService.findAll().size(),0);
    }

    @Test
    void saveExistingId() {
        long id = 2L;
        Owner owner2 = Owner.builder().build();
        owner2.setId(id);
        Owner savedOwner = ownerMapService.save(owner2);

        assertEquals(id,savedOwner.getId());
    }
    @Test
    void saveNoId() {
//        Owner owner2 = Owner.builder().build();
//        owner2.setId(2L);
        Owner savedOwner = ownerMapService.save(Owner.builder().build());

        assertNotNull(savedOwner);
        assertNotNull(savedOwner.getId());
    }

    @Test
    void findById() {
        Owner owner = ownerMapService.findById(ownerId);

        assertEquals(owner.getId(),ownerId);
    }

    @Test
    void findByLastName() {

        Owner smith = ownerMapService.findByLastName(LAST_NAME);

        assertNotNull(smith);
        assertEquals(ownerId,smith.getId());
    }
    @Test
    void findByLastNameNotFound() {

        Owner foo = ownerMapService.findByLastName("foo");

        assertNull(foo);
    }
}