package com.pizza.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pizza.model.dto.PizzaIngredientDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "pizza_ingredients")
public class PizzaIngredient {

    @EmbeddedId
    private PizzaIngredientKey id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId("pizzaId")
    @JoinColumn(name = "pizza_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private Pizza pizza;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private Ingredient ingredient;

    private Integer quantity;

    public PizzaIngredient() {
    }

    public PizzaIngredient(PizzaIngredientKey id, Pizza pizza, Ingredient ingredient, Integer quantity) {
        this.id = id;
        this.pizza = pizza;
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    public static PizzaIngredient from(PizzaIngredientDto pizzaIngredientDto) {
        PizzaIngredient pizzaIngredient = new PizzaIngredient();
        pizzaIngredient.setQuantity(pizzaIngredientDto.getQuantity());
        return pizzaIngredient;
    }

}

