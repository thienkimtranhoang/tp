# Goh Yik Bing - Project Portfolio Page

## Project: BudgetFlow:
Budgetflow is a personal finance management application designed to help students efficiently track their income,
expenses, and savings goals. With a simple, command-based interface, Budgetflow allows users to log their transactions,
filter them by category or date, and analyze their financial habits. It is written in Java with 4kLoC.

### Summary of Contributions
* __New Feature:__ Added the ability to delete Expenses:
  * What it does: This feature allows users to delete their expenses by providing a command `delete-expense`. The user 
  then has to enter `delete-expense [DESCRIPTION]`, which will then delete the corresponding entry from the list.
  * Justification: This feature helps users to be able to delete expenses that were wrongly accounted for.

* __New Feature:__ Added the ability to delete Income:
    * What it does: This feature allows users to delete their expenses by providing a command `delete-income`. The user
      then has to enter `delete-income [DESCRIPTION]`, which will then delete the corresponding entry from the list.
    * Justification: This feature helps users to be able to delete income that were wrongly accounted for.

* __New Feature:__ Added the ability to update Income:
  * What it does: This feature allows users to modify previously logged income using the `update-income` command.
  The user can adjust the `amount`, `category`, or `description` of an existing expense.
  * Justification: This feature enhances the flexibility of the application, as users may realize that they made a
  mistake while logging an income or need to update the details of a transaction. It improves the overall user
  experience by providing them with more control over their financial records.

* __New Feature:__ Added a help function:
  * What it does: This feature allows users to use the `help` command to learn what to type based on what they need.
  * Justification: This feature enhances the User experience as the user can know exactly what to type based on
  the description of the features given in the help function. 

* __New Feature:__ Added a DateValidator method:
    * What it does: This method allows the program to be able to accurately identify whether the dates entered by the
      user is a valid date so that all dates that are inside the program is a valid date.

* __Code Contributed:__ [RepoSense Link](https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2025-02-21)__

* __Documentation:__
    * User Guide:
      * Added documentation for delete-expense, delete-income, compare expenses and update-income command [#111](https://github.com/AY2425S2-CS2113-T11a-1/tp/pull/111)
    * Developer Guide:
      * Added delete-income, delete-expense, compare expenses and update-income for Implementation[#111](https://github.com/AY2425S2-CS2113-T11a-1/tp/pull/111)
* __Community:__
    * PRs reviewed (with non-trivial review comments): [#98](https://github.com/AY2425S2-CS2113-T11a-1/tp/pull/98)