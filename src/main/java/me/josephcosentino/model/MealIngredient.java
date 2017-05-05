package me.josephcosentino.model;

import javax.persistence.*;

/**
 * Association class between Meal and Ingredient
 *
 * @author Joseph Cosentino.
 */
@Entity(name = "meals_ingredients")
@IdClass(MealIngredientId.class)
public class MealIngredient {

    @Id
    @ManyToOne
    @JoinColumn(name = "meal_id", referencedColumnName = "meal_id")
    private Meal meal;

    @Id
    @ManyToOne
    @JoinColumn(name = "ingredient_id", referencedColumnName = "ingredient_id")
    private Ingredient ingredient;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UOM uom;

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public UOM getUom() {
        return uom;
    }

    public void setUom(UOM uom) {
        this.uom = uom;
    }

    @Override
    public String toString() {
        return "MealIngredient{" +
                "meal=" + meal +
                ", ingredient=" + ingredient +
                ", quantity=" + quantity +
                ", uom=" + uom +
                '}';
    }
}
