package tn.esprit.spring;
import tn.esprit.spring.services.PisteServicesImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.repositories.IPisteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


 class PisteServicesImplTest {

    @InjectMocks
    private PisteServicesImpl pisteServices;

    @Mock
    private IPisteRepository pisteRepository;

    private Piste piste;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        piste = new Piste();
        piste.setNumPiste(1L);
        // Initialize other properties of the piste as needed
    }

    @Test
     void testRetrieveAllPistes() {
        List<Piste> pistes = new ArrayList<>();
        pistes.add(piste);
        
        when(pisteRepository.findAll()).thenReturn(pistes);

        List<Piste> result = pisteServices.retrieveAllPistes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(piste.getNumPiste(), result.get(0).getNumPiste());
        verify(pisteRepository, times(1)).findAll();
    }

    @Test
    void testAddPiste() {
        when(pisteRepository.save(piste)).thenReturn(piste);

        Piste result = pisteServices.addPiste(piste);

        assertNotNull(result);
        assertEquals(piste.getNumPiste(), result.getNumPiste());
        verify(pisteRepository, times(1)).save(piste);
    }

    @Test
    void testRemovePiste() {
        pisteServices.removePiste(1L);
        verify(pisteRepository, times(1)).deleteById(1L);
    }

    @Test
    void testRetrievePiste() {
        when(pisteRepository.findById(1L)).thenReturn(Optional.of(piste));

        Piste result = pisteServices.retrievePiste(1L);

        assertNotNull(result);
        assertEquals(piste.getNumPiste(), result.getNumPiste());
        verify(pisteRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrievePisteNotFound() {
        when(pisteRepository.findById(1L)).thenReturn(Optional.empty());

        Piste result = pisteServices.retrievePiste(1L);

        assertNull(result);
        verify(pisteRepository, times(1)).findById(1L);
    }
}

