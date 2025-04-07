package budgetflow.expense;

/**
 * Represents an expense entry in the budget tracking system.
 * An expense consists of a category, description, amount, and date.
 */
public class Expense {

    private String description;
    private double amount;
    private String date;
    private String category;

    /**
     * Default constructor for an Expense.
     * Initializes an empty expense entry.
     */
    public Expense() {}

    /**
     * Constructs an Expense with the given details.
     *
     * @param category   The category of the expense (e.g., "Food", "Transport").
     * @param description A description of the expense (e.g., "Lunch", "Taxi fare").
     * @param amount      The amount of money spent.
     * @param date        The date when the expense occurred (in DD-MM-YYYY format).
     */
    public Expense(String category, String description, double amount, String date) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }

    /**
     * Gets the description of the expense.
     *
     * @return The description of the expense.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the amount of the expense.
     *
     * @return The amount spent on the expense.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Gets the date of the expense.
     *
     * @return The date when the expense occurred.
     */
    public String getDate() {
        return date;
    }

    /**
     * Gets the category of the expense.
     *
     * @return The category of the expense (e.g., "Food", "Transport").
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category of the expense.
     *
     * @param category The category of the expense (e.g., "Food", "Transport").
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Returns a string representation of the expense.
     * The string includes the category, description, amount, and date.
     *
     * @return A formatted string representing the expense.
     */
    @Override
    public String toString() {
        return category + " | " + description + " | $" + String.format("%.2f", amount) + " | " + date;
    }

    /**
     * Sets the description of the expense.
     *
     * @param description The description of the expense.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the amount of the expense.
     *
     * @param amount The amount of money spent on the expense.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Sets the date of the expense.
     *
     * @param date The date when the expense occurred.
     */
    public void setDate(String date) {
        this.date = date;
    }
}
