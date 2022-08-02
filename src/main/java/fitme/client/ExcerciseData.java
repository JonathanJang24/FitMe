package fitme.client;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ExcerciseData {

    private final StringProperty id;
    private final StringProperty excercise;
    private final StringProperty time;
    private final StringProperty cals_burned;
    private final StringProperty sets;
    private final StringProperty reps;
    private final StringProperty weight;

    public ExcerciseData(String id, String excercise, String time, String cals_burned, String sets, String reps, String weight){
        this.id = new SimpleStringProperty(id);
        this.excercise = new SimpleStringProperty(excercise);
        this.time = new SimpleStringProperty(time);
        this.cals_burned = new SimpleStringProperty(cals_burned);
        this.sets = new SimpleStringProperty(sets);
        this.reps = new SimpleStringProperty(reps);
        this.weight = new SimpleStringProperty(weight);
    }

    public String getId(){
        return id.get();
    }

    public void setId(String id){
        this.id.set(id);
    }

    public String getExcercise() {
        return excercise.get();
    }

    public void setExcercise(String excercise) {
        this.excercise.set(excercise);
    }

    public String getTime() {
        return time.get();
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public String getCals_burned() {
        return cals_burned.get();
    }

    public void setCals_burned(String cals_burned) {
        this.cals_burned.set(cals_burned);
    }

    public String getSets() {
        return sets.get();
    }

    public void setSets(String sets) {
        this.sets.set(sets);
    }

    public String getReps() {
        return reps.get();
    }

    public void setReps(String reps) {
        this.reps.set(reps);
    }

    public String getWeight() {
        return weight.get();
    }

    public void setWeight(String weight) {
        this.weight.set(weight);
    }
}
