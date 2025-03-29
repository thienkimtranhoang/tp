# Developer Guide

## Acknowledgements

{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

## Design & implementation

{Describe the design and implementation of the product. Use UML diagrams and short code snippets where applicable.}


## Product scope
### Target User Profile
The Finance Tracker App is designed primarily for **students** who need a simple and efficient tool to manage their finances. The app is ideal for:
- **Primary Audience**: Undergraduate and graduate students who wish to track their expenses, income, and savings.
- **Demographics**: Primarily students aged 18-30, tech-savvy, who can type fast and prefer typing over other means of input.
- **User Needs**:
    - Track income and expenses easily.
    - Set and manage specific saving goals (e.g., saving for a trip or a new gadget).
    - View financial records by date or category for better budgeting.
    - Receive insights and reminders to manage finances effectively.
- **User Challenges**:
    - Lack of financial management knowledge.
    - Limited budget management skills.
    - Difficulty in tracking daily expenses or small amounts that add up.
    - Need for a simple, mobile-friendly solution to manage finances on-the-go.
### Value proposition

Budgetflow is designed to address financial management challenges faced by students. It
offers a solution tailored to their needs.This app simplifies personal finance management
through the following key benefits:

- **Financial Insights**:  
  Users gain visibility into their spending patterns from comparing monthly expenses.

By combining ease of use, goal-driven features, and actionable insights, Budgetflow empowers students to:
- Build better financial habits.
- Reduce stress around money management.
- Achieve their financial goals with confidence.

## User Stories

|Version| As a ... | I want to ...  | So that I can ...                                           |
|--------|----------|----------------|-------------------------------------------------------------|
| v1.0    | uni student | log my daily expenses     | track where my money is going                                  |
|v1.0|new user| see usage instructions | refer to them when I forget how to use the application      |
|v2.0|user| find a to-do item by name | locate a to-do without having to go through the entire list |
|v2.0|user| filter by date | which allocation to which category                          |
| v2.0    | user        | update my expenses        | make changes to the expenses I already added                   |

## Non-Functional Requirements

{Give non-functional requirements}

## Glossary

* *glossary item* - Definition

## Instructions for manual testing

{Give instructions on how to do a manual product testing e.g., how to load sample data to be used for testing}
### 1. Adding Income
To add an income entry, use:
```sh
add category/Salary amt/1000.00 d/06-03-2025
```
This adds an income of **$1000.00** under the "Salary" category on **March 6, 2025**.

### 2. Logging an Expense
To log an expense, use:
```sh
log-expense category/Coffee desc/Coffee amt/3.50 d/06-03-2025
```
This logs an expense of **$3.50** for "Coffee" on **March 6, 2025**.

### 3. Viewing Expenses
To view all recorded expenses, use:
```sh
view-all-expense
```
This displays all logged expenses.

### 4. Listing All Incomes
To list all recorded incomes, use:
```sh
list income
```
This displays all income entries.

### 5. Filtering Income Records
- **By Date Range:**
  ```sh
  filter-income date from/01-03-2025 to/31-03-2025
  ```
  This filters income records from **March 1, 2025, to March 31, 2025**.

- **By Amount Range:**
  ```sh
  filter-income amount from/500 to/1500
  ```
  This filters incomes within the range **$500 to $1500**.

- **By Category:**
  ```sh
  filter-income category/Salary
  ```
  This filters income records under the **"Salary"** category.

### 6. Deleting an Income Entry
To delete an income entry with category **"Salary"**, use:
```sh
delete-income Salary
```

### 7. Updating an Expense Entry
To update an existing expense entry at index **1**, use:
```sh
update-expense index/1 category/drink desc/Coffee amt/4.00 d/06-04-2025
```
This updates the expense at **index 1** to category **"drink"**, with a description of **"Coffee"**, an amount of **$4.00**, and a date of **April 6, 2025**.

### 8. Comparing Expenses Between Two Months
To compare expenses between **March 2025 and April 2025**, use:
```sh
compare 03-2025 04-2025
```
This displays a comparison of expenses for **March 2025** vs. **April 2025**.

### 9. Deleting an Expense Entry
To delete an expense entry with the **"Coffee"** description, use:
```sh
delete-expense Coffee
```

### 10. Exiting the Application
To safely exit the application, use:
```sh
exit