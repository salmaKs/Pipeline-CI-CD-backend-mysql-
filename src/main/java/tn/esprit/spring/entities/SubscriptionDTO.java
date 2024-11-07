package tn.esprit.spring.entities;


import java.time.LocalDate;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE)

public class SubscriptionDTO{
@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    Long numSub; 
    LocalDate startDate; 
    LocalDate endDate; 
    Float price;
    @Enumerated(EnumType.STRING)
	TypeSubscription typeSub;

}

