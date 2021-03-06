package aa.project.character;

import java.util.*;
import aa.project.supers.GameCharacter;

public class Tank extends GameCharacter {
    private int attackIncrease;

    // Constructor for Tanks
    public Tank(int health, int attackPower, int mana, int teamNumber) {
        super(health, attackPower, mana,teamNumber);
        this.attackIncrease = 5;
    }

    // Attacks Single player of the other team
    @Override
    public void attack(GameCharacter other) {
        other.receiveDamage(this.getAttackPower());
        this.useMana(1);
    }

    // Increases Attack power of Every player of the SAME team.
    @Override
    public void specialAttack(List<GameCharacter> others) {
        for(GameCharacter gc : others) {
            gc.increasePower(attackIncrease);
        }
        this.useMana(2);
    }

    // String Representation of Tank
    @Override
    public String toString() {
        return "Team " + (this.getTeamNumber() + 1) + " Tank";
    }
}