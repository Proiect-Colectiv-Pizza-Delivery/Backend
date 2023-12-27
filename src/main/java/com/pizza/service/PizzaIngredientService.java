package com.pizza.service;

import com.pizza.model.Ingredient;
import com.pizza.model.Pizza;
import com.pizza.model.PizzaIngredient;
import com.pizza.model.PizzaIngredientKey;
import com.pizza.model.dto.IngredientWithQuantity;
import com.pizza.model.dto.PizzaIngredientDto;
import com.pizza.model.dto.PizzaIngredientList;
import com.pizza.model.dto.PizzaIngredientWithQuantityList;
import com.pizza.repository.IngredientRepository;
import com.pizza.repository.PizzaIngredientRepository;
import com.pizza.repository.PizzaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PizzaIngredientService {

    private final PizzaRepository pizzaRepository;
    private final IngredientRepository ingredientRepository;
    private final PizzaIngredientRepository pizzaIngredientRepository;

    @Autowired
    public PizzaIngredientService(PizzaRepository pizzaRepository, IngredientRepository ingredientRepository, PizzaIngredientRepository pizzaIngredientRepository) {
        this.pizzaRepository = pizzaRepository;
        this.ingredientRepository = ingredientRepository;
        this.pizzaIngredientRepository = pizzaIngredientRepository;
    }

    @Transactional
    public PizzaIngredientDto updateIngredientInPizza(Integer quantity, Long idPizza, Long idIngredient) {
        Pizza pizza = pizzaRepository.findById(idPizza).orElseThrow();
        Ingredient ingredient = ingredientRepository.findById(idIngredient).orElseThrow();
        PizzaIngredientKey key = new PizzaIngredientKey(idPizza, idIngredient);

        PizzaIngredient pizzaIngredient = pizzaIngredientRepository.findById(key)
                .orElseGet(() -> new PizzaIngredient(key, pizza, ingredient, 0));

        int newQuantity = pizzaIngredient.getQuantity() + quantity;
        pizzaIngredient.setQuantity(newQuantity);

        if (newQuantity <= 0) {
            removeIngredientFromPizza(idPizza, idIngredient);
        } else {
            PizzaIngredient savedPizzaIngredient = pizzaIngredientRepository.save(pizzaIngredient);
            if (newQuantity == quantity) { // New entity was created
                pizza.getPizzaIngredientSet().add(savedPizzaIngredient);
                addPizzaAllergens(pizza, ingredient);
            }
            return PizzaIngredientDto.from(savedPizzaIngredient, idPizza, idIngredient);
        }

        return PizzaIngredientDto.from(pizzaIngredient, idPizza, idIngredient);
    }


    @Transactional
    public void removeIngredientFromPizza(Long idPizza, Long idIngredient) {
        Pizza pizza = pizzaRepository.findById(idPizza).orElseThrow();
        Ingredient ingredient = ingredientRepository.findById(idIngredient).orElseThrow();

        pizzaIngredientRepository.findByPizzaIdAndIngredientId(idPizza, idIngredient)
                .ifPresent(pizzaIngredient -> {
                    pizzaIngredientRepository.delete(pizzaIngredient);
                    removePizzaAllergen(pizza, ingredient);
                });
    }


    public PizzaIngredientWithQuantityList updatePizzaIngredients(PizzaIngredientWithQuantityList pizzaIngredientWithQuantityList) {
        PizzaIngredientWithQuantityList returnedPizzaIngredientsList = new PizzaIngredientWithQuantityList();
        returnedPizzaIngredientsList.setIngredientsList(new ArrayList<>());

        Long pizzaId = pizzaIngredientWithQuantityList.getPizzaId();
        List<IngredientWithQuantity> ingredientList = pizzaIngredientWithQuantityList.getIngredientsList();

        for (IngredientWithQuantity ingredient : ingredientList) {
            PizzaIngredientDto pizzaIngredientDto = this.updateIngredientInPizza(ingredient.getQuantity(), pizzaId, ingredient.getIngredientId());
            IngredientWithQuantity ingredientWithQuantity = new IngredientWithQuantity(pizzaIngredientDto.getIngredientId(), pizzaIngredientDto.getQuantity());
            returnedPizzaIngredientsList.getIngredientsList().add(ingredientWithQuantity);
        }
        returnedPizzaIngredientsList.setPizzaId(pizzaId);
        return returnedPizzaIngredientsList;
    }

    public void removePizzaIngredients(PizzaIngredientList pizzaIngredientList) {
        Long pizzaId = pizzaIngredientList.getPizzaId();
        pizzaIngredientList.getIngredientsList().forEach(ingredient -> removeIngredientFromPizza(pizzaId, ingredient));
    }

    private void addPizzaAllergens(Pizza pizza, Ingredient ingredient) {
        String ingredientAllergen = ingredient.getAllergens();
        if (ingredientAllergen != null) {
            String currentAllergens = pizza.getAllergens();
            if (!currentAllergens.isEmpty()) {
                currentAllergens += ", ";
            }
            currentAllergens += ingredientAllergen;
            pizza.setAllergens(currentAllergens);
            pizzaRepository.save(pizza);
        }
    }


    private void removePizzaAllergen(Pizza pizza, Ingredient ingredient) {
        String ingredientAllergen = ingredient.getAllergens();
        if (ingredientAllergen != null) {
            String allergens = pizza.getAllergens();
            int index = allergens.indexOf(ingredientAllergen);
            if (index != -1) {
                String before = allergens.substring(0, index);
                String after = allergens.substring(index + ingredientAllergen.length());
                pizza.setAllergens(before + after.replaceAll("^,\\s*|,\\s*$", ""));
                pizzaRepository.save(pizza);
            }
        }
    }


}

