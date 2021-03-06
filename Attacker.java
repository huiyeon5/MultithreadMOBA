package aa.project.character;

import java.util.*;
import aa.project.supers.GameCharacter;

public class Attacker extends GameCharacter {

    // Constructor for Attackers
    public Attacker(int health, int attackPower, int mana, int teamNumber) {
        super(health, attackPower, mana,teamNumber);
    }

    // Attacks Single Player of the other team
    @Override
    public void attack(GameCharacter other) {
        other.receiveDamage(this.getAttackPower());
        this.useMana(1);
    }
    
    // Attacks Multiple Players of the other team
    @Override
    public void specialAttack(List<GameCharacter> others) {
        for(GameCharacter gc : others) {
            gc.receiveDamage(this.getAttackPower());
        }
        this.useMana(2);
    }
    
    // String Representation of Attacker
    @Override
    public String toString() {
        return "Team " + (this.getTeamNumber() + 1) + " Attacker";
    }
}