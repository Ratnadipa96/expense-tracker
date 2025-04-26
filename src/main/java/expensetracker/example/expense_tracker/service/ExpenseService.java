package expensetracker.example.expense_tracker.service;

import expensetracker.example.expense_tracker.dto.MonthlyExpenseReport;
import expensetracker.example.expense_tracker.dto.UpdateExpenseRequest;
import expensetracker.example.expense_tracker.exception.ResourceNotFoundException;
import expensetracker.example.expense_tracker.model.Expense;
import expensetracker.example.expense_tracker.model.User;
import expensetracker.example.expense_tracker.repository.ExpenseRepository;
import expensetracker.example.expense_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    // ⭐ Create Expense (example with User exception handling)
    public Expense createExpense(Long userId, Expense expense) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        expense.setUser(user);
        expense.setDate(expense.getDate() != null ? expense.getDate() : LocalDate.now());
        return expenseRepository.save(expense);
    }

    // ⭐ Update Expense (example with Expense exception handling)
    public Expense updateExpense(Long expenseId, UpdateExpenseRequest request) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + expenseId));

        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setDescription(request.getDescription());
        expense.setDate(request.getDate() != null ? request.getDate() : expense.getDate());

        return expenseRepository.save(expense);
    }

    // ⭐ Delete Expense
    public void deleteExpense(Long expenseId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + expenseId));

        expenseRepository.delete(expense);
    }

    // ⭐ Get Expense by ID
    public Expense getExpenseById(Long expenseId) {
        return expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + expenseId));
    }

    // ⭐ Get All Expenses for a User
    public List<Expense> getAllExpensesForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return expenseRepository.findByUserId(user.getId());
    }

    // Existing Methods (Calculations)...

    public double getTotalExpensesInDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        List<Expense> expenses = expenseRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
        return expenses.stream().mapToDouble(Expense::getAmount).sum();
    }

    public Map<String, Double> getTotalAmountPerCategory(Long userId) {
        List<Expense> expenses = expenseRepository.findByUserId(userId);
        return expenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Expense::getAmount)
                ));
    }

    // ⭐ New: Generate Monthly Report
    public MonthlyExpenseReport generateMonthlyReport(Long userId, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<Expense> expenses = expenseRepository.findByUserIdAndDateBetween(userId, startDate, endDate);

        double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();

        Map<String, Double> expensesByCategory = expenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Expense::getAmount)
                ));

        return MonthlyExpenseReport.builder()
                .totalExpenses(totalExpenses)
                .expensesByCategory(expensesByCategory)
                .build();
    }
}
