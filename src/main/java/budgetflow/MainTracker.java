package budgetflow;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainTracker {
    public static void main(String[] args) {
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.SEVERE);
        Logger parserLogger = Logger.getLogger("budgetflow.parser.Parser");
        parserLogger.setLevel(Level.SEVERE);
        FinanceTracker financeTracker = new FinanceTracker();
        financeTracker.run();
    }
}
