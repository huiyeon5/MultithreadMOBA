package aa.project;

import java.util.*;
import aa.project.GameCharacterThread;
import aa.project.supers.GameCharacter;

public class MidGameAnnouncer implements Runnable {
    private ArrayList<GameCharacterThread> playerThreads;
    private final int NUM_ANNOUNCEMENTS;

    public MidGameAnnouncer(ArrayList<GameCharacterThread> playerThreads, int INIT_HEALTH) {
        this.playerThreads = playerThreads;
        this.NUM_ANNOUNCEMENTS = (int)Math.log10(INIT_HEALTH) + 5;
    }

    @Override
    public void run() {
        for(int i = 0; i < NUM_ANNOUNCEMENTS; i++) {
            for(GameCharacterThread gct: playerThreads) {
                gct.suspend();
            }
            
            try {
                String prt = "\nMidGameAnnouncer BREAK\n******* Announcer is collating information ********\n\n";
                for(int j = 0; j < 5; j++) {
                    GameCharacter temp1 = playerThreads.get(j).getCharacter();
                    GameCharacter temp2 = playerThreads.get(j + 5).getCharacter();
                    prt += temp1.toString() + "\t\t" + temp2.toString() + "\nHealth: " + temp1.getHealth() + "\t\t" + "Health: " + temp2.getHealth() + "\n";
                }
                
                System.out.println(prt);
                
                
                System.out.println("******* Let the games begin again! ********\n");
                Thread.sleep(1500);
                for(GameCharacterThread gct: playerThreads) {
                    gct.resume();
                }

                Thread.sleep(100);
            } catch(InterruptedException e) {

            }

        }
    }
}