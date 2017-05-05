package me.josephcosentino.model;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Association class between User and Meal
 *
 * @author Joseph Cosentino.
 */
@Entity(name = "planned_meals")
@IdClass(UserMealId.class)
public class PlannedMeal {

    @Id
    @ManyToOne
    @JoinColumn(name = "meal_id", referencedColumnName = "meal_id")
    private Meal meal;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(nullable = false)
    private Boolean completed;

    @Column(nullable = false)
    private LocalDate date;

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "PlannedMeal{" +
                "meal=" + meal +
                ", user=" + user +
                ", completed=" + completed +
                ", date=" + date +
                '}';
    }
}
