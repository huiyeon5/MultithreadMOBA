package aa.project.supers;
import java.util.*;
import java.util.concurrent.locks.*;

// Super Class for all the Characters in the game.
public abstract class GameCharacter{
    protected int health;
    protected int attackPower;
    protected int teamNumber;
    protected int mana;
    // public boolean isDead;

    // Using 3 Locks (1 for Health, 1 for Power, 1 for Mana) for maximum Liveness
    private ReentrantReadWriteLock healthLocks = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock powerLocks = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock manaLocks = new ReentrantReadWriteLock();

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

    public void increasePower(int incAmount) {
        powerWrite.lock();
        try {
            this.attackPower += incAmount;
        } finally {
            powerWrite.unlock();
        }
    }

    public void increaseHealth(int incAmount) {
        healthWrite.lock();
        try {
            this.health += incAmount;
        } finally {
            healthWrite.unlock();
        }
    }

    public void increaseMana(int incAmount) {
        manaWrite.lock();
        try {
            this.mana += incAmount;
        } finally {
            manaWrite.unlock();
        }
    }
    
   // public method which is used by the subclass when getting attacked.
    public void receiveDamage(int attackPower) {
        healthWrite.lock();
        try {
            this.health -= attackPower;
        } finally {
            healthWrite.unlock();
        }
    }

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

    // Implemented by the subclasses of Character due to different characteristics of Attack
    public abstract void attack(GameCharacter other);
    public abstract void specialAttack(List<GameCharacter>others);
    public abstract String toString();
}