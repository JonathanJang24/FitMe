package fitme.client;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FoodData {

    private final StringProperty entry_id;
    private final StringProperty name;
    private final StringProperty servings;
    private final StringProperty calories;
    private final StringProperty protein;
    private final StringProperty fibers;
    private final StringProperty carbohydrates;
    private final StringProperty fats;

    public FoodData(String entry_id, String name, String serv, String cals, String pro, String fib, String carbs, String fats){
        this.entry_id = new SimpleStringProperty(entry_id);
        this.name = new SimpleStringProperty(name);
        this.servings = new SimpleStringProperty(serv);
        this.calories = new SimpleStringProperty(cals);
        this.protein = new SimpleStringProperty(pro);
        this.fibers = new SimpleStringProperty(fib);
        this.carbohydrates = new SimpleStringProperty(carbs);
        this.fats = new SimpleStringProperty(fats);
    }

    public String getEntry_id(){
        return entry_id.get();
    }

    public void setEntry_id(String id){
        this.entry_id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getServings(){
        return servings.get();
    }

    public void setServings(String servings){
        this.servings.set(servings);
    }

    public String getCalories() {
        return calories.get();
    }

    public void setCalories(String calories) {
        this.calories.set(calories);
    }

    public String getProtein() {
        return protein.get();
    }

    public void setProtein(String protein) {
        this.protein.set(protein);
    }

    public String getFibers() {
        return fibers.get();
    }

    public void setFibers(String fibers) {
        this.fibers.set(fibers);
    }

    public String getCarbohydrates() {
        return carbohydrates.get();
    }

    public void setCarbohydrates(String carbohydrates) {
        this.carbohydrates.set(carbohydrates);
    }

    public String getFats() {
        return fats.get();
    }

    public void setFats(String fats) {
        this.fats.set(fats);
    }



}
