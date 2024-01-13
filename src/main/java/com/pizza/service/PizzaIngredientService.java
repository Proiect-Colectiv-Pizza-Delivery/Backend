package com.pizza.service;

import com.pizza.exception.CustomException;
import com.pizza.model.Ingredient;
import com.pizza.model.Pizza;
import com.pizza.model.PizzaIngredient;
import com.pizza.model.PizzaIngredientKey;
import com.pizza.model.dto.*;
import com.pizza.repository.IngredientRepository;
import com.pizza.repository.PizzaIngredientRepository;
import com.pizza.repository.PizzaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional(rollbackOn = CustomException.class)
    public PizzaIngredientIdsWithQuantityList updatePizzaIngredients(PizzaIngredientIdsWithQuantityList pizzaIngredientIdsWithQuantityList) throws CustomException {
        Long pizzaId = pizzaIngredientIdsWithQuantityList.getPizzaId();
        List<IngredientIdWithQuantity> ingredientList = pizzaIngredientIdsWithQuantityList.getIngredientsIdList();

        for (IngredientIdWithQuantity ingredient : ingredientList) {
            updateIngredientInPizza(ingredient.getQuantity(), pizzaId, ingredient.getIngredientId());
        }

        Pizza updatedPizzaWithIngredients = pizzaRepository.findById(pizzaId).orElseThrow(() -> new CustomException("Pizza with id " + pizzaId + " not found"));

        PizzaIngredientIdsWithQuantityList response = new PizzaIngredientIdsWithQuantityList();
        response.setPizzaId(pizzaId);
        response.setIngredientsIdList(updatedPizzaWithIngredients.getPizzaIngredientSet().stream().map(IngredientIdWithQuantity::from).collect(Collectors.toList()));

        return response;

    }

    public PizzaIngredientDto updateIngredientInPizza(Integer quantity, Long idPizza, Long idIngredient) throws CustomException {
        Pizza pizza = pizzaRepository.findById(idPizza).orElseThrow(() -> new CustomException("Pizza with id " + idPizza + " not found"));
        Ingredient ingredient = ingredientRepository.findById(idIngredient).orElseThrow(() -> new CustomException("Ingredient with id " + idIngredient + " not found"));
        PizzaIngredientKey key = new PizzaIngredientKey(idPizza, idIngredient);

        PizzaIngredient pizzaIngredient = pizzaIngredientRepository.findById(key)
                .orElseGet(() -> new PizzaIngredient(key, pizza, ingredient, 0));

        if (quantity == 0) { //no change
            return PizzaIngredientDto.from(pizzaIngredient, idPizza, idIngredient);
        }

        int newQuantity = pizzaIngredient.getQuantity() + quantity;
        pizzaIngredient.setQuantity(newQuantity);

        if (newQuantity <= 0) { //ingredient removed
            removeIngredientFromPizza(idPizza, idIngredient);
        } else {
            PizzaIngredient savedPizzaIngredient = pizzaIngredientRepository.save(pizzaIngredient);
            if (newQuantity == quantity) { //new ingredient
                pizza.getPizzaIngredientSet().add(savedPizzaIngredient);
                addPizzaAllergens(pizza, ingredient);
            }
            return PizzaIngredientDto.from(savedPizzaIngredient, idPizza, idIngredient);
        }

        return PizzaIngredientDto.from(pizzaIngredient, idPizza, idIngredient);
    }

    public PizzaIngredientWithQuantityList removePizzaIngredientsAndReturn(PizzaIngredientList pizzaIngredientList) throws CustomException {

        Long pizzaId = pizzaIngredientList.getPizzaId();
        pizzaRepository.findById(pizzaId).orElseThrow(() -> new CustomException("Pizza with id " + pizzaId + " not found"));

        removePizzaIngredients(pizzaId, pizzaIngredientList);

        Pizza updatedPizzaWithIngredients = pizzaRepository.findById(pizzaId).orElseThrow(() -> new CustomException("Pizza with id " + pizzaId + " not found"));

        PizzaIngredientWithQuantityList response = new PizzaIngredientWithQuantityList();
        response.setPizzaId(pizzaId);
        response.setIngredientsList(updatedPizzaWithIngredients.getPizzaIngredientSet().stream().map(IngredientWithQuantity::from).collect(Collectors.toList()));

        return response;
    }

    @Transactional(rollbackOn = CustomException.class)
    public void removePizzaIngredients(Long pizzaId, PizzaIngredientList pizzaIngredientList) throws CustomException {
        for (Long ingredient : pizzaIngredientList.getIngredientsList()) {
            removeIngredientFromPizza(pizzaId, ingredient);
        }
    }

    public void removeIngredientFromPizza(Long idPizza, Long idIngredient) throws CustomException {
        Pizza pizza = pizzaRepository.findById(idPizza).orElseThrow(() -> new CustomException("Pizza with id " + idPizza + " not found"));
        Ingredient ingredient = ingredientRepository.findById(idIngredient).orElseThrow(() -> new CustomException("Ingredient with id " + idIngredient + " not found"));

        pizzaIngredientRepository.findByPizzaIdAndIngredientId(idPizza, idIngredient)
                .ifPresent(pizzaIngredient -> {
                    pizzaIngredientRepository.delete(pizzaIngredient);
                    removePizzaAllergen(pizza, ingredient);
                });
    }

    private void addPizzaAllergens(Pizza pizza, Ingredient ingredient) {
        String ingredientAllergens = ingredient.getAllergens();
        if (ingredientAllergens != null && !ingredientAllergens.isEmpty()) {
            List<String> pizzaAllergensList = new ArrayList<>(Arrays.asList(pizza.getAllergens().split(",\\s*")));
            List<String> ingredientAllergensList = Arrays.asList(ingredientAllergens.split(",\\s*"));

            for (String allergen : ingredientAllergensList) {
                pizzaAllergensList.add(allergen);
            }
            String updatedAllergens = String.join(", ", pizzaAllergensList);
            pizza.setAllergens(updatedAllergens);
            pizzaRepository.save(pizza);
        }
    }

    private void removePizzaAllergen(Pizza pizza, Ingredient ingredient) {
        String ingredientAllergens = ingredient.getAllergens();
        if (ingredientAllergens != null && !ingredientAllergens.isEmpty()) {
            List<String> pizzaAllergensList = new ArrayList<>(Arrays.asList(pizza.getAllergens().split(",\\s*")));
            List<String> ingredientAllergensList = Arrays.asList(ingredientAllergens.split(",\\s*"));

            boolean modified = false;
            for (String allergen : ingredientAllergensList) {
                modified |= pizzaAllergensList.remove(allergen);
            }

            if (modified) {
                String updatedAllergens = String.join(", ", pizzaAllergensList);
                pizza.setAllergens(updatedAllergens);
                pizzaRepository.save(pizza);
            }
        }
    }


}

