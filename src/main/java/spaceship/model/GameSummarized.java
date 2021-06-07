package spaceship.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameSummarized {

    private String user_id;
    private List<String> board;

    public GameSummarized(String user_id, String[][] grid) {
        this.user_id = user_id;

        board = new ArrayList<>();
        for (int i = 0; i < grid.length; i++) {
            board.add(Arrays.stream(grid[i]).collect(Collectors.joining("")));
        }
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<String> getBoard() {
        return board;
    }

    public void setBoard(List<String> board) {
        this.board = board;
    }
}
