package tn.esprit.spring.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PisteDTO {
    private Long numPiste;
    private String namePiste;
    private Color color;
    private int length;
    private int slope;
    private Set<Long> skierIds;
}

