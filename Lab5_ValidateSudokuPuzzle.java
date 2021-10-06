
//-----------------------------------------------------------
//  Author: Zachary Perales
//  Course: CSC240-80
//  Due   : 10/24/2019
//  Description: This program validates a Sudoku puzzle. I used
//  the 2D array to work with. I changed the values in the array,
//  to test it thoroughly. All requirements should be completed
//  to the best of my understanding.
//-----------------------------------------------------------

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Lab5_ValidateSudokuPuzzle {
    boolean isRowInvalid = false;
    boolean isColumnInvalid = false;
    boolean isSectionInvalid = false;

    boolean isRowIncomplete = false;
    boolean isColumnIncomplete = false;
    boolean isSectionIncomplete = false;

    boolean isPuzzleInvalid = false;
    boolean isPuzzleIncomplete = false;

    int troubleSpot;

    ArrayList<String> messageArrayList = new ArrayList<String>();
    ArrayList<String> deDupedMessageArrayList = new ArrayList<String>();
    ArrayList<Integer> troubleArray = new ArrayList<Integer>();

    public int[][] getPuzzleSections() {
        return puzzleSections;
    }

    public void setPuzzleSections(int[][] puzzleSections) {
        this.puzzleSections = puzzleSections;
    }

    int[][] puzzleSections = new int[10][10];

    public ArrayList<Integer> getTroubleArray() {
        return troubleArray;
    }

    // This makes the sections
    public ArrayList<String> buildPuzzleSections(int[][] puzzleSectionArray) {
        ArrayList<String> puzzleSections = new ArrayList<String>();
        String[] section = new String[9];

        for (int i = 0; i < 9; i++) {
            section[i] = "Section[" + (i + 1) + "]:\n";

            for (int j = 0; j < 9; j++) {
                section[i] = section[i] + puzzleSectionArray[i][j] + ",";

                if (j == 2 || j == 5) {
                    section[i] = section[i] + "\n";
                }
            }

            puzzleSections.add(section[i]);
        }

        return puzzleSections;
    }

    // This cleans up the duplicates and makes a nice ending display for the final steps
    public ArrayList<String> cleanAndMergeDisplayArrayList(ArrayList<String> dirtyArrayList, ArrayList<String> sectionArrayList) {
        ArrayList<String> rowsCols = new ArrayList<String>();
        ArrayList<String> messages = new ArrayList<String>();
        ArrayList<String> combine = new ArrayList<String>();

        String identifier = "";

        for (String string : dirtyArrayList) {
            if (string.contains("Row[") || string.contains("Column[")) {
                if (!rowsCols.contains(string)) {
                    rowsCols.add(string);
                }
            }

            if (string.contains("incomplete") || string.contains("duplicate") || string.contains("non-single-digit")) {
                messages.add(string);
            }
        }

        for (String rowCol : rowsCols) {
            identifier = rowCol.split("\\:")[0];
            identifier = identifier.replace("[", " ");
            identifier = identifier.replace("]", "");
            identifier = identifier.toLowerCase();

            for (String message : messages) {
                if (message.contains(identifier)) {
                    combine.add(message);
                }
            }

            combine.add(rowCol);
        }

        for (String section : sectionArrayList) {
            boolean containsMsgs = false;
            identifier = section.split("\\:")[0];
            identifier = identifier.replace("[", " ");
            identifier = identifier.replace("]", "");
            identifier = identifier.toLowerCase();

            for (String message : messages) {
                if (message.contains(identifier)) {
                    combine.add(message);
                    containsMsgs = true;
                }
            }

            if (containsMsgs) {
                combine.add(section + "\n");
            }
        }

        return combine;

    }


    public ArrayList<String> getDeDupedMessageArrayList() {
        return deDupedMessageArrayList;
    }

    public void setDeDupedMessageArrayList(ArrayList<String> deDupedMessageArrayList) {
        this.deDupedMessageArrayList = deDupedMessageArrayList;
    }

    public boolean isSectionInvalid() {
        return isSectionInvalid;
    }

    public void setSectionInvalid(boolean sectionInvalid) {
        isSectionInvalid = sectionInvalid;
    }

    public int getTroubleSpot() {
        return troubleSpot;
    }

    public void setTroubleSpot(int troubleSpot) {
        this.troubleSpot = troubleSpot;
    }

    public boolean isRowInvalid() {
        return isRowInvalid;
    }

    public void setRowInvalid(boolean rowInvalid) {
        isRowInvalid = rowInvalid;
    }

    public boolean isColumnInvalid() {
        return isColumnInvalid;
    }

    public void setColumnInvalid(boolean columnInvalid) {
        isColumnInvalid = columnInvalid;
    }

    public ArrayList<String> getMessageArrayList() {
        return messageArrayList;
    }

    // Adds a message to the message list
    public void addMessage(ArrayList<String> messageArrayList, String message) {
        messageArrayList.add(message);
    }

    // Prints the puzzle or column rows
    public void printPuzzleRowOrCol(int[][] puzzle, int row, int col, String location, String issue) {

        if (location.equals("Row")) {
            System.out.print("\nRow [" + row + "]: ");

            for (int i = 0; i < 9; i++) {
                System.out.print(puzzle[row][i] + ", ");
            }

        } else if (location.equals("Column")) {
            System.out.print("\nColumn [" + col + "]: ");

            for (int i = 0; i < 9; i++) {
                System.out.print(puzzle[i][col] + ", ");
            }
        }

        if (issue.equals("Incomplete")) {
            System.out.print("\nIncomplete\n");
        } else if (issue.equals("Invalid")) {
            System.out.print("\nInvalid\n");
        }
    }

    // Prints the puzzle
    public void printPuzzle(int[][] puzzle) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (j == 8 && i == 8) {
                    System.out.print(puzzle[i][j]);
                    break;
                }

                System.out.print(puzzle[i][j] + ",");

                if (j == 2 || j == 5) {
                    System.out.print("  ");
                }
            }

            System.out.println();

            if (i == 2 || i == 5) {
                System.out.println();
            }
        }
    }

    // Validates the rows and check for incompletes
    boolean validateRows(int[][] puzzle) {
        setTroubleSpot(-1);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                for (int x = 0; x < 9; x++) {
                    if (puzzle[i][j] == 0) {
                        isRowIncomplete = true;
                        addMessage(getMessageArrayList(), "There are incomplete values in row " + (i + 1));

                        if (getTroubleSpot() != i) {
                            printPuzzleRowOrCol(puzzle, i, j, "Row", "Incomplete");

                            String rowDisplay = "Row[" + (i + 1) + "]: ";

                            for (int z = 0; z < 9; z++) {
                                if (z == 8) {
                                    rowDisplay = rowDisplay + puzzle[i][z] + "\n";
                                    break;
                                }

                                rowDisplay = rowDisplay + puzzle[i][z] + ", ";
                            }

                            addMessage(getMessageArrayList(), rowDisplay);
                        }

                        setTroubleSpot(i);
                    }
                }
            }
        }

        setTroubleSpot(-1);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                for (int x = 0; x < 9; x++) {
                    if ((puzzle[i][j] == puzzle[i][x]) && (x != j) && (puzzle[i][j] != 0)) {
                        isRowInvalid = true;
                        addMessage(getMessageArrayList(), "There are duplicate values in row " + (i + 1));

                        if (getTroubleSpot() != i) {
                            printPuzzleRowOrCol(puzzle, i, j, "Row", "Invalid");

                            String rowDisplay = "Row[" + (i + 1) + "]: ";

                            for (int z = 0; z < 9; z++) {
                                if (z == 8) {
                                    rowDisplay = rowDisplay + puzzle[i][z] + "\n";
                                    break;
                                }

                                rowDisplay = rowDisplay + puzzle[i][z] + ", ";
                            }

                            addMessage(getMessageArrayList(), rowDisplay);
                        }

                        setTroubleSpot(i);
                    } else if (puzzle[i][j] > 9 || puzzle[i][j] < 0) {
                        isRowInvalid = true;

                        addMessage(getMessageArrayList(), "There are non-single-digit values in row " + (i + 1));

                        if (getTroubleSpot() != i) {
                            printPuzzleRowOrCol(puzzle, i, j, "Row", "Invalid");

                            String rowDisplay = "Row[" + (i + 1) + "]: ";

                            for (int z = 0; z < 9; z++) {
                                if (z == 8) {
                                    rowDisplay = rowDisplay + puzzle[i][z] + "\n";
                                    break;
                                }

                                rowDisplay = rowDisplay + puzzle[i][z] + ", ";
                            }

                            addMessage(getMessageArrayList(), rowDisplay);
                        }

                        setTroubleSpot(i);
                    }
                }
            }
        }

        return isRowInvalid;
    }

    // Validate the columns and check for incompletes
    boolean validateCols(int[][] puzzle) {
        setTroubleSpot(-1);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                for (int x = 0; x < 9; x++) {
                    if (puzzle[i][j] == 0) {
                        addMessage(getMessageArrayList(), "There are incomplete values in column " + (j + 1));
                        isColumnIncomplete = true;

                        if (!getTroubleArray().contains(j)) {
                            printPuzzleRowOrCol(puzzle, i, j, "Column", "Incomplete");

                            String columnDisplay = "Column[" + (j + 1) + "]: \n";

                            for (int z = 0; z < 9; z++) {
                                if (z == 8) {
                                    columnDisplay = columnDisplay + puzzle[z][j] + "\n";
                                    break;
                                }

                                columnDisplay = columnDisplay + puzzle[z][j] + ",\n";
                            }

                            addMessage(getMessageArrayList(), columnDisplay);
                        }

                        getTroubleArray().add(j);
                    }
                }
            }
        }

        setTroubleSpot(-1);

        getTroubleArray().clear();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                for (int x = 0; x < 9; x++) {
                    if ((puzzle[i][j] == puzzle[x][j]) && (x != i) && (puzzle[i][j] != 0)) {
                        addMessage(getMessageArrayList(), "There are duplicate values in column " + (j + 1));
                        isColumnInvalid = true;

                        if (!getTroubleArray().contains(j)) {
                            printPuzzleRowOrCol(puzzle, i, j, "Column", "Invalid");


                            String columnDisplay = "Column[" + (j + 1) + "]: \n";

                            for (int z = 0; z < 9; z++) {
                                if (z == 8) {
                                    columnDisplay = columnDisplay + puzzle[z][j] + "\n";
                                    break;
                                }

                                columnDisplay = columnDisplay + puzzle[z][j] + ",\n";
                            }

                            addMessage(getMessageArrayList(), columnDisplay);
                        }

                        getTroubleArray().add(j);
                    } else if (puzzle[i][j] > 9  || puzzle[i][j] < 0) {
                        addMessage(getMessageArrayList(), "There are non-single-digit values in column " + (j + 1));
                        isColumnInvalid = true;

                        if (!getTroubleArray().contains(j)) {
                            printPuzzleRowOrCol(puzzle, i, j, "Column", "Invalid");
                            String columnDisplay = "Column[" + (j + 1) + "]: \n";

                            for (int z = 0; z < 9; z++) {
                                if (z == 8) {
                                    columnDisplay = columnDisplay + puzzle[z][j] + "\n";
                                    break;
                                }

                                columnDisplay = columnDisplay + puzzle[z][j] + ",\n";
                            }

                            addMessage(getMessageArrayList(), columnDisplay);
                        }

                        getTroubleArray().add(j);
                    }
                }
            }
        }

        return isColumnInvalid();
    }

    // Validate the sections and check for incompletes
    boolean validateSections(int[][] puzzle) {
        int[][] puzzleSections = new int[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (j > 5) {
                    if (i > 5) {
                        puzzleSections[8][((i % 3) * (3)) + j % 3] = puzzle[i][j];
                    } else if (i > 2) {
                        puzzleSections[5][((i % 3) * (3)) + j % 3] = puzzle[i][j];
                    } else if (i >= 0) {
                        puzzleSections[2][((i % 3) * (3)) + j % 3] = puzzle[i][j];
                    }
                } else if (j > 2) {
                    if (i > 5) {
                        puzzleSections[7][((i % 3) * (3)) + j % 3] = puzzle[i][j];
                    } else if (i > 2) {
                        puzzleSections[4][((i % 3) * (3)) + j % 3] = puzzle[i][j];
                    } else if (i >= 0) {
                        puzzleSections[1][((i % 3) * (3)) + j % 3] = puzzle[i][j];
                    }
                } else if (j >= 0) {
                    if (i > 5) {
                        puzzleSections[6][((i % 3) * (3)) + j % 3] = puzzle[i][j];
                    } else if (i > 2) {
                        puzzleSections[3][((i % 3) * (3)) + j % 3] = puzzle[i][j];
                    } else if (i >= 0) {
                        puzzleSections[0][((i % 3) * (3)) + j % 3] = puzzle[i][j];
                    }
                }
            }
        }

        setTroubleSpot(-1);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                for (int x = 0; x < 9; x++) {
                    if ((puzzleSections[i][j] == puzzleSections[i][x]) && (x != j) && (puzzleSections[i][j] != 0)) {
                        isSectionInvalid = true;

                        if (getTroubleSpot() != i) {
                            addMessage(getMessageArrayList(), "There are duplicate values in section " + (i + 1));
                        }

                        setTroubleSpot(i);
                    } else if (puzzleSections[i][j] > 9 ||  puzzle[i][j] < 0) {
                        isSectionInvalid = true;
                        addMessage(getMessageArrayList(), "There are non-single-digit values in section " + (i + 1));
                    }
                }
            }
        }

        setTroubleSpot(-1);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                for (int x = 0; x < 9; x++) {
                    if (puzzleSections[i][j] == 0) {
                        isSectionIncomplete = true;

                        if (getTroubleSpot() != i) {
                            addMessage(getMessageArrayList(), "There are incomplete values in section " + (i + 1));
                        }

                        setTroubleSpot(i);
                    }
                }
            }
        }

        setPuzzleSections(puzzleSections);

        return true;
    }

    public static void main(String[] args) {
        Lab5_ValidateSudokuPuzzle labPuzzle = new Lab5_ValidateSudokuPuzzle();

        int puzzle[][] =
                {
                        {8, 2, 7, 1, 5, 4, 3, 9, 6},
                        {9, 6, 5, 3, 2, 7, 1, 4, 8},
                        {3, 4, 1, 6, 8, 9, 7, 5, 2},
                        {5, 9, 3, 4, 6, 8, 2, 7, 1},
                        {4, 7, 2, 5, 1, 3, 6, 8, 9},
                        {6, 1, 8, 9, 7, 2, 4, 3, 5},
                        {7, 8, 6, 2, 3, 5, 9, 1, 4},
                        {1, 5, 4, 7, 9, 6, 8, 2, 3},
                        {2, 3, 9, 8, 4, 1, 5, 6, 7}
                };


        int puzzleSections[][];

        System.out.println("\n********** STEP 4 **********\n");

        labPuzzle.printPuzzle(puzzle);

        System.out.println("\n********** STEPS 5 to 7 ********** [Information will show if applicable]");

        labPuzzle.validateRows(puzzle);
        labPuzzle.validateCols(puzzle);

        labPuzzle.validateSections(puzzle);

        puzzleSections = labPuzzle.getPuzzleSections();

        ArrayList<String> builtSections = labPuzzle.buildPuzzleSections(puzzleSections);

        System.out.println("\n********** STEP 8 to 10 ********** [Information will show if applicable]");

        labPuzzle.setDeDupedMessageArrayList((ArrayList<String>) labPuzzle.getMessageArrayList().stream().distinct().collect(Collectors.toList()));

        ArrayList<String> cleanDisplayList = labPuzzle.cleanAndMergeDisplayArrayList(labPuzzle.getDeDupedMessageArrayList(), builtSections);

        for (String string : cleanDisplayList) {
            System.out.println(string);
        }

        System.out.println("\n********** STEP 11 **********");

        if (labPuzzle.isRowInvalid || labPuzzle.isRowInvalid || labPuzzle.isSectionInvalid) {
            labPuzzle.isPuzzleInvalid = true;
        }

        if (labPuzzle.isRowIncomplete || labPuzzle.isColumnIncomplete || labPuzzle.isSectionIncomplete) {
            labPuzzle.isPuzzleIncomplete = true;
        }

        if (labPuzzle.isPuzzleInvalid) {
            System.out.println("The puzzle is invalid");
            System.exit(0);
        } else if (labPuzzle.isPuzzleIncomplete) {
            System.out.println("The puzzle is incomplete.");
            System.exit(0);
        } else {
            System.out.println("The entire puzzle is valid.");
        }

    }
}