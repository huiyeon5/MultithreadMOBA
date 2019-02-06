package aa.project;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import aa.project.supers.GameCharacter;

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
        for(int i = 0; i < ACTION_COUNT; i++) {
            int percent = r.nextInt(10) + 1;
            synchronized(this) {
                while(suspended) {
                    try {
                        wait();
                    } catch(InterruptedException e) {

                    }
                }
            }
            if(currCharacter instanceof Attacker) {
                if(percent <= 8) {
                    ArrayList<GameCharacter> opponents = getOpponents(1, r);
                    currCharacter.attack(opponents.get(0));
                    System.out.println(printNormal(opponents.get(0)));
                } else {
                    ArrayList<GameCharacter> opponents = getOpponents(5,r);
                    currCharacter.specialAttack(opponents);
                    System.out.println(printSpecial(opponents));
                }
            } else if(currCharacter instanceof Healer) {
                if(percent <= 8) {
                    ArrayList<GameCharacter> opponents = getTeam(1, r);
                    currCharacter.attack(opponents.get(0));
                    System.out.println(printNormal(opponents.get(0)));
                } else {
                    ArrayList<GameCharacter> opponents = getTeam(4,r);
                    currCharacter.specialAttack(opponents);
                    System.out.println(printSpecial(opponents));
                }
            } else {
                if(percent <= 8) {
                    ArrayList<GameCharacter> opponents = getOpponents(1, r);
                    currCharacter.attack(opponents.get(0));
                    System.out.println(printNormal(opponents.get(0)));
                } else {
                    ArrayList<GameCharacter> opponents = getTeam(4,r);
                    currCharacter.specialAttack(opponents);
                    System.out.println(printSpecial(opponents));
                }
            }
        }

        // System.out.println("\n" +currCharacter.toString() + " Done with all Attacks\n");
    }

    private ArrayList<GameCharacter> getOpponents(int numOpp, Random r) {
        ArrayList<GameCharacter> chars = new ArrayList<>();
        
        int i = 0;
        if(currCharacter.getTeamNumber() == 0) {
            if(numOpp == 1) {
                int rand = r.nextInt(5) + 5;
                chars.add(players[rand]);
                return chars;
            } else {
                int[] arr = {5,6,7,8,9};
                shuffleArray(arr);
                while(i < numOpp) {
                    int num = arr[i];
                    if(num != currentPlayer) {
                        chars.add(players[num]);
                    }
                    i++;
                }
            }
        } else {
            if(numOpp == 1) {
                int rand = r.nextInt(5);
                chars.add(players[rand]);
                return chars;
            } else {
                int[] arr = {0,1,2,3,4};
                shuffleArray(arr);
                while(i < numOpp) {
                    int num = arr[i];
                    if(num != currentPlayer) {
                        chars.add(players[num]);
                    }
                    i++;
                }
            }
        }

        return chars;
    }

    private ArrayList<GameCharacter> getTeam(int numOpp, Random r) {
        ArrayList<GameCharacter> chars = new ArrayList<>();
        int i = 0;
        if(currCharacter.getTeamNumber() == 0) {
            if(numOpp == 1) {
                int rand = r.nextInt(5);
                while(rand == currentPlayer) {
                    rand = r.nextInt(5);
                }
                chars.add(players[rand]);
                return chars;
            } else {
                int[] arr = {0,1,2,3,4};
                shuffleArray(arr);
                while(i < numOpp) {
                    int num = arr[i];
                    if(num != currentPlayer) {
                        chars.add(players[num]);
                    }
                    i++;
                }
            }
        } else {
            if(numOpp == 1) {
                int rand = r.nextInt(5) + 5;
                while(rand == currentPlayer) {
                    rand = r.nextInt(5) + 5;
                }
                chars.add(players[rand]);
                return chars;
            } else {
                int[] arr = {5,6,7,8,9};
                shuffleArray(arr);
                while(i < numOpp) {
                    int num = arr[i];
                    if(num != currentPlayer) {
                        chars.add(players[num]);
                    }
                    i++;
                }
            }
        }

        return chars;
    }


    private void shuffleArray(int[] ar) {
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
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