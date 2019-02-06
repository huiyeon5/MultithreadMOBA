package aa.project;
import java.util.*;

import aa.project.supers.GameCharacter;

public class Attacker extends GameCharacter {

    public Attacker(int health, int attackPower, int mana, int teamNumber) {
        super(health, attackPower, mana,teamNumber);
    }

    @Override
    public void attack(GameCharacter other) {
        other.receiveDamage(attackPower);
        this.useMana(1);
    }
    
    @Override
    public void specialAttack(List<GameCharacter> others) {
        for(GameCharacter gc : others) {
            gc.receiveDamage(attackPower);
        }
        this.useMana(2);
    }

    @Override
    public String toString() {
        return "Team " + (teamNumber + 1) + " Attacker";
    }
}