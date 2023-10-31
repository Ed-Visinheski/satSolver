public class Main {

    public static void main(String[] args){
        Solver solver = new Solver();
//        System.out.println("Testing");
        int [] assignment = {0,-1,-1};
        int [] clause = {1,2,-3};
        int[][] clauseDB = {{1,2,3},{4,5,6}};
//        System.out.println(solver.checkClause(assignment,clause));
//        System.out.println("CheckClause ran");
        int [][] clauseDatabase = {{1,2},{3,4}};
        System.out.println(solver.checkClauseDatabase(assignment, clauseDatabase));
        //System.out.println(solver.findUnit(assignment,clause));
    }
}
