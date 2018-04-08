package pl.tomek.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;


    @NotEmpty(message = "{pl.tomek.model.Product.empty}")
    private String name;
    @NotEmpty(message = "{pl.tomek.model.Product.empty}")
    private String state;
    @Length(min = 1,max = 80,message ="{pl.tomek.model.Product.header}")
    private String header;
    private String kategoria;
    @Min(value = 1,message = "{pl.tomek.model.Product.min}")
    private double prcies;
    @NotEmpty(message = "{pl.tomek.model.Product.empty}")
    private String opis;
    @NotEmpty(message = "{pl.tomek.model.Product.empty}")
    private String aukcja;
    private String owner;

    private String licytujacy;





    @OneToMany(cascade = {CascadeType.PERSIST},fetch = FetchType.EAGER)
    private List<Zdjecia> zdjecia=new ArrayList<>();




}
