# Nguyen Quy Dat - Project Portfolio Page

## Project: BudgetFlow:
Budgetflow is a personal finance management application designed to help students efficiently track their income,
expenses, and savings goals. With a simple, command-based interface, Budgetflow allows users to log their transactions,
filter them by category or date, and analyze their financial habits. It is written in Java with 4kLoC.

### Summary of Contributions
* __Code contributed__ [Reposense Link](https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=quydatnguyen&breakdown=true)
* __New features:__ Added ability to view all expenses
  * __What it does:__ This feature allows user to view all existing expenses that have been logged into the application.
  * __Justification:__ This feature helps user to keep track of all expenses that have been logged in, provides important information about their logged expenses. 
This plays an important assistance role in managing expenses for user, include viewing list changes after logging in new expenses or delete/ update an existing expenses.
  * __Highlights:__ The displayed information from this feature mostly matches with the keyword syntax for category, date, amount and description, which also assists
user in logging in commands. This feature also display the total expenses amount, which even helps user to quickly calculate total amount of expenses. 

* __New feature:__ Added ability to filter expenses based on category, description, amount or date.
  * __What it does:__ This feature allows user to filter and get expenses based on category, description, amount or date based on tag and keyword.
  * __Justification:__ This feature aim to assist querying expenses, allowing user to only display certain expenses after filtering. In addition, it helps user to search
for an expense quicker and help in managing expenses by filtering and focusing on smaller group of expenses. 
  * __Highlights:__ The filter for description allows partial matching, which helps user to even find expense based on description even if they don't exactly remember the description. 
The filter for amount and date also allows both exact matching and filtering from a range value, serving 

* __Documentation:__ 
  * __User Guide:__
    * Added documentation for `Listing expenses` and `Filtering expenses`.
    * Adjusted command summary.
    * Added outline for User Guide.
  * __Developer Guide:__
    * Added documentation for `Architecture`, `Ui`, `Parser`, `Command`, `ExpenseList`, `Listing All Expenses`, `Filtering Expenses`
    * Added manual test instructions for `list expenses` and `filter expenses`  
    * Added introduction and prerequisites

* __Community:__
  * Implemented and maintained general classes: UI, Parser, Command, ExpenseList, exceptions.
  * Refactored codes for v1.0.
  * Maintained issue tracker.
  * Reviewed team's Developer Guides and fix format error. 
  * PR reviewed (with non-trivial comments)
