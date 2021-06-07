package spaceship.controller;

import spaceship.model.BattleTracker;
import spaceship.model.Game;
import spaceship.model.GameSummarized;
import spaceship.request.NewGameRequest;
import spaceship.request.SalvoShotRequest;
import spaceship.response.GameStatusResponse;
import spaceship.response.NewGameResponse;
import spaceship.response.SalvoShotResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import spaceship.service.AttackService;


@RestController
public class Controller {

    @Autowired
    BattleTracker battleTracker;

    @Autowired
    AttackService attackService;

    @Value( "${server.port}" )
    private Integer serverPort;

    @PostMapping("/spaceship/protocol/game/new")
    ResponseEntity<NewGameResponse> newProtocolGame(@RequestBody NewGameRequest newGameRequest) {
        Game game = new Game(newGameRequest.getUser_id(), newGameRequest.getFull_name(),
                newGameRequest.getRules() != null ? newGameRequest.getRules() : "standard",
                newGameRequest.getSpaceship_protocol(), "match-" + (battleTracker.getGameCount() + 1));
        battleTracker.addGame(game);

        return new ResponseEntity<>(new NewGameResponse(game.getUserId1(), game.getFullName1(), game.getRules(), game.getGameId(), game.getStarting()), HttpStatus.CREATED);
    }

    @PutMapping("/spaceship/protocol/game/{game_id}")
    ResponseEntity<SalvoShotResponse> opponentShot(@RequestBody SalvoShotRequest salvoShotRequest, @PathVariable("game_id") String gameId) {
        Game game = battleTracker.getGame(gameId);
        if (game == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        HttpStatus httpStatus = game.getGame().containsKey("won") ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        SalvoShotResponse salvoShotResponse = attackService.takeSalvoShot(game,1, salvoShotRequest);

        return new ResponseEntity<>(salvoShotResponse, httpStatus);
    }

    @PutMapping("/spaceship/user/game/{game_id}/fire")
    ResponseEntity<SalvoShotResponse> playerShot(@RequestBody SalvoShotRequest salvoShotRequest, @PathVariable("game_id") String gameId) {
        Game game = battleTracker.getGame(gameId);
        if (game == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        HttpStatus httpStatus = game.getGame().containsKey("won") ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        SalvoShotResponse salvoShotResponse = attackService.takeSalvoShot(game,2, salvoShotRequest);

        return new ResponseEntity<>(salvoShotResponse, httpStatus);
    }

    @GetMapping("/spaceship/user/game/{game_id}")
    ResponseEntity<GameStatusResponse> viewGameStatus(@PathVariable("game_id") String gameId) {
        Game game = battleTracker.getGame(gameId);
        if (game == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        GameSummarized self = new GameSummarized(game.getUserId1(), game.getSpaceship1().getGrid()[0]);
        String[][] opponentGrid = game.getSpaceship2().getGrid()[0];

        String[][] hiddenOpponentGrid = new String[opponentGrid.length][opponentGrid.length];
        for (int i = 0; i < opponentGrid.length; i++)
            for (int j = 0; j < opponentGrid[i].length; j++) {
                if (opponentGrid[i][j].equals("*")) {
                    hiddenOpponentGrid[i][j] = ".";
                } else {
                    hiddenOpponentGrid[i][j] = opponentGrid[i][j];
                }
            }
        GameSummarized opponent = new GameSummarized(game.getUserId2(), hiddenOpponentGrid);

        return new ResponseEntity<>(new GameStatusResponse(self, opponent, game.getGame()), HttpStatus.OK);
    }

    @PostMapping("/spaceship/user/game/{game_id}/auto")
    ResponseEntity<SalvoShotResponse> autoShot(@PathVariable("game_id") String gameId) {
        Game game = battleTracker.getGame(gameId);
        if (game == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        if (game.getGame().containsKey("won")) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            SalvoShotResponse salvoShotResponse = attackService.takeAutoSalvoShot(game);
            return new ResponseEntity<>(salvoShotResponse, HttpStatus.OK);
        }
    }
}