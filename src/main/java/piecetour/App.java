/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package piecetour;

import piecetour.board.Cell;

import java.util.concurrent.ThreadLocalRandom;

public class App {
    public static final Integer boardSize = 10;

    // Move pattern on basis of the change of
    // x coordinates and y coordinates respectively
    private final static Integer[][] moves = {{1, -2}, {2, -1}, {2, 1}, {1, 2}, {-1, 2},
            {-2, 1}, {-2, -1}, {-1, -2}, {0, 0}, {0, 0}};

    // function restricts the knight to remain within
    // the 8x8 chessboard
    boolean verifyLimits(Integer column, Integer line) {
        return ((column >= 0 && line >= 0) &&
                (column < boardSize && line < boardSize));
    }

    /* Checks whether boardCells square is valid and
    empty or not */
    boolean isCellValidAndEmpty(Integer boardCells[], Integer column, Integer line) {
        return (verifyLimits(column, line)) && (boardCells[line * boardSize + column] < 0);
    }

    /* Returns the number of empty squares
    adjacent to (column, line) */
    Integer getNeighboringEmptyCellsNumber(Integer boardCells[], Integer column, Integer line) {
        Integer count = 0;
        for (Integer i = 0; i < boardSize; ++i)
            if (isCellValidAndEmpty(boardCells, (column + moves[i][0]),
                    (line + moves[i][1])))
                count++;

        return count;
    }

    // Picks next point using Warnsdorff's heuristic.
    // Returns false if it is not possible to pick
    // next point.
    Cell nextMove(Integer boardCells[], Cell currentCell) {
        Integer choosenMovement = -1;
        Integer adjacentEmptyCellsNumber;
        Integer previousAdjacentEmptyCellsNumber = (boardSize + 1);
        Integer newColumn;
        Integer newLine;

        // Try all N adjacent of (*x, *y) starting
        // from boardCells random adjacent. Find the adjacent
        // with minimum degree.
        Integer start = ThreadLocalRandom.current().nextInt(1000) % boardSize;
        for (Integer count = 0; count < boardSize; ++count) {
            Integer possibleMovement = (start + count) % boardSize;
            newColumn = currentCell.getColumn() + moves[possibleMovement][0];
            newLine = currentCell.getLine() + moves[possibleMovement][1];
            adjacentEmptyCellsNumber = getNeighboringEmptyCellsNumber(boardCells, newColumn, newLine);
            if ((isCellValidAndEmpty(boardCells, newColumn, newLine))
                    && adjacentEmptyCellsNumber < previousAdjacentEmptyCellsNumber) {
                choosenMovement = possibleMovement;
                previousAdjacentEmptyCellsNumber = adjacentEmptyCellsNumber;
            }
        }

        // IF we could not find boardCells next currentCell
        if (choosenMovement == -1)
            return null;

        // Store coordinates of next point
        newColumn = currentCell.getColumn() + moves[choosenMovement][0];
        newLine = currentCell.getLine() + moves[choosenMovement][1];

        // Mark next move
        boardCells[newLine * boardSize + newColumn] = boardCells[(currentCell.getLine()) * boardSize +
                (currentCell.getColumn())] + 1;

        // Update next point
        currentCell.setColumn(newColumn);
        currentCell.setLine(newLine);

        return currentCell;
    }

    /* displays the chessboard with all the
    legal knight's moves */
    void print(Integer boardCells[]) {
        for (Integer i = 0; i < boardSize; ++i) {
            for (Integer j = 0; j < boardSize; ++j)
                System.out.printf("%d\t", boardCells[j * boardSize + i]);
            System.out.printf("\n");
        }
    }

    /* checks its neighbouring sqaures */
    /* If the knight ends on a square that is one
    knight's move from the beginning square,
    then tour is closed */
    boolean neighbour(Cell nextCell, Integer currentColumn, Integer currentLine) {
        for (Integer i = 0; i < boardSize; ++i)
            if (((nextCell.getColumn() + moves[i][0]) == currentColumn) &&
                    ((nextCell.getLine() + moves[i][1]) == currentLine))
                return true;

        return false;
    }

    /* Generates the legal moves using warnsdorff's
    heuristics. Returns false if not possible */
    boolean findClosedTour() {
        // Filling up the chessboard matrix with -1's
        Integer boardCells[] = new Integer[boardSize * boardSize];
        for (Integer i = 0; i < boardSize * boardSize; ++i)
            boardCells[i] = -1;

        // initial position
        Integer currentColumn = 3;
        Integer currentLine = 2;

        // Current points are same as initial points
        Cell cell = new Cell(currentColumn, currentLine);

        boardCells[cell.getLine() * boardSize + cell.getColumn()] = 1; // Mark first move.

        // Keep picking next points using
        // Warnsdorff's heuristic
        Cell nextCell = null;
        for (Integer i = 0; i < boardSize * boardSize - 1; ++i) {
            nextCell = nextMove(boardCells, cell);
            if (nextCell == null)
                return false;
        }

        // Check if tour is closed (Can end
        // at starting point)
        if (!neighbour(nextCell, currentColumn, currentLine))
            return false;

        print(boardCells);
        return true;
    }

    // Driver Code
    public static void main(String[] args) {
        // While we don't get a solution
        while (!new App().findClosedTour()) {
            ;
        }
    }
}
