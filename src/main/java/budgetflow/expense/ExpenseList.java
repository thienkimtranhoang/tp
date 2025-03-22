package budgetflow.expense;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ExpenseList {
    public static final String EMPTY_EXPENSE_LIST_MESSAGE =
            "There is currently no expense in your list right now. Please add more expenses to continue";
    private final ArrayList<Expense> innerList = new ArrayList<>();
    private double totalExpenses;

    public ExpenseList() {}

    public ExpenseList(Expense... expenses) {
        final List<Expense> initialExpenses = Arrays.asList(expenses);
        innerList.addAll(initialExpenses);
    }

    public ExpenseList(Collection<Expense> expenses) {
        innerList.addAll(expenses);
    }

    public int getSize() {
        return innerList.size();
    }

    /**
     * Get expense object from the list using its index
     * @param index of the desired expense to get
     * @return the expense with matching index
     */
    public Expense get(int index) {
        return innerList.get(index);
    }

    /**
     * Find expense object with description that contains query keyword
     * @param keyword keyword to find expense
     * @return expense with des description matching keyword or null expense object if not found
     */
    public ExpenseList get(String keyword) {
        ExpenseList outExpenses = new ExpenseList();
        for (int i = 0; i < this.getSize(); i++) {
            String desc = this.get(i).getDescription();
            if (desc != null && desc.contains(keyword)) {
                outExpenses.add(this.get(i));
            }
        }
        return outExpenses;
    }

    public void add(Expense expense) {
        innerList.add(expense);
        totalExpenses += expense.getAmount();
    }

    public void delete(Expense expense) {
        totalExpenses -= expense.getAmount();
        innerList.remove(expense);
    }

    public void delete(int index) {
        Expense deleteExpense = this.get(index);
        totalExpenses -= deleteExpense.getAmount();
        innerList.remove(index);
    }

    @Override
    public String toString() {
        if (innerList.isEmpty()) {
            return EMPTY_EXPENSE_LIST_MESSAGE;
        }
        StringBuilder sb = new StringBuilder();
        for (Expense expense : innerList) {
            sb.append(expense.toString()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }
}
