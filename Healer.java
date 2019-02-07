package aa.project;
import java.util.*;


import aa.project.supers.GameCharacter;

public class Healer extends GameCharacter {

    // Constructor for Healers
    public Healer(int health, int attackPower, int mana, int teamNumber) {
        super(health, attackPower, mana,teamNumber);
    }

    // Increases Mana of Single Player of the SAME team
    @Override
    public void attack(GameCharacter other) {
        other.increaseMana(attackPower);
        this.useMana(1);
    }
    
    // Increases Health of Every Player (Excluding Him/herself) of the SAME team
    @Override
    public void specialAttack(List<GameCharacter> others) {
        for(GameCharacter gc : others) {
            gc.increaseHealth(attackPower);
        }
        this.useMana(2);
    }

    // String Representation of Healer
    @Override
    public String toString() {
        return "Team " + (teamNumber + 1) + " Healer";
    }
}