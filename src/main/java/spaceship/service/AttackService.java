package spaceship.service;

import org.springframework.stereotype.Service;
import spaceship.model.Game;
import spaceship.model.Spaceship;
import spaceship.request.SalvoShotRequest;
import spaceship.response.SalvoShotResponse;

import java.util.*;

@Service
public class AttackService {

    public SalvoShotResponse takeAutoSalvoShot(Game gameplay) {

        Spaceship spaceship1 = gameplay.getSpaceship1();
        Spaceship spaceship2 = gameplay.getSpaceship2();
        String rules = gameplay.getRules();

        List<String> salvo = new ArrayList<>();
        List<String> possibleShots = new ArrayList<>();
        int opponentHealth = (int)spaceship2.getSpaceshipsHealth().values().stream().filter(e -> e != 0).count();
        int numberOfShots = 0;

        if (opponentHealth != 0) {
            for (int i = 0; i < spaceship1.gridSize; i++) {
                for (int j = 0; j < spaceship1.gridSize; j++) {
                    if (spaceship1.getGrid()[0][i][j].equals(".") || spaceship1.getGrid()[0][i][j].equals("*")) {
                        String shot = Integer.toString(i, 16).toUpperCase() + "x" + Integer.toString(j, 16).toUpperCase();
                        possibleShots.add(shot);
                    }
                }
            }
        }

        if (rules.equals("standard") || rules.equals("super-charge")) {
            numberOfShots = opponentHealth;
        } else if (rules.contains("-shot")) {
            numberOfShots = Integer.valueOf(rules.substring(0, rules.length() - "-shot".length()));
        } else if (rules.equals("desperation")) {
            numberOfShots = 6 - (int)spaceship2.getSpaceshipsHealth().values().stream().filter(e -> e != 0).count();
        }

        if (possibleShots.size() <= numberOfShots) {
            salvo = possibleShots;
        } else {
            Random rand = new Random();
            while (salvo.size() != numberOfShots) {
                String shot = possibleShots.get(rand.nextInt(possibleShots.size()));
                possibleShots.remove(shot);
                salvo.add(shot);
            }
        }

        return takeSalvoShot(gameplay,1, new SalvoShotRequest(salvo));
    }

    public SalvoShotResponse takeSalvoShot(Game gameplay, int player, SalvoShotRequest salvoShotRequest) {

        Spaceship spaceship1 = gameplay.getSpaceship1();
        Spaceship spaceship2 = gameplay.getSpaceship2();
        Map<String, String> game = gameplay.getGame();
        String userId1 = gameplay.getUserId1();
        String userId2 = gameplay.getUserId2();
        String rules = gameplay.getRules();
        SalvoShotResponse salvoShotResponse = null;

        if (player == 1) {
            salvoShotResponse = takeSalvoShot(spaceship1, salvoShotRequest);
            if (game.containsKey("won")) {
                salvoShotResponse.getGame().remove("player_turn");
                salvoShotResponse.getGame().put("won", game.get("won"));
            } else if (salvoShotResponse.getGame().containsKey("won")) {
                salvoShotResponse.getGame().put("won", userId2);
                game.remove("player_turn");
                game.put("won", userId2);
            } else {
                String userId = rules.equals("super-charge") && salvoShotResponse.getSalvo().values().contains("kill") ? userId2 : userId1;
                salvoShotResponse.getGame().put("player_turn", userId);
                game.put("player_turn", userId);
            }
        } else {
            salvoShotResponse = takeSalvoShot(spaceship2, salvoShotRequest);
            if (game.containsKey("won")) {
                salvoShotResponse.getGame().remove("player_turn");
                salvoShotResponse.getGame().put("won", game.get("won"));
            } else if (salvoShotResponse.getGame().containsKey("won")) {
                salvoShotResponse.getGame().put("won", userId1);
                game.remove("player_turn");
                game.put("won", userId1);
            } else {
                String userId = rules.equals("super-charge") && salvoShotResponse.getSalvo().values().contains("kill") ? userId1 : userId2;
                salvoShotResponse.getGame().put("player_turn", userId);
                game.put("player_turn", userId);
            }
        }

        return salvoShotResponse;
    }

    public SalvoShotResponse takeSalvoShot(Spaceship spaceship, SalvoShotRequest salvoShotRequest) {

        String[][][] grid = spaceship.getGrid();
        Map<String, Integer> spaceshipsHealth = spaceship.getSpaceshipsHealth();
        LinkedHashMap<String, String> salvo = new LinkedHashMap<>();

        for (String shot : salvoShotRequest.getSalvo()) {
            int shotRow = Integer.parseInt(shot.substring(0, 1), 16);
            int shotColumn = Integer.parseInt(shot.substring(2, 3), 16);

            if (spaceship.gameOver()) {
                salvo.put(shot, "miss");
            } else if (grid[0][shotRow][shotColumn].equals(".")) {
                grid[0][shotRow][shotColumn] = "-";
                salvo.put(shot, "miss");
            } else if (grid[0][shotRow][shotColumn].equals("-") || grid[0][shotRow][shotColumn].equals("X")) {
                salvo.put(shot, "miss");
            } else if (grid[0][shotRow][shotColumn].equals("*")) {
                grid[0][shotRow][shotColumn] = "X";
                spaceshipsHealth.merge(grid[1][shotRow][shotColumn], -1, Integer::sum);
                if (spaceshipsHealth.get(grid[1][shotRow][shotColumn]) == 0) {
                    salvo.put(shot, "kill");
                } else {
                    salvo.put(shot, "hit");
                }
            }
        }

        if (spaceship.gameOver()) {
            return new SalvoShotResponse(salvo, "won");
        } else {
            return new SalvoShotResponse(salvo, "player_turn");
        }
    }
}
