# BudgetFlow User Guide

### Welcome to Budgetflow!

Managing finances as a student can be overwhelming—keeping track of expenses, ensuring savings goals are met, and
understanding spending habits all require time and effort. **Budgetflow** is here to simplify this process for you!

Designed with students in mind, Budgetflow helps you **effortlessly track income and expenses, set savings goals, and
gain insights into your financial habits.** Whether you're saving for your next trip, managing your daily coffee budget,
or planning your monthly expenses, Budgetflow ensures you stay in control.

With a **simple, command-based interface**, Budgetflow is lightweight, efficient, and fast—perfect for students who
prefer typing over navigating complex apps. You can log your transactions, filter them based on category or date, and
even compare monthly expenses at

---

### 1. Logging an Expense
**Description:** Logs a new expense with a category, description, amount, and date.  
**Command:**
```plaintext
log-expense category/<CATEGORY> desc/<DESCRIPTION> amt/<AMOUNT> d/<DATE>
```
**Example:**
```plaintext
log-expense category/Coffee desc/Coffee amt/3.50 d/06-03-2025
```
**Output:**
```plaintext
Saving goal retrieved: 0.0
Expense logged: Coffee | Coffee | $3.50 | 06-03-2025
```

---

### 2. Updating an Expense Entry
**Description:** Modifies an existing expense entry.  
**Command:**
```plaintext
update-expense index/<INDEX> category/<NEW_CATEGORY> desc/<NEW_DESCRIPTION> amt/<NEW_AMOUNT> d/<NEW_DATE>
```
**Example:**
```plaintext
update-expense index/1 category/drink desc/Coffee amt/4.00 d/06-04-2025
```
**Output:**
```plaintext
Expense updated at index 1: drink | Coffee | $4.00 | 06-04-2025. Total Expenses: $4.00
```

---

### 3. Deleting an Income
**Description:** Deletes an instance of an Income entry.
**Command:**
```plaintext
delete-income [NAME OF INCOME ENTRY]
```
**Output:**
```plaintext
Saving goal retrieved: [REMAINING OF SAVING GOAL]
Income deleted: [NAME OF INCOME ENTRY]
```

---

### 4. Deleting an Expense
**Description:** Deletes an instance of an Expense entry.  
**Command:**
```plaintext
delete-expense [NAME OF EXPENSE ENTRY]
```
**Output:**
```plaintext
Saving goal retrieved: [REMAINING OF SAVING GOAL]
Income deleted: [NAME OF EXPENSE ENTRY]
```

---

### 5. Comparing two Monthly Expenses 
**Description:** Compares Monthly expenses between two months  
**Command:**
```plaintext
compare MM-YYYY MM-YYYY
```
**Output:**
```plaintext
Total expenses for MM-YYYY: [AMOUNT]
Total expenses for MM-YYYY: [AMOUNT]
```

---

### 6. Updating an Income
**Description:** Updates an instance of an Income entry.  
**How it works** The user only has to write the Index and the thing that the user would like to update. 
E.g. If the user only wants to update the amt, he can run `update-income INDEX amt/[UPDATED_AMOUNT]`
**Command:**
```plaintext
update-income [INDEX] category/[UPDATED_CATEGORY] amt/[UPDATED_AMOUNT] d/[UPDATED_DATE]
```
**Output:**
```plaintext
Income updated: [UPDATED_CATEGORY], Amount: [UPDATED_AMOUNT], Date: [UPDATED_DATE]
```

---
### 7. Setting a Savings Goal
**Description:** Sets a savings goal with a specified amount and optional target date.  
**Command:**
```plaintext
set-saving-goal amt/<GOAL_AMOUNT> 
```
**Example:**
```plaintext
set-saving-goal amt/5000
```
**Output:**
```plaintext
Savings goal set: $5000 by 31-12-2025.
```
---

### 8. Viewing all expenses
**Description:** Views all existing expenses that have been logged with the total sum of expenses' amount.   
**Command:**
```plaintext
view-all-expense
```
**Output:**
* For empty list:
```plaintext
No expenses have been logged yet.
```
* For non-empty list:
```plaintext
Expenses log:
[INFORMATION OF EXPENSES]
```

Output example: 
```plaintext
Expenses log:
1 | Coffee | Coffee | $3.50 | 06-03-2025
2 | drink | Juice | $6.50 | 06-04-2015
Total Expenses: $10.00
```
---

### 9. Filtering expenses
**Overview:** Filters expenses based the keyword and given condition.  
 
#### 9.1. Filtering expenses based on category
**Description:** Filters all expenses with category that exactly match with keyword.
**Command:**
```plaintext
find-expense /category [CATEGORY]
```
* tag `/category` indicates filtering by category.
* keyword `[CATEGORY]` containing the name of category we want to find expenses from.
* __NOTE__: the tag and keywords are __CASE SENSITIVE__

**Output:**
```plaintext
Here are all matching expenses:
[INFORMATION OF MATCHING EXPENSES]
```
**Example:**
```plaintext
find-expense /category drink
Here are all matching expenses:
drink | Juice | $6.50 | 06-04-2015
```
#### 9.2. Filtering expenses based on description
**Description:** Filters all expenses with description that contain keyword.
**Command:**
```plaintext
find-expense /desc [DESC]
```
* tag `/desc` indicates filtering by description
* keyword `[DESC]` containing the name of category we want to find expenses from.
* __NOTE__: the tag and keywords are __CASE SENSITIVE__

**Output:**
```plaintext
Here are all matching expenses:
[INFORMATION OF MATCHING EXPENSES]
```
**Example:**
```plaintext
find-expense /desc Coffee
Here are all matching expenses:
Coffee | Coffee | $3.50 | 06-03-2025
```
#### 9.3. Filtering expenses based on amount
**Description:** Filters all expenses that matches certain amount value.
**Command:**
```plaintext
find-expense /amt [AMT]
```
* tag `/amt` indicates filtering by amount
* keyword `[AMT]` containing the amount value of expenses we want to search from.
* __NOTE__: 
  * the tag is __CASE SENSITIVE__. 
  * Value of `[AMT]` must be a valid integer or decimal number with digit before dot. Some examples of keyword that may lead to error: `string`, `.12` (no digit before dot), `5..6` (multiple dots).

**Output:**
```plaintext
Here are all matching expenses:
[INFORMATION OF MATCHING EXPENSES]
```
**Example:**
```plaintext
find-expense /amt 3.50
Here are all matching expenses:
Coffee | Coffee | $3.50 | 06-03-2025
---
```
#### 9.4. Filtering expenses based on amount range
**Description:** Filters all expenses with amount value within indicated range.
**Command:**
```plaintext
find-expense /amtrange [AMT1] [AMT2]
```
* tag `/amtrange` indicates filtering by amount range.
* keywords `[AMT1]` and `[AMT2]` indicating the range of amount value we want to filter expenses from.
  * `[AMT1]`: The lower bound of value range used for filtering.
  * `[AMT2]`: The upper bound of value range used for filtering.
* __NOTE__:
    * the tag is __CASE SENSITIVE__.
    * Values of `[AMT1]` and `[AMT2]` must be valid integer or decimal number with digit before dot. Some examples of keyword that may lead to error: `string`, `.12` (no digit before dot), `5..6` (multiple dots).
    * `[AMT1]` and `[AMT2]` must be separated by at least 1 space ` `.

**Output:**
```plaintext
Here are all matching expenses:
[INFORMATION OF MATCHING EXPENSES]
```
**Example:**
```plaintext
find-expense /amtrange 3.00 6.50
Here are all matching expenses:
Coffee | Coffee | $3.50 | 06-03-2025
drink | Juice | $6.50 | 06-04-2015
---
```

#### 9.5. Filtering expenses based on date
**Description:** Filters all expenses that matches a certain date.
**Command:**
```plaintext
find-expense /d [DATE]
```
* tag `/d` indicates filtering by date.
* keyword `[DATE]` indicate the date to filter expenses from.
* __NOTE__:
    * the tag is __CASE SENSITIVE__.
    * Value of `[DATE]` must follow format dd-MM-yyyy.

**Output:**
```plaintext
Here are all matching expenses:
[INFORMATION OF MATCHING EXPENSES]
```
**Example:**
```plaintext
find-expense /d 06-03-2025
Here are all matching expenses:
Coffee | Coffee | $3.50 | 06-03-2025
---
```
#### 9.6. Filtering expenses based on date range
**Description:** Filters all expenses with the date lies within an indicated date range.
**Command:**
```plaintext
find-expense /drange [DATE1] [DATE2]
```
* tag `/drange` indicates filtering by date range.
* keywords `[DATE1]` and `[DATE2]` respectively indicating the first date to start searching from and the last date to search to.
* __NOTE__:
    * the tag is __CASE SENSITIVE__.
    * Values of `[DATE1]` and `[DATE2]` must follow format dd-MM-yyyy.
    * `[DATE1]` and `[DATE2]` must be separated by at least 1 space ` `.

**Output:**
```plaintext
Here are all matching expenses:
[INFORMATION OF MATCHING EXPENSES]
```
**Example:**
```plaintext
find-expense /drange 06-03-2022 07-12-2025
Here are all matching expenses:
Coffee | Coffee | $3.50 | 06-03-2025
---
```

### 10. Exiting the Application
**Description:** Safely exits the BudgetFlow application.  
**Command:**
```plaintext
exit
```
**Output:**
```plaintext
Goodbye!
```

## FAQ

**Q: Does BudgetFlow support multiple currencies?**  
A: Currently, BudgetFlow only supports tracking in one currency at a time.

**Q: Can I edit or delete an entry after logging it?**  
A: Yes! You can use the `update-expense` or `delete-expense` commands to modify or remove an expense entry. Similarly, you can use `delete-income` to remove an income entry.

---

## Command Summary

| **Command** | **Description** |
|------------|---------------|
| `add category/Salary amt/1000.00 d/06-03-2025` | Adds an income entry with the specified category, amount, and date. |
| `log-expense category/Coffee desc/Coffee amt/3.50 d/06-03-2025` | Logs an expense entry with details. |
| `view-all-expense` | Displays all logged expenses. |
| `list income` | Shows all recorded incomes. |
| `filter-income date from/01-03-2025 to/31-03-2025` | Filters income entries within a specific date range. |
| `filter-income amount from/500 to/1500` | Filters income entries within a specified amount range. |
| `filter-income category/Salary` | Filters income by category. |
| `delete-income Salary` | Deletes an income entry with the category *Salary*. |
| `update-expense index/1 category/drink desc/Coffee amt/4.00 d/06-04-2025` | Updates an expense entry at index 1. |
| `compare 03-2025 04-2025` | Compares total expenses between March and April 2025. |
| `delete-expense Coffee` | Deletes an expense entry with the description *Coffee*. |
| `exit` | Exits the application safely. |
