package budgetflow;

public class Expense {
    private String description;
    private double amount;
    private String date;

    public Expense(String description, double amount, String date) {
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }
}