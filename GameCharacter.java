package aa.project.supers;
import java.util.*;
import java.util.concurrent.locks.*;

// Super Class for all the Characters in the game.
public abstract class GameCharacter{
    private int health;
    private int attackPower;
    private int teamNumber;
    private int mana;
    private boolean dead;

    // Using 3 Locks (1 for Health, 1 for Power, 1 for Mana) for maximum Liveness
    private ReentrantReadWriteLock healthLocks = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock powerLocks = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock manaLocks = new ReentrantReadWriteLock();

    // Read Write Locks for all attributes
    private Lock healthRead = healthLocks.readLock();
    private Lock healthWrite = healthLocks.writeLock();
    private Lock powerRead = powerLocks.readLock();
    private Lock powerWrite = powerLocks.writeLock();
    private Lock manaRead = healthLocks.readLock();
    private Lock manaWrite = healthLocks.readLock();

    // Constructor used even if cannot be instantiated. Called by the subclass
    public GameCharacter(int health, int attackPower, int mana,int teamNumber) {
        this.health = health;
        this.attackPower = attackPower;
        this.teamNumber = teamNumber;
        this.mana = mana;
        this.dead = false;
    }

    /**
     * @return the health
     */
    public int getHealth() {
        healthRead.lock();
        try {
            return health;
        } finally {
            healthRead.unlock();
        }
    }

    /**
     * @return the attackPower
     */
    public int getAttackPower() {
        powerRead.lock();
        try {
            return attackPower;
        } finally {
            powerRead.unlock();
        }
    }

    /**
     * @return the mana
     */
    public int getMana() {
        manaRead.lock();
        try {
            return mana;
        } finally {
            manaRead.unlock();
        }
    }

    /**
     * @return the teamNumber
     */
    public int getTeamNumber() {
        return teamNumber;
    }

    /**
     * @return if Character is dead or not
     */
    public synchronized boolean isDead() {
        return dead;
    }

    // Increases the Attacking Power of the Character
    public void increasePower(int incAmount) {
        powerWrite.lock();
        try {
            this.attackPower += incAmount;
        } finally {
            powerWrite.unlock();
        }
    }

    // Increases the Health of the Character
    public void increaseHealth(int incAmount) {
        healthWrite.lock();
        try {
            this.health += incAmount;
        } finally {
            healthWrite.unlock();
        }
    }

    // Increases the Mana of the Character
    public void increaseMana(int incAmount) {
        manaWrite.lock();
        try {
            this.mana += incAmount;
        } finally {
            manaWrite.unlock();
        }
    }
    
   // Character Receives Damage and loses health
    public void receiveDamage(int attackPower) {
        healthWrite.lock();
        try {
            this.health -= attackPower;
        } finally {
            healthWrite.unlock();
        }
    }

    // Reduces Mana every time a move is made
    public void useMana(int attackType) {
        if(attackType == 1) {
            manaWrite.lock();
            try {
                mana -= 1;
            } finally {
                manaWrite.unlock();
            }
        }else {
            manaWrite.lock();
            try {
                mana -= 2;
            } finally {
                manaWrite.unlock();
            }
        }
    }

    public synchronized void die() {
        this.dead = true;
    }

    // Implemented by the subclasses of Character due to different characteristics of Attack
    public abstract void attack(GameCharacter other);
    // Implemented by the subclasses of Character due to different characteristics of SpecialAttack
    public abstract void specialAttack(List<GameCharacter>others);
    // Implemented by the subclasses of Character due to different String Output
    public abstract String toString();
}