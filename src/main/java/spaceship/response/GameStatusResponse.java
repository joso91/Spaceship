package spaceship.response;

import spaceship.model.GameSummarized;

import java.util.Map;

public class GameStatusResponse {

    private GameSummarized self;
    private GameSummarized opponent;
    private Map<String, String> game;

    public GameStatusResponse() {
    }

    public GameStatusResponse(GameSummarized self, GameSummarized opponent, Map<String, String> game) {
        this.self = self;
        this.opponent = opponent;
        this.game = game;
    }

    public GameSummarized getSelf() {
        return self;
    }

    public void setSelf(GameSummarized self) {
        this.self = self;
    }

    public GameSummarized getOpponent() {
        return opponent;
    }

    public void setOpponent(GameSummarized opponent) {
        this.opponent = opponent;
    }

    public Map<String, String> getGame() {
        return game;
    }

    public void setGame(Map<String, String> game) {
        this.game = game;
    }
}
