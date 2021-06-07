package spaceship.response;

public class NewGameResponse {

    private String user_id;
    private String full_name;
    private String game_id;
    private String starting;
    private String rules;

    public NewGameResponse() {
    }

    public NewGameResponse(String user_id, String full_name, String game_id, String starting, String rules) {
        this.user_id = user_id;
        this.full_name = full_name;
        this.game_id = game_id;
        this.starting = starting;
        this.rules = rules;
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

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public String getStarting() {
        return starting;
    }

    public void setStarting(String starting) {
        this.starting = starting;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }
}
