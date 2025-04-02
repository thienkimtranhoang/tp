# Tran Hoang Thien Kiim - Project Portfolio Page

## Project: BudgetFlow:
Budgetflow is a personal finance management application designed to help students efficiently track their income,
expenses, and savings goals. With a simple, command-based interface, Budgetflow allows users to log their transactions,
filter them by category or date, and analyze their financial habits. It is written in Java with 4kLoC.

### Summary of Contributions
* __New Feature:__ Added the ability to add basic incomes:
    * What it does: This feature allows users to log their expenses by providing a command `log-expense`. The user is
      prompted to enter the `amount`, `category`, and a brief `description` of the expense, which will be recorded and saved
      in Expense List.
    * Justification: This feature helps users keep track of their daily expenditures efficiently, ensuring that they are
      able to monitor their spending habits in real-time. By logging each expense, users can better understand where their
      money is going and make informed financial decisions.
    * Highlights: The feature supports categorization, making it easier for users to analyze their spending in specific
      areas such as food, transportation, and entertainment.
      <br>
      *__New Feature:__ Added the ability to set saving goal and see saving progress:
    * What it does: This feature allows users to modify previously logged expenses using the `update-expense` command.
      The user can adjust the `amount`, `category`, or `description` of an existing expense.
    * Justification: This feature enhances the flexibility of the application, as users may realize that they made a
      mistake while logging an expense or need to update the details of a transaction. It improves the overall user
      experience by providing them with more control over their financial records.
    * Highlights: This feature ensures that users can easily make adjustments to their finances without the need to
      delete and re-enter entire transactions, which can be cumbersome.
      <br>
      *__Enhancements to existing features:__
    * Added add-on information of saving goal and saving progress to list income command  [#100](https://github.com/AY2425S2-CS2113-T11a-1/tp/pull/100)
* __Code Contributed:__ [RepoSense Link](https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=thienkim&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2025-02-21)
  <br><br>
* __Project Management:__
    * Managed releases `v1.0` - `v2.0` (2 releases) on GitHub
      <br>
* __Documentation:__
    * User Guide:
        * 
        * Added solutions in Q&A section [#101](https://github.com/AY2425S2-CS2113-T11a-1/tp/pull/101)
    * Developer Guide:
        * Added documentation for add income command and set saving goal command [#101](https://github.com/AY2425S2-CS2113-T11a-1/tp/pull/101)
          <br>
* __Community:__
    * Reviewed Developer Guide of the team and fixed format error
    * PRs reviewed (with non-trivial review comments):
    * Reported bugs and suggestions for other teams in the class