package com.pizza.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pizza.model.dto.PizzaDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "pizzas")
public class Pizza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String allergens;

    @Column(nullable = false)
    private float price;

    @Column(nullable = false)
    private String blatType;

    @Column(nullable = false)
    private Integer blatQuantity;

    @Column(nullable = false)
    private String baseName;

    @Column(nullable = false)
    private Integer baseQuantity;

    @OneToMany(mappedBy = "pizza", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<PizzaIngredient> pizzaIngredientSet;

    public Pizza() {
    }

    public Pizza(String name, String allergens, float price, String blatType, Integer blatQuantity, String baseName, Integer baseQuantity, Map<Long, Integer> ingredients) {
        this.name = name;
        this.allergens = allergens;
        this.price = price;
        this.blatType = blatType;
        this.blatQuantity = blatQuantity;
        this.baseName = baseName;
        this.baseQuantity = baseQuantity;
    }

    public static Pizza from(PizzaDto pizzaDto){
        Pizza pizza=new Pizza();
        pizza.setName(pizzaDto.getName());
        pizza.setAllergens(pizzaDto.getAllergens());
        pizza.setPrice(pizzaDto.getPrice());
        pizza.setBlatType(pizzaDto.getBlatType());
        pizza.setBlatQuantity(pizzaDto.getBlatQuantity());
        pizza.setBaseName(pizzaDto.getBaseName());
        pizza.setBaseQuantity(pizzaDto.getBaseQuantity());
        pizza.setPizzaIngredientSet(new HashSet<>());

        return pizza;
    }

    @Override
    public String toString() {
        return "Pizza{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", allergens='" + allergens + '\'' +
                ", price=" + price +
                ", blatType='" + blatType + '\'' +
                ", blatQuantity=" + blatQuantity +
                ", baseName='" + baseName + '\'' +
                ", baseQuantity=" + baseQuantity +
                ", pizzaIngredientSet=" + pizzaIngredientSet +
                '}';
    }
}
