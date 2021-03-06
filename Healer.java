package aa.project.character;

import java.util.*;
import aa.project.supers.GameCharacter;

public class Healer extends GameCharacter {
    private int healthIncrease;

    // Constructor for Healers
    public Healer(int health, int attackPower, int mana, int teamNumber) {
        super(health, attackPower, mana,teamNumber);
        this.healthIncrease = 5;
    }

    // Increases Mana of Single Player of the SAME team
    @Override
    public void attack(GameCharacter other) {
        other.increaseMana(this.getAttackPower());
        this.useMana(1);
    }
    
    // Increases Health of Every Player of the SAME team
    @Override
    public void specialAttack(List<GameCharacter> others) {
        for(GameCharacter gc : others) {
            gc.increaseHealth(healthIncrease);
        }
        this.useMana(2);
    }

    // String Representation of Healer
    @Override
    public String toString() {
        return "Team " + (this.getTeamNumber() + 1) + " Healer";
    }
}