package pl.tomek.model;

import javafx.scene.image.Image;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;

@Entity
@Data
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @NotEmpty(message = "{pl.tomek.model.Product.empty}")
    private String name;
    @NotEmpty(message = "{pl.tomek.model.Product.empty}")
    private String state;
    private String kategoria;
    @Min(value = 1,message = "{pl.tomek.model.Product.min}")
    private double prcies;
    @NotEmpty(message = "{pl.tomek.model.Product.empty}")
    private String opis;
    @NotEmpty(message = "{pl.tomek.model.Product.empty}")
    private String aukcja;
    private String owner;

    private double cenapoczatkowa;



}
