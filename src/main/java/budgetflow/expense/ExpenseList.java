package budgetflow.expense;

import budgetflow.exception.InvalidDateException;
import budgetflow.exception.InvalidNumberFormatException;
import budgetflow.exception.InvalidTagException;
import budgetflow.parser.DateValidator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ExpenseList {
    private static final String AMT_PATTERN = "[0-9]+(\\.[0-9]*)?";
    private static final String DATE_PATTERN = "dd-MM-yyyy";
    private static final String EMPTY_EXPENSE_LIST_MESSAGE =
            "There is currently no expense in your list right now. Please add more expenses to continue";
    private static final String ERROR_INVALID_DATE_FORMAT = "Please enter valid date format: dd-MM-yyyy";
    private static final String ASSERT_FAIL_INCORRECT_DATE_FORMAT =
            "Date inside the list is at incorrect date format";
    private static final String TAG_DESCRIPTION = "/desc";
    private static final String TAG_CATEGORY = "/category";
    private static final String TAG_AMOUNT = "/amt";
    private static final String TAG_DATE = "/d";
    private static final String TAG_AMOUNT_RANGE = "/amtrange";
    private static final String TAG_DATE_RANGE = "/drange";
    private static final String ERROR_INVALID_TAG = "Please enter valid tag: /desc | /amt| /d| /category";
    private static final String ERROR_INVALID_AMOUNT_FORMAT = "Please enter valid float number after /amt";

    private final ArrayList<Expense> innerList = new ArrayList<>();
    private double totalExpenses;

    public ExpenseList() {
        // Empty constructor.
    }

    public ExpenseList(Expense... expenses) {
        final List<Expense> initialExpenses = Arrays.asList(expenses);
        innerList.addAll(initialExpenses);
        updateTotalExpenses();
    }

    public ExpenseList(Collection<Expense> expenses) {
        innerList.addAll(expenses);
        updateTotalExpenses();
    }

    public int getSize() {
        return innerList.size();
    }

    /**
     * Get expense object from the list using its index.
     *
     * @param index of the desired expense to get.
     * @return the expense with matching index.
     */
    public Expense get(int index) {
        return innerList.get(index);
    }

    /**
     * Find all expenses by either description, category, amount or date.
     *
     * @param tag indicates whether user wants to find expense based on description, category, amount or date.
     * @param keyword keyword to find expense from indicated tag.
     * @return all matching expenses from indicated tags and keyword.
     * @throws InvalidTagException if user attempts to find expense from an unknown tag.
     * @throws InvalidNumberFormatException if keyword for tag /amt does not have valid double number format.
     * @throws InvalidDateException if keyword for tag /d does not follow dd-MM-yyyy format.
     */
    public ExpenseList getByTag(String tag, String keyword) throws InvalidTagException,
            InvalidNumberFormatException, InvalidDateException {
        return switch (tag) {
            case TAG_DESCRIPTION -> getExpenseByDesc(keyword);
            case TAG_CATEGORY -> getExpenseByCategory(keyword);
            case TAG_AMOUNT -> getExpenseByAmount(keyword);
            case TAG_DATE -> getExpenseByDate(keyword);
            case TAG_AMOUNT_RANGE -> getExpenseByAmountRange(keyword);
            case TAG_DATE_RANGE -> getExpenseByDateRange(keyword);
            default -> throw new InvalidTagException(ERROR_INVALID_TAG);
        };
    }

    private ExpenseList getExpenseByDateRange(String keyword) throws InvalidDateException {
        String[] dateRange = keyword.split("\\s+");
        if (!DateValidator.isValidDate(dateRange[0]) || !DateValidator.isValidDate(dateRange[1])) {
            throw new InvalidDateException(ERROR_INVALID_DATE_FORMAT);
        }
        LocalDate startDate = parseLocalDateFromString(dateRange[0]);
        LocalDate endDate = parseLocalDateFromString(dateRange[1]);
        ExpenseList outExpenses = new ExpenseList();
        for (int i = 0; i < this.getSize(); i++) {
            assert DateValidator.isValidDate(this.get(i).getDate()) : ASSERT_FAIL_INCORRECT_DATE_FORMAT;
            LocalDate date = parseLocalDateFromString(this.get(i).getDate());
            if (!date.isBefore(startDate) && !date.isAfter(endDate)) {
                outExpenses.add(this.get(i));
            }
        }
        return outExpenses;
    }

    private ExpenseList getExpenseByAmountRange(String keyword) throws InvalidNumberFormatException {
        ExpenseList outExpenses = new ExpenseList();
        String[] amountRange = keyword.split("\\s+");
        Double startAmount;
        Double endAmount;
        try {
            startAmount = Double.parseDouble(amountRange[0]);
            endAmount = Double.parseDouble(amountRange[1]);
        } catch (NumberFormatException e) {
            throw new InvalidNumberFormatException(ERROR_INVALID_AMOUNT_FORMAT);
        }
        for (int i = 0; i < this.getSize(); i++) {
            Double amount = this.get(i).getAmount();
            if (amount.compareTo(startAmount) >= 0 && amount.compareTo(endAmount) <= 0) {
                outExpenses.add(this.get(i));
            }
        }
        return outExpenses;
    }

    /**
     * Find expense object with description that contains query keyword.
     *
     * @param keyword keyword to find expense.
     * @return expense with description matching keyword.
     */
    private ExpenseList getExpenseByDesc(String keyword) {
        ExpenseList outExpenses = new ExpenseList();
        for (int i = 0; i < this.getSize(); i++) {
            String desc = this.get(i).getDescription();
            if (desc != null && desc.contains(keyword)) {
                outExpenses.add(this.get(i));
            }
        }
        return outExpenses;
    }

    /**
     * Find all expense objects from the same category.
     *
     * @param keyword the category user wishes to find.
     * @return all expenses from the category.
     */
    private ExpenseList getExpenseByCategory(String keyword) {
        ExpenseList outExpenses = new ExpenseList();
        for (int i = 0; i < this.getSize(); i++) {
            String category = this.get(i).getCategory();
            if (category.equals(keyword)) {
                outExpenses.add(this.get(i));
            }
        }
        return outExpenses;
    }

    /**
     * Find all expenses with matching amount to the query.
     *
     * @param keyword the amount to search.
     * @return all expenses with matching amount.
     * @throws InvalidNumberFormatException if amount keyword is not in a valid format.
     */
    private ExpenseList getExpenseByAmount(String keyword) throws InvalidNumberFormatException {
        if (!keyword.matches(AMT_PATTERN)) {
            throw new InvalidNumberFormatException(ERROR_INVALID_AMOUNT_FORMAT);
        }
        ExpenseList outExpenses = new ExpenseList();
        Double keywordAmount;
        try {
            keywordAmount = Double.parseDouble(keyword);
        } catch (NumberFormatException e) {
            throw new InvalidNumberFormatException(ERROR_INVALID_AMOUNT_FORMAT);
        }
        for (int i = 0; i < this.getSize(); i++) {
            Double amount = this.get(i).getAmount();
            if (amount.compareTo(keywordAmount) == 0) {
                outExpenses.add(this.get(i));
            }
        }
        return outExpenses;
    }

    private ExpenseList getExpenseByDate(String keyword) throws InvalidDateException {
        if (!DateValidator.isValidDate(keyword)) {
            throw new InvalidDateException(ERROR_INVALID_DATE_FORMAT);
        }
        LocalDate keywordDate = parseLocalDateFromString(keyword);
        ExpenseList outExpenses = new ExpenseList();
        for (int i = 0; i < this.getSize(); i++) {
            assert DateValidator.isValidDate(this.get(i).getDate()) : ASSERT_FAIL_INCORRECT_DATE_FORMAT;
            LocalDate date = parseLocalDateFromString(this.get(i).getDate());
            if (date.isEqual(keywordDate)) {
                outExpenses.add(this.get(i));
            }
        }
        return outExpenses;
    }

    private LocalDate parseLocalDateFromString(String keyword) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        return LocalDate.parse(keyword, inputFormatter);
    }

    /**
     * Adds an expense to the expense list.
     *
     * @param expense the expense object to be added.
     */
    public void add(Expense expense) {
        innerList.add(expense);
        totalExpenses += expense.getAmount();
    }

    public void delete(Expense expense) {
        totalExpenses -= expense.getAmount();
        innerList.remove(expense);
    }

    /**
     * Removes an expense in the list based on the index.
     *
     * @param index the index of the expense to be deleted.
     */
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

    /**
     * Recalculates the total expenses to be up to date.
     */
    public void updateTotalExpenses() {
        totalExpenses = 0.0;
        for (Expense expense : innerList) {
            totalExpenses += expense.getAmount();
        }
    }

    /**
     * Clears the expense list and resets total expenses to zero.
     */
    public void clear() {
        innerList.clear();
        totalExpenses = 0.0;
    }
}
