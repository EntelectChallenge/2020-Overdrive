package za.co.entelect.challenge;

import com.google.gson.Gson;
import za.co.entelect.challenge.command.Command;
import za.co.entelect.challenge.entities.GameState;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;

public class Main {

    private static final String ROUNDS_DIRECTORY = "rounds";
    private static final String STATE_FILE_NAME = "state.json";

    /**
     * Read the current state, feed it to the bot, get the output and print it to stdout
     *
     * @param args the args
     **/
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Gson gson = new Gson();
        Random random = new Random(System.nanoTime());

        for (int i = 0; i < 1; i++) {
            try {
                int roundNumber = 0;

                String statePath = String.format("../%s/%d/%s", ROUNDS_DIRECTORY, roundNumber, STATE_FILE_NAME);
                System.out.println(Paths.get(statePath));
                String state = new String(Files.readAllBytes(Paths.get(statePath)));

                GameState gameState = gson.fromJson(state, GameState.class);
                Command command = new Bot(random, gameState).run();

                System.out.println(String.format("C;%d;%s", roundNumber, command.render()));
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
