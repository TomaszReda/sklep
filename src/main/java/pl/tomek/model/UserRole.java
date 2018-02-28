package pl.tomek.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class UserRole {

    @Id
    @GeneratedValue
    private Long id;
    private String role;
    private String description;


}
