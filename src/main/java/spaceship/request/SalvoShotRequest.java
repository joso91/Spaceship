package spaceship.request;

import java.util.List;

public class SalvoShotRequest {

    private List<String> salvo;

    public SalvoShotRequest() {
    }

    public SalvoShotRequest(List<String> salvo) {
        this.salvo = salvo;
    }

    public List<String> getSalvo() {
        return salvo;
    }

    public void setSalvo(List<String> salvo) {
        this.salvo = salvo;
    }
}
