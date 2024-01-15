package com.pizza.model;

import com.pizza.model.dto.IngredientDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ingredients")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = true)
    private String allergens;

    public Ingredient() {

    }

    public Ingredient(String name, Integer stock, String allergens) {
        this.name = name;
        this.stock = stock;
        this.allergens = allergens;
    }

    public static Ingredient from(IngredientDto ingredientDto) {
        Ingredient ingredient = new Ingredient();

        ingredient.setName(ingredientDto.getName());
        ingredient.setStock(ingredientDto.getStock());
        ingredient.setAllergens(ingredientDto.getAllergens());

        return ingredient;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", stock=" + stock +
                ", allergens=" +
                '}';
    }
}