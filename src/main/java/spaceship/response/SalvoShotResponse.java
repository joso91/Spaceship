package spaceship.response;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SalvoShotResponse {

    private LinkedHashMap<String, String> salvo;
    private Map<String, String> game;

    public SalvoShotResponse() {
    }

    public SalvoShotResponse(LinkedHashMap<String, String> salvo, String gameProgress) {
        this.salvo = salvo;
        game = new HashMap<>();
        game.put(gameProgress, "");
    }

    public LinkedHashMap<String, String> getSalvo() {
        return salvo;
    }

    public void setSalvo(LinkedHashMap<String, String> salvo) {
        this.salvo = salvo;
    }

    public Map<String, String> getGame() {
        return game;
    }

    public void setGame(Map<String, String> game) {
        this.game = game;
    }
}
