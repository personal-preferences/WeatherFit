package org.personal.weatherfit.aggregate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "outfit")
public class Outfit {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "outfit_date")
    private String outfitDate;

    @Column(name = "male_top")
    private String maleTop;
    @Column(name = "male_bottom")
    private String maleBottom;
    @Column(name = "male_outer")
    private String maleOuter;
    @Column(name = "male_shoes")
    private String maleShoes;

    @Column(name = "female_top")
    private String femaleTop;
    @Column(name = "female_bottom")
    private String femaleBottom;
    @Column(name = "female_outer")
    private String femaleOuter;
    @Column(name = "female_shoes")
    private String femaleShoes;

}
