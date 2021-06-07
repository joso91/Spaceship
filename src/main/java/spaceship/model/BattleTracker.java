package spaceship.model;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class BattleTracker {

    private List<Game> gameList = new ArrayList<>();
    private int gameCount;

    public void addGame(Game game) {
        gameList.add(game);
        gameCount += 1;
    }

    public int getGameCount() {
        return gameCount;
    }

    public Game getGame(String game_id) {
        return gameList.stream().filter(e -> e.getGameId().equals(game_id)).findAny().orElse(null);
    }
}
