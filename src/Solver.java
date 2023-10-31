// IN1002 Introduction to Algorithms
// Coursework 2022/2023
//
// Submission by
// Eduardo Cook-Visinheski
// eduardo.cook-visinheski@city.ac.uk
//Can solve from clauseDB1 to clauseDB8, clauseDB9 takes 20 seconds

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class Solver {

    private int[][] clauseDatabase = null;
    private int numberOfVariables = 0;

    /* Your answers go below here */

    // Part A.1
    // Worst case complexity : ??? O(v)
    // Best case complexity : ??? O(1)
    public boolean checkClause(int[] assignment, int[] clause) {
        for (int literal : clause) {
            if (assignment[Math.abs(literal)] * literal > 0) {return true;}
        }
        return false;
    }


    // Part A.2
    // Worst case complexity : ??? O(cl)
    // Best case complexity : ??? O(1)
    public boolean checkClauseDatabase(int[] assignment, int[][] clauseDatabase) {

        for (int[] c : clauseDatabase) {
            for (int l : c) {if (l * assignment[Math.abs(l)] > 0) {return true;}}
            return false;
        }
        return true;
    }

    // Part A.3
    // Worst case complexity : ??? O(v)
    // Best case complexity : ??? O(1)
    public int checkClausePartial(int[] assignment, int[] clause) {
        boolean isSat = false;
        boolean isUnSat = true;
        for (int literal : clause) {
            if (literal<0) {if (assignment[Math.abs(literal)] == -1) {isSat = true;}}
            else {if (assignment[Math.abs(literal)] == 1) {isSat = true;}}
            if (assignment[Math.abs(literal)] == 0) {isUnSat = false;}
        }
        if (isSat) {return 1;}
        else if (isUnSat) {return -1;}
        else {return 0;}
    }

    // Part A.4
    // Worst case complexity : O(v)
    // Best case complexity : O(1)
    public int findUnit(int[] partialAssignment, int[] clause) {
        int unknownLiteral = 0;
        int numOfUnknown = 0;
        for (int literal : clause) {
            if (partialAssignment[literal] == 0) {
                unknownLiteral = literal;
                numOfUnknown++;
                if (numOfUnknown > 1) {return 0;}
            }
            else if (partialAssignment[literal] == 1 && literal > 0) {return 0;}
        }
        if (numOfUnknown == 1) {return unknownLiteral;}
        else {return 0;}
    }

    // Part B
    // I think this can solve ????
    //8 WORKING
    int[] checkSat(int[][] clauseDatabase) {
        for (int[] clauseChecked : clauseDatabase) {
            for (int currentLit : clauseChecked) {
                if (Math.abs(currentLit) > numberOfVariables) {numberOfVariables = Math.abs(currentLit);}}
        }
        int[] finalAssignment = new int[numberOfVariables + 1];
        Arrays.fill(finalAssignment, 0);
        if (backtracking(finalAssignment, clauseDatabase, 1)) {
            return finalAssignment;
        } else {
            return null;
        }
    }

    public boolean backtracking(int[] assignment, int[][] clauseDatabase, int varIndex) {
        if (varIndex > assignment.length - 1) {return true;}
        int uLit = unitFinder(assignment, clauseDatabase);
        if (uLit != 0) {
            if (uLit > 0) {assignment[Math.abs(uLit)] = 1;}
            else {assignment[Math.abs(uLit)] = -1;}
            return backtracking(assignment, clauseDatabase, varIndex);
        }
        assignment[varIndex] = 1;
        if (consistency(assignment, clauseDatabase)) {
            if (backtracking(assignment, clauseDatabase, varIndex + 1)) {return true;}
        }
        assignment[varIndex] = -1;
        if (consistency(assignment, clauseDatabase)) {
            if (backtracking(assignment, clauseDatabase, varIndex + 1)) {return true;}
        }
        assignment[varIndex] = 0;
        return false;
    }

    public boolean consistency(int[] assignment, int[][] clauseDatabase) {
        for (int[] ints : clauseDatabase) {
            int result = checkClausePartial(assignment, ints);
            if (result == -1) {
                return false;
            }
        }
        return true;
    }
    public int unitFinder(int[] partialAssignment, int[][] clauseDatabase) {
        int unitLiteral = unitPropagate(partialAssignment, clauseDatabase);
        while (unitLiteral > 0) {
            partialAssignment[unitLiteral] = (partialAssignment[unitLiteral] == 1) ? 1 : -1;
            unitLiteral = unitPropagate(partialAssignment, clauseDatabase);
        }
        return unitLiteral;
    }
    public int unitPropagate(int[] partialAssignment, int[][] clauseDatabase) {
        int uLit = -1;

        for (int[] clause : clauseDatabase) {
            int numULiterals = 0;

            for (int literal : clause) {
                int variable = Math.abs(literal);

                if (partialAssignment[variable] == 0) {
                    numULiterals++;
                    uLit = literal;
                }
                else if (partialAssignment[variable] == (literal > 0 ? 1 : -1)) {
                    numULiterals = 0;
                    break;}
            }

            if (numULiterals == 1) {
                if (uLit > 0) {return uLit;}
                else {return -uLit;}
            }
            else if (numULiterals == 0) {return 0;}}
        return 0;
    }

        /*****************************************************************\
         *** DO NOT CHANGE! DO NOT CHANGE! DO NOT CHANGE! DO NOT CHANGE! ***
         *******************************************************************
         *********** Do not change anything below this comment! ************
         \*****************************************************************/

    public static void main(String[] args) {
        try {
            Solver mySolver = new Solver();

            System.out.println("Enter the file to check");

            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(isr);
            String fileName = br.readLine();

            int returnValue = 0;

            Path file = Paths.get(fileName);
            BufferedReader reader = Files.newBufferedReader(file);
            returnValue = mySolver.runSatSolver(reader);

            return;

        } catch (Exception e) {
            System.err.println("Solver failed :-(");
            e.printStackTrace(System.err);
            return;

        }
    }

    public int runSatSolver(BufferedReader reader) throws Exception, IOException {

        // First load the problem in, this will initialise the clause
        // database and the number of variables.
        loadDimacs(reader);

        // Then we run the part B algorithm
        int [] assignment = checkSat(clauseDatabase);

        // Depending on the output do different checks
        if (assignment == null) {
            // No assignment to check, will have to trust the result
            // is correct...
            System.out.println("s UNSATISFIABLE");
            return 20;

        } else {
            // Cross check using the part A algorithm
            boolean checkResult = checkClauseDatabase(assignment, clauseDatabase);

            if (checkResult == false) {
                throw new Exception("The assignment returned by checkSat is not satisfiable according to checkClauseDatabase?");
            }

            System.out.println("s SATISFIABLE");

            // Check that it is a well structured assignment
            if (assignment.length != numberOfVariables + 1) {
                throw new Exception("Assignment should have one element per variable.");
            }
            if (assignment[0] != 0) {
                throw new Exception("The first element of an assignment must be zero.");
            }
            for (int i = 1; i <= numberOfVariables; ++i) {
                if (assignment[i] == 1 || assignment[i] == -1) {
                    System.out.println("v " + (i * assignment[i]));
                } else {
                    throw new Exception("assignment[" + i + "] should be 1 or -1, is " + assignment[i]);
                }
            }

            return 10;
        }
    }

    // This is a simple parser for DIMACS file format
    void loadDimacs(BufferedReader reader) throws Exception, IOException {
        int numberOfClauses = 0;

        // Find the header line
        do {
            String line = reader.readLine();

            if (line == null) {
                throw new Exception("Found end of file before a header?");
            } else if (line.startsWith("c")) {
                // Comment line, ignore
                continue;
            } else if (line.startsWith("p cnf ")) {
                // Found the header
                String counters = line.substring(6);
                int split = counters.indexOf(" ");
                numberOfVariables = Integer.parseInt(counters.substring(0,split));
                numberOfClauses = Integer.parseInt(counters.substring(split + 1));

                if (numberOfVariables <= 0) {
                    throw new Exception("Variables should be positive?");
                }
                if (numberOfClauses < 0) {
                    throw new Exception("A negative number of clauses?");
                }
                break;
            } else {
                throw new Exception("Unexpected line?");
            }
        } while (true);

        // Set up the clauseDatabase
        clauseDatabase = new int[numberOfClauses][];

        // Parse the clauses
        for (int i = 0; i < numberOfClauses; ++i) {
            String line = reader.readLine();

            if (line == null) {
                throw new Exception("Unexpected end of file before clauses have been parsed");
            } else if (line.startsWith("c")) {
                // Comment; skip
                --i;
                continue;
            } else {
                // Try to parse as a clause
                ArrayList<Integer> tmp = new ArrayList<Integer>();
                String working = line;

                do {
                    int split = working.indexOf(" ");

                    if (split == -1) {
                        // No space found so working should just be
                        // the final "0"
                        if (!working.equals("0")) {
                            throw new Exception("Unexpected end of clause string : \"" + working + "\"");
                        } else {
                            // Clause is correct and complete
                            break;
                        }
                    } else {
                        int var = Integer.parseInt(working.substring(0,split));

                        if (var == 0) {
                            throw new Exception("Unexpected 0 in the middle of a clause");
                        } else {
                            tmp.add(var);
                        }

                        working = working.substring(split + 1);
                    }
                } while (true);

                // Add to the clause database
                clauseDatabase[i] = new int[tmp.size()];
                for (int j = 0; j < tmp.size(); ++j) {
                    clauseDatabase[i][j] = tmp.get(j);
                }
            }
        }

        // All clauses loaded successfully!
        return;
    }

}