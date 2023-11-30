package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.repositories.OwnerRepository;
import guru.springframework.sfgpetclinic.repositories.PetRepository;
import guru.springframework.sfgpetclinic.repositories.PetTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerSDJpaServiceTest {

    public static final String LAST_NAME = "Smith";
    @InjectMocks
    OwnerSDJpaService service;

    @Mock
    OwnerRepository ownerRepository;
    @Mock
    PetTypeRepository petTypeRepository;
    @Mock
    PetRepository petRepository;
    Owner returnOwner;
    @BeforeEach
    void setUp() {
        returnOwner = Owner.builder().lastName(LAST_NAME).build();
        returnOwner.setId(1L);
    }

    @Test
    void findAll() {
        Set<Owner> returnOwnersSet = new HashSet<>();
        returnOwnersSet.add(Owner.builder().build());
        returnOwnersSet.add(Owner.builder().build());

        when(ownerRepository.findAll()).thenReturn(returnOwnersSet);
        Set<Owner> owners = service.findAll();

        assertNotNull(owners);
        assertEquals(2,owners.size());
    }

    @Test
    void findById() {

        when(ownerRepository.findById(any())).thenReturn(Optional.of(returnOwner));

        Owner owner = service.findById(1L);

        assertNotNull(owner);
        assertEquals(1L,owner.getId());
    }
    @Test
    void findByIdNotFound() {

        when(ownerRepository.findById(any())).thenReturn(Optional.empty());

        Owner owner = service.findById(1L);

        assertNull(owner);
    }

    @Test
    void save() {
        Owner ownerToSave = Owner.builder().build();
        ownerToSave.setId(2L);
        when(ownerRepository.save(any())).thenReturn(ownerToSave);

        Owner owner = service.save(ownerToSave);

        assertNotNull(owner);
        assertEquals(2L,owner.getId());
    }

    @Test
    void delete() {
        service.delete(returnOwner);

        verify(ownerRepository,times(1)).delete(any());
    }

    @Test
    void deleteById() {
        service.deleteById(1L);

        verify(ownerRepository,times(1)).deleteById(any());
    }

    @Test
    void findByLastName() {

        when(ownerRepository.findByLastName(any())).thenReturn(returnOwner);
        Owner smith = service.findByLastName(LAST_NAME);

        assertEquals(LAST_NAME,smith.getLastName());
        verify(ownerRepository).findByLastName(any());
    }
}