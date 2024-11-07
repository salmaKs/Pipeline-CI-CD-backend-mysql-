package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.repositories.ISubscriptionRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.Collections;

@Slf4j
@AllArgsConstructor
@Service
public class SubscriptionServicesImpl implements ISubscriptionServices {

    private ISubscriptionRepository subscriptionRepository;

    private ISkierRepository skierRepository;

    @Override
    public Subscription addSubscription(Subscription subscription) {
        if (subscription != null && subscription.getStartDate() != null && subscription.getTypeSub() != null) {
            switch (subscription.getTypeSub()) {
                case ANNUAL:
                    subscription.setEndDate(subscription.getStartDate().plusYears(1));
                    break;
                case SEMESTRIEL:
                    subscription.setEndDate(subscription.getStartDate().plusMonths(6));
                    break;
                case MONTHLY:
                    subscription.setEndDate(subscription.getStartDate().plusMonths(1));
                    break;
            }
            return subscriptionRepository.save(subscription);
        }
        return null;  // Return null if subscription is invalid
    }

    @Override
    public Subscription updateSubscription(Subscription subscription) {
        if (subscription != null) {
            return subscriptionRepository.save(subscription);
        }
        return null;
    }

    @Override
    public Subscription retrieveSubscriptionById(Long numSubscription) {
        if (numSubscription != null) {
            return subscriptionRepository.findById(numSubscription).orElse(null);
        }
        return null;
    }

    @Override
    public Set<Subscription> getSubscriptionByType(TypeSubscription type) {
        if (type != null) {
            return subscriptionRepository.findByTypeSubOrderByStartDateAsc(type);
        }
         return Collections.emptySet();
    }

    @Override
    public List<Subscription> retrieveSubscriptionsByDates(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return subscriptionRepository.getSubscriptionsByStartDateBetween(startDate, endDate);
        }
         return Collections.emptyList();
    }

    @Override
    @Scheduled(cron = "*/30 * * * * *") /* Cron expression to run a job every 30 seconds */
    public void retrieveSubscriptions() {
        List<Subscription> subscriptions = subscriptionRepository.findDistinctOrderByEndDateAsc();
        if (subscriptions != null) {
            for (Subscription sub : subscriptions) {
                if (sub != null && sub.getNumSub() != null && sub.getEndDate() != null) {
                    Skier aSkier = skierRepository.findBySubscription(sub);
                    if (aSkier != null && aSkier.getFirstName() != null && aSkier.getLastName() != null) {
                        log.info(sub.getNumSub().toString() + " | " + sub.getEndDate().toString()
                                + " | " + aSkier.getFirstName() + " " + aSkier.getLastName());
                    } else {
                        log.warn("Skier or Skier's name is null for subscription: " + sub.getNumSub());
                    }
                } else {
                    log.warn("Subscription or its properties are null.");
                }
            }
        } else {
            log.warn("No subscriptions found.");
        }
    }

    //@Scheduled(cron = "* 0 9 1 * *") /* Cron expression to run a job every month at 9am */
    @Scheduled(cron = "*/30 * * * * *") /* Cron expression to run a job every 30 seconds */
    public void showMonthlyRecurringRevenue() {
        try {
            Float monthlyRevenue = 0f;
            Float monthly = subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY);
            Float semestriel = subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL);
            Float annual = subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL);

            if (monthly != null) monthlyRevenue += monthly;
            if (semestriel != null) monthlyRevenue += semestriel / 6;
            if (annual != null) monthlyRevenue += annual / 12;

            log.info("Monthly Revenue = " + monthlyRevenue);
        } catch (Exception e) {
            log.error("Error calculating monthly revenue: ", e);
        }
    }
}

