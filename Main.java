package aa.project;

import aa.*;
import java.util.*;
import aa.project.supers.GameCharacter;

public class Main{
    public static void main(String[] args) throws InterruptedException {
        int NUM_CHAR = 10; // Total number of Characters on Field

        GameCharacter[] players = new GameCharacter[NUM_CHAR];

        // Makes the team. (1 Healer, 1 Tank, Rest are Attackers)
        GameCharacter[] team0 = makeTeam(NUM_CHAR/2,0);
        GameCharacter[] team1 = makeTeam(NUM_CHAR/2,1);

        int i = 0;
        for(GameCharacter gc : team0) {
            players[i] = gc;
            i++;
        }
        for(GameCharacter gc : team1) {
            players[i] = gc;
            i++;
        }

        // create threads
        ArrayList<Thread> threadList = new ArrayList<Thread>();
        ArrayList<GameCharacterThread> gameCharacterThreadList = new ArrayList<GameCharacterThread>();
        for (i = 0; i < NUM_CHAR; i++){
            GameCharacterThread temp = new GameCharacterThread(players, i); 
            threadList.add(new Thread(temp));
            gameCharacterThreadList.add(temp);
        }
        
        // Create Mid Game Announcer Thread
        Thread mga = new Thread(new MidGameAnnouncer(gameCharacterThreadList));
        mga.start();
        
        // Start All Threads
        for(Thread t: threadList) {
            t.start();
        }

        // main thread will join all threads
        mga.join();
        for (i = 0; i< NUM_CHAR; i++){
            threadList.get(i).join();
        }

        // print summary
        String prt = "";
        System.out.println("\n********* FINAL RESULTS *********");
        for (int k = 0; k < NUM_CHAR/2; k++) {
            GameCharacter temp1 = players[k];
            GameCharacter temp2 = players[k + (NUM_CHAR/2)];
            prt += temp1.toString() + "\t\t" + temp2.toString() + "\nHealth: " + temp1.getHealth() + "\t\t" + "Health: " + temp2.getHealth() + "\nAttack Power: " + temp1.getAttackPower() + "\t" + "Attack Power: " + temp2.getAttackPower() + "\nMana: " + temp1.getMana() + "\t\t" + "Mana: " + temp2.getMana() + "\n\n";
        }
        System.out.println(prt);

        // Print Winner
        printWinner(players);
    }

    // Prints the Winner (Winner Chosen by number of characters alive, if same, total health of the team)
    private static void printWinner(GameCharacter[] players) {
        int team1Survived = 0;
        int team2Survived = 0;
        int team1TotalHealth = 0;
        int team2TotalHealth = 0;

        for(int i = 0; i < players.length/2; i++) {
            int t1Health = players[i].getHealth();
            int t2Health = players[i + players.length/2].getHealth();
            if(t1Health > 0) {
                team1Survived++;
                team1TotalHealth += t1Health;
            }

            if(t2Health > 0) {
                team2Survived++;
                team2TotalHealth += t2Health;
            }
        }

        if(team1Survived > team2Survived) {
            System.out.println("*** TEAM 1 WINS by Number of Survivors ***");
        } else if(team1Survived < team2Survived) {
            System.out.println("*** TEAM 2 WINS by Number of Survivors ***");
        } else {
            if(team1TotalHealth > team2TotalHealth) {
                System.out.println("*** TEAM 1 WINS by Total Health ***");
            } else if(team2TotalHealth > team1TotalHealth) {
                System.out.println("*** TEAM 2 WINS by Total Health ***");
            } else {
                System.out.println("*** ITS A DRAW! ***");
            }
        }
    }

    // Function to make the team
    private static GameCharacter[] makeTeam(int players, int teamNum) {
        
        // Default Configurations of Characters
        final int INIT_HEALTH = 1000;
        final int TANK_HEALTH = 1200;
        final int INIT_MANA = 100;
        final int HEALER_MANA = 120;
        final int INIT_POWER = 5;
        final int HEALER_POWER = 20;

        GameCharacter[] plyer = new GameCharacter[players];
        
        GameCharacter healer = new Healer(INIT_HEALTH, HEALER_POWER, HEALER_MANA, teamNum);
        plyer[0] = healer;
        GameCharacter tank = new Tank(TANK_HEALTH, INIT_POWER, INIT_MANA, teamNum);
        plyer[players-1] = tank;
        for(int i = 1; i < players-1; i++) {
            plyer[i] = new Attacker(INIT_HEALTH,INIT_POWER,INIT_MANA,teamNum);
        }

        return plyer;
    }
}