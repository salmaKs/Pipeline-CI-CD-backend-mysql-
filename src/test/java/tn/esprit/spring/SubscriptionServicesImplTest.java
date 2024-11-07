package tn.esprit.spring;
import tn.esprit.spring.services.SubscriptionServicesImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.repositories.ISubscriptionRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class SubscriptionServicesImplTest {

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @Mock
    private ISkierRepository skierRepository;

    @InjectMocks
    private SubscriptionServicesImpl subscriptionServices;

    private Subscription subscription;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        subscription = new Subscription();
        subscription.setStartDate(LocalDate.of(2024, 1, 1));
        subscription.setTypeSub(TypeSubscription.ANNUAL);
    }

    @Test
    void testAddSubscription_WithValidSubscription() {
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        Subscription result = subscriptionServices.addSubscription(subscription);

        assertNotNull(result);
        assertEquals(LocalDate.of(2025, 1, 1), result.getEndDate());
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    void testAddSubscription_WithNullSubscription() {
        Subscription result = subscriptionServices.addSubscription(null);
        assertNull(result);
        verify(subscriptionRepository, never()).save(any(Subscription.class));
    }

    @Test
    void testUpdateSubscription_WithValidSubscription() {
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        Subscription result = subscriptionServices.updateSubscription(subscription);

        assertNotNull(result);
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    void testRetrieveSubscriptionById_WithExistingId() {
        when(subscriptionRepository.findById(anyLong())).thenReturn(Optional.of(subscription));

        Subscription result = subscriptionServices.retrieveSubscriptionById(1L);

        assertNotNull(result);
        verify(subscriptionRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrieveSubscriptionById_WithNonExistingId() {
        when(subscriptionRepository.findById(anyLong())).thenReturn(Optional.empty());

        Subscription result = subscriptionServices.retrieveSubscriptionById(1L);

        assertNull(result);
        verify(subscriptionRepository, times(1)).findById(1L);
    }

    @Test
    void testGetSubscriptionByType_WithValidType() {
        Set<Subscription> subscriptions = Set.of(subscription);
        when(subscriptionRepository.findByTypeSubOrderByStartDateAsc(TypeSubscription.ANNUAL)).thenReturn(subscriptions);

        Set<Subscription> result = subscriptionServices.getSubscriptionByType(TypeSubscription.ANNUAL);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(subscriptionRepository, times(1)).findByTypeSubOrderByStartDateAsc(TypeSubscription.ANNUAL);
    }

    @Test
    void testRetrieveSubscriptionsByDates_WithValidDates() {
        List<Subscription> subscriptions = List.of(subscription);
        when(subscriptionRepository.getSubscriptionsByStartDateBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(subscriptions);

        List<Subscription> result = subscriptionServices.retrieveSubscriptionsByDates(LocalDate.of(2023, 1, 1), LocalDate.of(2024, 1, 1));

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(subscriptionRepository, times(1)).getSubscriptionsByStartDateBetween(any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void testRetrieveSubscriptions_WithValidSubscriptions() {
        Skier skier = new Skier();
        skier.setFirstName("John");
        skier.setLastName("Doe");

        subscription.setNumSub(1L);
        subscription.setEndDate(LocalDate.of(2024, 12, 31));

        when(subscriptionRepository.findDistinctOrderByEndDateAsc()).thenReturn(List.of(subscription));
        when(skierRepository.findBySubscription(any(Subscription.class))).thenReturn(skier);

        subscriptionServices.retrieveSubscriptions();

        verify(subscriptionRepository, times(1)).findDistinctOrderByEndDateAsc();
        verify(skierRepository, times(1)).findBySubscription(subscription);
    }

    @Test
    void testShowMonthlyRecurringRevenue() {
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY)).thenReturn(100f);
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL)).thenReturn(600f);
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL)).thenReturn(1200f);

        subscriptionServices.showMonthlyRecurringRevenue();

       
        verify(subscriptionRepository, times(3)).recurringRevenueByTypeSubEquals(any(TypeSubscription.class));
    }
}


