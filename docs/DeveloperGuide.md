# Developer Guide
<br><br>
![](diagrams/icon.png)<br>

Anak Agung Gde Yogi Pramana<br>

Darius Lee Qi Lun<br>

Goh Yik Bing<br>

Nguyen Quy Dat<br>

Tran Hoang Thien Kim<br>

## Table of Contents
* [Introduction](#introduction)
* [Getting Started](#getting-started)<br>
  * [Prerequisites](#prerequisites)<br>
  * [Setting Up](#setting-up)<br>
* [Design](#design) <br>
  * [Architecture](#architecture) <br>
  * [UI](#ui) <br>
  * [Storage](#storage) <br>
  * [Logic](#logic) <br>
  * [Model](#model) <br>
    * [Expense](#expense) <br>
* [Implementation](#implementation) <br>
  * [Adding Income](#add) <br>
  * [Logging an Expense](#log-expense) <br>
  * [Viewing All Expenses](#view-all-expense) <br>
  * [Listing All Incomes](#list-income) <br>
  * [Filtering Incomes by Amount or by Category](#filter-income amount/date) <br>
  * [Deleting an Income Entry](#delete-income) <br>
  * [Updating an Expense Entry](#update-expense) <br>
  * [Comparing Expenses Between Two Months](#compare) <br>
  * [Deleting an Expense Entry](#delete-expense) <br>
  * [Exiting the Application](#exit) <br>
* [Documentation](#documentation) <br>
* [Testing](#testing)<br>
* [Appendix A. Product scope](#appendix-a-product-scope)<br>
* [Appendix B. User stories](#appendix-b-user-stories)<br>
* [Appendix C. Non-functional requirements](#appendix-c-non-functional-requirements)<br>
* [Appendix D. Instructions for manual testing](#appendix-d-instructions-for-manual-testing)

## Introduction

## Getting Started

### Prerequisites

### Setting Up

## Design
This section outlines the various components of the application and explains how they interact to execute the program.
### Architecture

### Command

### Storage
__API__:`Storage.java`
The `Storage` component can save the list of incomes and expenses data in .txt format and read it back.

### Logic

### Model

#### Expense
__API__: 

## Acknowledgements

{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

## implementation

This section provides an overview of the implementation of key features.

### Adding Income

### Logging an Expense

This feature allows users to add expenses and relevant information about them (category, description, amount and date) to the expense list.

Here is an example of usage scenario:

Step 1: The user launches the application to log a spending to his expense list. The user executes
`log-expense category/Coffee desc/Coffee amt/3.50 d/06-03-2025` to add a new spending to the list.

Step 2: ...

...

### Viewing All Expenses

### Listing All Incomes

### Filtering Incomes by Amount or by Category

### Deleting an Income Entry

The `DeleteIncomeCommand` class extends the `Command` class and is responsible for processing the deletion of an income
entry based on its category. If the specified income category is not found in the list of incomes,
the command throws an exception (`UnfoundIncomeException`) to indicate the error.

This class adheres to the Command design pattern, 
where each command encapsulates a specific action (in this case, deleting an income entry).
The `execute` method implements the core logic for finding and removing the specified income entry.

Below is the Class Diagram of the `DeleteIncomeCommand` Class.

![Delete Income Command Class Diagram](diagrams/DeleteIncomeCommandClass.png)

Below is the Command sequence of the `DeleteIncomeCommand` Class.

![Delete Income Command Sequence](diagrams/DeleteIncomeCommandSequence.png)

### Updating an Income Entry

The UpdateIncomeCommand class extends the `Command` class and is responsible for updating an existing income record
in the system. It processes user input to extract updated income details (category, amount, and date),
validates the input, updates the corresponding income entry in the list, and saves the updated data to storage.
This class adheres to the Command design pattern,
where each command encapsulates a specific action (in this case, updating an income).

Below is the Class Diagram of the `UpdateIncomeCommand` Class.

![Delete Income Command Class Diagram](diagrams/UpdateIncomeCommandClass.png)

Below is the Command sequence of the `UpdateIncomeCommand` Class.

![Delete Income Command Sequence](diagrams/UpdateIncomeCommandSequence.png)

### Updating an Expense Entry

### Comparing Expenses Between Two Months

### Deleting an Expense Entry

The `DeleteExpenseCommand` class extends the `Command` class and is responsible for processing
the deletion of an expense entry based on its description.
If the specified expense description is not found in the list of expenses,
the command throws an exception (`UnfoundExpenseException`) to indicate the error.

This class adheres to the Command design pattern, 
where each command encapsulates a specific action (in this case, deleting an expense entry).
The `execute` method implements the core logic for finding and removing the specified expense entry.

Below is the Class Diagram of the `DeleteExpenseCommand` Class.

![Delete Expense Command Class Diagram](diagrams/DeleteExpenseCommandClass.png)

Below is the Command sequence of the `DeleteExpenseCommand` Class.

![Delete Expense Command Sequence](diagrams/DeleteExpenseCommandSequence.png)

### Exiting the Application

## Documentation

## Testing
JUnit is used for writing tests in this project. You can learn more about JUnit [here](https://se-education.org/guides/tutorials/gradle.html).
If you're using IntelliJ, you can run all tests by right-clicking the `test/java` folder and selecting `Run 'Tests'`, or by pressing `CRTL` + `SHIFT` + `F10`. 
Alternatively, you can open a console and run the command `gradelw clean test` (Mac/linus: `.gradlew clean test`).

###
{Describe the design and implementation of the product. Use UML diagrams and short code snippets where applicable.}


## Appendix A: Product scope
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

## Appendix B: User Stories

|Version| As a ... | I want to ...  | So that I can ...                                           |
|--------|----------|----------------|-------------------------------------------------------------|
| v1.0    | uni student | log my daily expenses     | track where my money is going                                  |
|v1.0|new user| see usage instructions | refer to them when I forget how to use the application      |
|v2.0|user| find a to-do item by name | locate a to-do without having to go through the entire list |
|v2.0|user| filter by date | which allocation to which category                          |
| v2.0    | user        | update my expenses        | make changes to the expenses I already added                   |

## Appendix C: Non-Functional Requirements

1. This application works on any computers that are Windows, macOS or Unix that has Java17.
## Glossary

* *glossary item* - Definition

## Appendix D: Instructions for manual testing

1. Launching
  * Download the jar file [here](insert git JAR file link here) and place it in an empty folder.
  * Open the command prompt and enter the command `java -jar tP.jar`.
  * Expected: The application shows a welcome message.

2. Adding expenses and incomes
  * Test case: `log-expense category/Coffee desc/Coffee amt/3.50 d/06-03-2025`
  * Expected: Confirmation message to indicate that the expense has been added successfully.
  * Test case: `add category/Salary amt/1000.00 d/06-03-2025`
  * Expected: Confirmation message to indicate that the income has been added successfully.

3. Editing expenses
  * Test case: `update-expense index/<index> category/drink desc/Coffee amt/4.00 d/06-04-2025` where `index` is the index of the expense list to be updated and `category`, `amt` and `d` are the category, amount and date to be changed to.
  * Expected: Confirmation message to indicate that the expense has been updated successfully.

