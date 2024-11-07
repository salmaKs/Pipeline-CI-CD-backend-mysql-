package tn.esprit.spring.entities;


import java.time.LocalDate;
import java.util.Set;

import javax.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE)
public class SkierDTO{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    Long numSkier;             
    String firstName;         
    String lastName;          
    LocalDate dateOfBirth;    
    String city;             
    Long subscriptionId;      
    Set<Long> pisteIds;       
    Set<Long> registrationIds;

}

