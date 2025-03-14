package budgetflow;

public class Expense {
    private String description;
    private double amount;
    private String date;
    private String category;

    public Expense() {};

    public Expense(String description, double amount, String date) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = "Uncategorized";
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

    public String getCategory() {return category;}
    public void setCategory(String category) {this.category = category;}
}
