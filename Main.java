package aa.project;

import aa.*;
import java.util.*;
import aa.project.supers.GameCharacter;

public class Main{
    public static void main(String[] args) throws InterruptedException {
        GameCharacter[] players = new GameCharacter[10];
        GameCharacter[] team0 = makeTeam(5,0);
        GameCharacter[] team1 = makeTeam(5,1);
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
        for (i = 0; i < 10; i++){
            GameCharacterThread temp = new GameCharacterThread(players, i); 
            threadList.add(new Thread(temp));
            gameCharacterThreadList.add(temp);
        }
        
        Thread mga = new Thread(new MidGameAnnouncer(gameCharacterThreadList));
        mga.start();
        
        for(Thread t: threadList) {
            t.start();
        }

        // main thread will join all threads
        mga.join();
        for (i = 0; i< 10; i++){
            threadList.get(i).join();
        }

        // print summary
        for (int k = 0; k < players.length; k++) {
            int playerNumber = k + 1;
            System.out.println("Player " + playerNumber);
            System.out.println("Health: " + players[k].getHealth() + " | Attack Power: " + players[k].getAttackPower() + " | Mana: " + players[k].getMana());
            System.out.println(players[k].toString());
            System.out.println("--");
        }

    }

    private static GameCharacter[] makeTeam(int players, int teamNum) {
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