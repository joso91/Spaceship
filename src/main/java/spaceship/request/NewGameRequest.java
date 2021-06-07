package spaceship.request;

import spaceship.model.SpaceshipProtocol;

public class NewGameRequest {

    private String user_id;
    private String full_name;
    private String rules;
    private SpaceshipProtocol spaceship_protocol;

    public NewGameRequest() {
    }

    public NewGameRequest(String user_id, String full_name, String rules, SpaceshipProtocol spaceship_protocol) {
        this.user_id = user_id;
        this.full_name = full_name;
        this.rules = rules;
        this.spaceship_protocol = spaceship_protocol;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public SpaceshipProtocol getSpaceship_protocol() {
        return spaceship_protocol;
    }

    public void setSpaceship_protocol(SpaceshipProtocol spaceship_protocol) {
        this.spaceship_protocol = spaceship_protocol;
    }
}
