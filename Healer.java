package aa.project;
import java.util.*;


import aa.project.supers.GameCharacter;

public class Healer extends GameCharacter {

    public Healer(int health, int attackPower, int mana, int teamNumber) {
        super(health, attackPower, mana,teamNumber);
    }

    @Override
    public void attack(GameCharacter other) {
        other.increaseMana(attackPower);
        this.useMana(1);
    }
    
    @Override
    public void specialAttack(List<GameCharacter> others) {
        for(GameCharacter gc : others) {
            gc.increaseHealth(attackPower);
        }
        this.useMana(2);
    }

    @Override
    public String toString() {
        return "Team " + (teamNumber + 1) + " Healer";
    }
}