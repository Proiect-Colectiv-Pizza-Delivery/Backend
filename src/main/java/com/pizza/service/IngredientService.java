package com.pizza.service;

import com.pizza.model.Ingredient;
import com.pizza.model.dto.IngredientDto;
import com.pizza.repository.IngredientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<IngredientDto> getAllIngredients() {
        return ingredientRepository.findAll().stream().map(IngredientDto::from).collect(Collectors.toList());
    }

    public Optional<IngredientDto> getIngredientById(Long id) {
        return ingredientRepository.findById(id).map(IngredientDto::from);
    }

    public IngredientDto saveIngredient(IngredientDto ingredientDto) {
        return IngredientDto.from(ingredientRepository.save(Ingredient.from(ingredientDto)));
    }

    public boolean deleteIngredientById(Long id) {
        return ingredientRepository.findById(id)
                .map(ingredient -> {
                    ingredientRepository.delete(ingredient);
                    return true;
                }).orElse(false);
    }

    @Transactional
    public Optional<IngredientDto> updateIngredient(Long id, IngredientDto newIngredientDto) {
        return ingredientRepository.findById(id)
                .map(ingredientToUpdate -> {
                    ingredientToUpdate.setName(newIngredientDto.getName());
                    ingredientToUpdate.setStock(newIngredientDto.getStock());
                    ingredientToUpdate.setAllergens(newIngredientDto.getAllergens());

                    return IngredientDto.from(ingredientToUpdate);
                });
    }

}
