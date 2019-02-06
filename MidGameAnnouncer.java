package aa.project;

import java.util.*;
import aa.project.GameCharacterThread;
import aa.project.supers.GameCharacter;

public class MidGameAnnouncer implements Runnable {
    private ArrayList<GameCharacterThread> playerThreads;
    private final int NUM_ANNOUNCEMENTS = 5;

    public MidGameAnnouncer(ArrayList<GameCharacterThread> playerThreads) {
        this.playerThreads = playerThreads;
    }

    @Override
    public void run() {
        for(int i = 0; i < NUM_ANNOUNCEMENTS; i++) {
            for(GameCharacterThread gct: playerThreads) {
                gct.suspend();
            }
            
            System.out.println("\n******* Announcer is collating information ********");
            try {
                String prt = "";
                for(int j = 0; j < 5; j++) {
                    GameCharacter temp1 = playerThreads.get(j).getCharacter();
                    GameCharacter temp2 = playerThreads.get(j + 5).getCharacter();
                    prt += temp1.toString() + "\t\t" + temp2.toString() + "\nHealth: " + temp1.getHealth() + "\t\t" + "Health: " + temp2.getHealth() + "\n";
                }
                
                System.out.println(prt);
                
                
                System.out.println("******* Let the games begin again! ********\n");
                
                for(GameCharacterThread gct: playerThreads) {
                    gct.resume();
                }

                Thread.sleep(50);
            } catch(InterruptedException e) {

            }

        }
    }
}