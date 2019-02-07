package aa.project;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import aa.project.supers.GameCharacter;
import aa.project.character.*;

// Runnable
public class GameCharacterThread implements Runnable {
    private int currentPlayer;
    private GameCharacter[] players;
    private GameCharacter currCharacter;
    private final int ACTION_COUNT = 50;
    private boolean suspended = false;

    // run
    public GameCharacterThread(GameCharacter[] players, int currentPlayer) {
        this.currentPlayer = currentPlayer;
        this.players = players;
        this.currCharacter = players[currentPlayer];
    }


    @Override
    public void run() {
        Random r = ThreadLocalRandom.current();

        // Run the loop until the character dies
        while(!currCharacter.isDead()) {

            // Breaks the loop if all opponents are dead
            if(allDead(currCharacter.getTeamNumber())) {
                break;
            }
            int percent = r.nextInt(10) + 1;

            // Waits until notified by Midgameannouncer to finish announcing
            synchronized(this) {
                while(suspended) {
                    try {
                        wait();
                    } catch(InterruptedException e) {

                    }
                }
            }

            // if Character is Attacker
            if(currCharacter instanceof Attacker) {
                // 80% of the time Attacker will execute Normal Attack
                if(percent <= 8) {
                    ArrayList<GameCharacter> opponents = getOpponents(true, r);
                    if(opponents.size() == 0 || opponents == null) {
                        break;
                    }
                    currCharacter.attack(opponents.get(0));
                    System.out.println(printNormal(opponents.get(0)));
                } else {
                    ArrayList<GameCharacter> opponents = getOpponents(false,r);
                    if(opponents.size() == 0 || opponents == null) {
                        break;
                    }
                    currCharacter.specialAttack(opponents);
                    System.out.println(printSpecial(opponents));
                }
            } else if(currCharacter instanceof Healer) {
                if(percent <= 8) {
                    ArrayList<GameCharacter> opponents = getTeam(true, r);
                    currCharacter.attack(opponents.get(0));
                    System.out.println(printNormal(opponents.get(0)));
                } else {
                    ArrayList<GameCharacter> opponents = getTeam(false,r);
                    currCharacter.specialAttack(opponents);
                    System.out.println(printSpecial(opponents));
                }
            } else {
                if(percent <= 8) {
                    ArrayList<GameCharacter> opponents = getOpponents(true, r);
                    if(opponents.size() == 0 || opponents == null) {
                        break;
                    }
                    currCharacter.attack(opponents.get(0));
                    System.out.println(printNormal(opponents.get(0)));
                } else {
                    ArrayList<GameCharacter> opponents = getTeam(false,r);
                    currCharacter.specialAttack(opponents);
                    System.out.println(printSpecial(opponents));
                }
            }

            if(currCharacter.getHealth() <= 0) {
                currCharacter.die();
            }
        }

        // System.out.println("\n" +currCharacter.toString() + " Done with all Attacks\n");
    }

    // Get opponents, either only ONE or many alive opponents
    private ArrayList<GameCharacter> getOpponents(boolean one, Random r) {    
        ArrayList<GameCharacter> chars = new ArrayList<>();
        int i = 0;
        if(currCharacter.getTeamNumber() == 0) {
            if(one) {
                int rand = r.nextInt(5) + 5;
                
                while(players[rand].isDead()) {
                    rand = r.nextInt(5) + 5;
                }
                
                chars.add(players[rand]);
                return chars;
            } else {
                int[] arr = new int[players.length/2];
                int c = 0;
                for(int j = players.length/2; j < players.length; j++) {
                    arr[c] = j;
                    c++;
                }
                while(i < arr.length) {
                    int num = arr[i];
                    if(!players[num].isDead()) {
                        chars.add(players[num]);
                    }
                    i++;
                }
                
                return chars;
            }
        } else {
            if(one) {
                int rand = r.nextInt(5);
                while(players[rand].isDead()) {
                    rand = r.nextInt(5);
                }
                chars.add(players[rand]);
                return chars;
            } else {
                int[] arr = new int[players.length/2];
                for(int j = 0; j < players.length/2; j++) {
                    arr[j] = j;
                }
                while(i < arr.length) {
                    int num = arr[i];
                    if(!players[num].isDead()) {
                        chars.add(players[num]);
                    }
                    i++;
                }
                return chars;
            }
        }
        
    }
    
    // Get Team member, either only ONE or many alive members
    private ArrayList<GameCharacter> getTeam(boolean one, Random r) {
        ArrayList<GameCharacter> chars = new ArrayList<>();
        int i = 0;

        if(currCharacter.getTeamNumber() == 0) {
            if(one) {
                int rand = r.nextInt(5);
                while(players[rand].isDead()) {
                    rand = r.nextInt(5);
                }
                chars.add(players[rand]);
                return chars;
            } else {
                int[] arr = new int[players.length/2];
                for(int j = 0; j < players.length/2; j++) {
                    arr[j] = j;
                }
                while(i < arr.length) {
                    int num = arr[i];
                    if(!players[num].isDead()) {
                        chars.add(players[num]);
                    }
                    i++;
                }
            }
        } else {
            if(one) {
                int rand = r.nextInt(5) + 5;
                while(players[rand].isDead()) {
                    rand = r.nextInt(5) + 5;
                }
                chars.add(players[rand]);
                return chars;
            } else {
                int[] arr = new int[players.length/2];
                int c = 0;
                for(int j = players.length/2; j < players.length; j++) {
                    arr[c] = j;
                    c++;
                }
                while(i < arr.length) {
                    int num = arr[i];
                    if(!players[num].isDead()) {
                        chars.add(players[num]);
                    }
                    i++;
                }
            }
        }

        return chars;
    }
    
    // Method to check if all opponents are dead
    private boolean allDead(int team) {
        if(team == 0) {
            for(int i = players.length/2; i < players.length; i++) {
                if(!players[i].isDead()) {
                    return false;
                }
            }
            return true;
        } else {
            for(int i = 0; i < players.length/2; i++) {
                if(!players[i].isDead()) {
                    return false;
                }
            }
            return true;
        }

    }
    
    private String printNormal(GameCharacter opponent) {
        String returnStr = currCharacter.toString() + " >>> " + opponent.toString() + " --> Normal Attack!";
        return returnStr;
    }

    private String printSpecial(ArrayList<GameCharacter> opponents) {
        return currCharacter.toString() + " used Special Attack";
    }

    public synchronized void suspend() { suspended = true; notify(); }
    public synchronized void resume() { suspended = false; notify(); }
    public GameCharacter getCharacter() {
        return currCharacter;
    }
}