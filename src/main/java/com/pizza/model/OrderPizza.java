package com.pizza.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "order_pizza")
public class OrderPizza {

    @EmbeddedId
    private OrderPizzaKey id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private Order order;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId("pizzaId")
    @JoinColumn(name = "pizza_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private Pizza pizza;

    private Integer quantity;
}