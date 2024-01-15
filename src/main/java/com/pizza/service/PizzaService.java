package com.pizza.service;

import com.pizza.model.Pizza;
import com.pizza.model.dto.PizzaDto;
import com.pizza.model.dto.PizzaDtoWithIngredients;
import com.pizza.repository.PizzaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PizzaService {

    private final PizzaRepository pizzaRepository;

    @Autowired
    public PizzaService(PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
    }

    public List<PizzaDto> getAllPizzas() {
        return pizzaRepository.findAll().stream().map(PizzaDto::from).collect(Collectors.toList());
    }

    public List<PizzaDtoWithIngredients> getAllPizzasWithIngredients() {
        return pizzaRepository.findAll().stream().map(PizzaDtoWithIngredients::from).collect(Collectors.toList());
    }

    public Optional<PizzaDto> getPizzaById(Long id) {
        return pizzaRepository.findById(id).map(PizzaDto::from);
    }
    public Optional<PizzaDtoWithIngredients> getPizzaWithIngredientsById(Long id) {
        return pizzaRepository.findById(id).map(PizzaDtoWithIngredients::from);
    }

    public PizzaDto savePizza(PizzaDto pizzaDto) {
        return PizzaDto.from(pizzaRepository.save(Pizza.from(pizzaDto)));
    }

    public boolean deletePizzaById(Long id) {
        return pizzaRepository.findById(id)
                .map(pizza -> {
                    pizzaRepository.delete(pizza);
                    return true;
                }).orElse(false);
    }


    @Transactional
    public Optional<PizzaDto> updatePizza(Long id, PizzaDto newPizzaDto) {
        return pizzaRepository.findById(id)
                .map(pizzaToUpdate -> {
                    pizzaToUpdate.setName(newPizzaDto.getName());
                    pizzaToUpdate.setAllergens(newPizzaDto.getAllergens());
                    pizzaToUpdate.setPrice(newPizzaDto.getPrice());
                    pizzaToUpdate.setBlatType(newPizzaDto.getBlatType());
                    pizzaToUpdate.setBlatQuantity(newPizzaDto.getBlatQuantity());
                    pizzaToUpdate.setBaseName(newPizzaDto.getBaseName());
                    pizzaToUpdate.setBaseQuantity(newPizzaDto.getBaseQuantity());

                    return PizzaDto.from(pizzaToUpdate);
                });
    }
}
