import org.junit.Test;
import org.junit.Assert;

public class TestCheckClause {
    @Test
    public void TestCheckClauseTrue(){
        Solver solver = new Solver();
        int [] assignment = new int [] {1};
        int [] clause = new int [] {1};
        boolean result = solver.checkClause(assignment,clause);
        Assert.assertTrue(result);
    }

    @Test
    public void TestCheckClauseFalse(){
        Solver solver = new Solver();
        int [] assignment = new int [] {-1};
        int [] clause = new int [] {1};
        boolean result = solver.checkClause(assignment,clause);
        Assert.assertFalse(result);
    }

}
