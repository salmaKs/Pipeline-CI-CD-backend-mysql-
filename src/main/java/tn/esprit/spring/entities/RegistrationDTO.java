package tn.esprit.spring.entities;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

public class RegistrationDTO{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    Long numRegistration; 
    int numWeek;         
    Long skierId;        
    Long courseId;       
}

