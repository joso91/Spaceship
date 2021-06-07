package spaceship.model;

import java.util.*;

public class Game {

    private Spaceship spaceship1;
    private Spaceship spaceship2;

    private String userId1;
    private String userId2;

    private String fullName1;
    private String fullName2;

    private String rules;
    private SpaceshipProtocol spaceshipProtocol;
    private String gameId;
    private String starting;

    private Map<String, String> game;


    public Game(String userId2, String fullName2, String rules, SpaceshipProtocol spaceshipProtocol, String gameId) {
        this.spaceship1 = new Spaceship();
        this.spaceship2 = new Spaceship();
        this.userId1 = "player";
        this.userId2 = userId2;
        this.fullName1 = "Assessment Player";
        this.fullName2 = fullName2;
        this.rules = rules;
        this.spaceshipProtocol = spaceshipProtocol;
        this.gameId = gameId;
        this.starting = userId2;
        this.game = new HashMap<>();
        this.game.put("player_turn", this.userId1);
    }

    public Spaceship getSpaceship1() {
        return spaceship1;
    }

    public void setSpaceship1(Spaceship spaceship1) {
        this.spaceship1 = spaceship1;
    }

    public Spaceship getSpaceship2() {
        return spaceship2;
    }

    public void setSpaceship2(Spaceship spaceship2) {
        this.spaceship2 = spaceship2;
    }

    public String getUserId1() {
        return userId1;
    }

    public void setUserId1(String userId1) {
        this.userId1 = userId1;
    }

    public String getUserId2() {
        return userId2;
    }

    public void setUserId2(String userId2) {
        this.userId2 = userId2;
    }

    public String getFullName1() {
        return fullName1;
    }

    public void setFullName1(String fullName1) {
        this.fullName1 = fullName1;
    }

    public String getFullName2() {
        return fullName2;
    }

    public void setFullName2(String fullName2) {
        this.fullName2 = fullName2;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public SpaceshipProtocol getSpaceshipProtocol() {
        return spaceshipProtocol;
    }

    public void setSpaceshipProtocol(SpaceshipProtocol spaceshipProtocol) {
        this.spaceshipProtocol = spaceshipProtocol;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getStarting() {
        return starting;
    }

    public void setStarting(String starting) {
        this.starting = starting;
    }

    public Map<String, String> getGame() {
        return game;
    }

    public void setGame(Map<String, String> game) {
        this.game = game;
    }
}
