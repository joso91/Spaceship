package spaceship.model;


import java.util.*;
import java.util.stream.Stream;

public class Spaceship {

    private String[][][] grid;

    public static final int gridSize = 16;
    public static final Map<String, List<String[][]>> spaceships = new HashMap<>();
    private Map<String, Integer> spaceshipsHealth;

    static {
        fillSpaceshipTypes();
    }


    public Spaceship() {
        initializeGrid();
        placeSpaceShips();
        initializeSpaceshipsHealth();
    }

    private static void fillSpaceshipTypes() {
        fillWingerList();
        fillAngleList();
        fillAClassList();
        fillBClassList();
        fillSClassList();
    }

    private static void fillWingerList() {
        String[][] wingerUp = {
                {"*", ".", "*"},
                {"*", ".", "*"},
                {".", "*", "."},
                {"*", ".", "*"},
                {"*", ".", "*"}
        };
        String[][] wingerLeft = {
                {"*", "*", ".", "*", "*"},
                {".", ".", "*", ".", "."},
                {"*", "*", ".", "*", "*"}
        };
        String[][] wingerDown = {
                {"*", ".", "*"},
                {"*", ".", "*"},
                {".", "*", "."},
                {"*", ".", "*"},
                {"*", ".", "*"}
        };
        String[][] wingerRight = {
                {"*", "*", ".", "*", "*"},
                {".", ".", "*", ".", "."},
                {"*", "*", ".", "*", "*"}
        };

        List<String[][]> wingerList = Arrays.asList(wingerUp, wingerLeft, wingerDown, wingerRight);
        spaceships.put("winger", wingerList);
    }

    private static void fillAngleList() {
        String[][] angleUp = {
                {"*", ".", "."},
                {"*", ".", "."},
                {"*", ".", "."},
                {"*", "*", "*"}
        };
        String[][] angleLeft = {
                {".", ".", ".", "*"},
                {".", ".", ".", "*"},
                {"*", "*", "*", "*"}
        };
        String[][] angleDown = {
                {"*", "*", "*"},
                {".", ".", "*"},
                {".", ".", "*"},
                {".", ".", "*"}
        };
        String[][] angleRight = {
                {"*", "*", "*", "*"},
                {"*", ".", ".", "."},
                {"*", ".", ".", "."}
        };

        List<String[][]> angleList = Arrays.asList(angleUp, angleLeft, angleDown, angleRight);
        spaceships.put("angle", angleList);
    }

    private static void fillAClassList() {
        String[][] aClassUp = {
                {".", "*", "."},
                {"*", ".", "*"},
                {"*", "*", "*"},
                {"*", ".", "*"}
        };
        String[][] aClassLeft = {
                {".", "*", "*", "*"},
                {"*", ".", "*", "."},
                {".", "*", "*", "*"}
        };
        String[][] aClassDown = {
                {"*", ".", "*"},
                {"*", "*", "*"},
                {"*", ".", "*"},
                {".", "*", "."}
        };
        String[][] aClassRight = {
                {"*", "*", "*", "."},
                {".", "*", ".", "*"},
                {"*", "*", "*", "."}
        };

        List<String[][]> aClassList = Arrays.asList(aClassUp, aClassLeft, aClassDown, aClassRight);
        spaceships.put("aClass", aClassList);
    }

    private static void fillBClassList() {
        String[][] bClassUp = {
                {"*", "*", "."},
                {"*", ".", "*"},
                {"*", "*", "."},
                {"*", ".", "*"},
                {"*", "*", "."}
        };
        String[][] bClassLeft = {
                {".", "*", ".", "*", "."},
                {"*", ".", "*", ".", "*"},
                {"*", "*", "*", "*", "*"}
        };
        String[][] bClassDown = {
                {".", "*", "*"},
                {"*", ".", "*"},
                {".", "*", "*"},
                {"*", ".", "*"},
                {".", "*", "*"}
        };
        String[][] bClassRight = {
                {"*", "*", "*", "*", "*"},
                {"*", ".", "*", ".", "*"},
                {".", "*", ".", "*", "."}
        };

        List<String[][]> bClassList = Arrays.asList(bClassUp, bClassLeft, bClassDown, bClassRight);
        spaceships.put("bClass", bClassList);
    }

    private static void fillSClassList() {
        String[][] sClassUp = {
                {".", "*", "*", "."},
                {"*", ".", ".", "."},
                {".", "*", "*", "."},
                {".", ".", ".", "*"},
                {".", "*", "*", "."}
        };
        String[][] sClassLeft = {
                {".", ".", ".", "*", "."},
                {"*", ".", "*", ".", "*"},
                {"*", ".", "*", ".", "*"},
                {".", "*", ".", ".", "."}
        };
        String[][] sClassDown = {
                {".", "*", "*", "."},
                {"*", ".", ".", "."},
                {".", "*", "*", "."},
                {".", ".", ".", "*"},
                {".", "*", "*", "."}
        };
        String[][] sClassRight = {
                {".", ".", ".", "*", "."},
                {"*", ".", "*", ".", "*"},
                {"*", ".", "*", ".", "*"},
                {".", "*", ".", ".", "."}
        };

        List<String[][]> sClassList = Arrays.asList(sClassUp, sClassLeft, sClassDown, sClassRight);
        spaceships.put("sClass", sClassList);
    }

    private void initializeGrid() {
        grid = new String[2][gridSize][gridSize];

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < gridSize; j++) {
                for (int k = 0; k < gridSize; k++) {
                    if (i == 0) {
                        grid[i][j][k] = ".";
                    } else {
                        grid[i][j][k] = "";
                    }
                }
            }
        }
    }

    private void placeSpaceShips() {
        Random rand = new Random();
        List<String> spaceshipModelsList = new ArrayList<>(spaceships.keySet());

        while (!spaceshipModelsList.isEmpty()) {
            String spaceshipModel = spaceshipModelsList.get(rand.nextInt(spaceshipModelsList.size()));
            spaceshipModelsList.remove(spaceshipModel);

            int spaceshipRotation = rand.nextInt(spaceships.get(spaceshipModel).size());
            String[][] spaceship = spaceships.get(spaceshipModel).get(spaceshipRotation);

            //potential bug for different gridSize or spaceships
            while (true){
                int insertRowPosition = rand.nextInt(gridSize - spaceship.length + 1);
                int insertColumnPosition = rand.nextInt(gridSize - spaceship[0].length + 1);
                if (insertSubGrid(spaceship, insertRowPosition, insertColumnPosition, spaceshipModel)) {
                    break;
                }
            }
        }
    }

    private boolean insertSubGrid(String[][] subgrid, int subgridRowPosition, int subgridColumnPosition, String spaceshipModel) {
        int subgridRowNumber = subgrid.length;
        int subgridColumnNumber= subgrid[0].length;

        for (int j = 0; j < subgridRowNumber; j++) {
            for (int k = 0; k < subgridColumnNumber; k++) {
                if (grid[0][subgridRowPosition + j][subgridColumnPosition + k].equals("*")){
                    return false;
                }
            }
        }

        for (int j = 0; j < subgridRowNumber; j++) {
            for (int k = 0; k < subgridColumnNumber; k++) {
                grid[0][subgridRowPosition + j][subgridColumnPosition + k] = subgrid[j][k];
                if (subgrid[j][k].equals("*")) {
                    grid[1][subgridRowPosition + j][subgridColumnPosition + k] = spaceshipModel;
                }
            }
        }

        return true;
    }

    public void initializeSpaceshipsHealth() {

        spaceshipsHealth = new HashMap<>();
        for (String spaceshipModel : spaceships.keySet()) {
            String[][] spaceshipModelUp = spaceships.get(spaceshipModel).get(0);
            spaceshipsHealth.put(spaceshipModel, (int)Arrays.stream(spaceshipModelUp).flatMap(Stream::of).filter(e -> e.equals("*")).count());
        }
    }

    public boolean gameOver() {
        return spaceshipsHealth.values().stream().mapToInt(Integer::valueOf).sum() == 0;
    }

    public String[][][] getGrid() {
        return grid;
    }

    public Map<String, Integer> getSpaceshipsHealth() {
        return spaceshipsHealth;
    }
}
