package expensetracker.example.expense_tracker.controller;

import expensetracker.example.expense_tracker.dto.CreateExpenseRequest;
import expensetracker.example.expense_tracker.dto.MonthlyExpenseReport;
import expensetracker.example.expense_tracker.dto.UpdateExpenseRequest;
import expensetracker.example.expense_tracker.model.Expense;
import expensetracker.example.expense_tracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<Expense> createExpense(@RequestBody CreateExpenseRequest createExpenseRequest,
                                                 @RequestParam Long userId) {
        Expense expense = expenseService.createExpense(createExpenseRequest, userId);
        return ResponseEntity.ok(expense);
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses(@RequestParam Long userId) {
        List<Expense> expenses = expenseService.getAllExpenses(userId);
        return ResponseEntity.ok(expenses);
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long expenseId,
                                                 @RequestBody UpdateExpenseRequest updateExpenseRequest) {
        Expense updatedExpense = expenseService.updateExpense(expenseId, updateExpenseRequest);
        return ResponseEntity.ok(updatedExpense);
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long expenseId) {
        expenseService.deleteExpense(expenseId);
        return ResponseEntity.ok("Expense deleted successfully");
    }

    @GetMapping("/total")
    public ResponseEntity<Double> getTotalExpensesInDateRange(@RequestParam Long userId,
                                                              @RequestParam String startDate,
                                                              @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        double total = expenseService.getTotalExpensesInDateRange(userId, start, end);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/category-totals")
    public ResponseEntity<Map<String, Double>> getTotalAmountPerCategory(@RequestParam Long userId) {
        Map<String, Double> categoryTotals = expenseService.getTotalAmountPerCategory(userId);
        return ResponseEntity.ok(categoryTotals);
    }

    // ⭐ New: Monthly report API
    @GetMapping("/report/monthly")
    public ResponseEntity<MonthlyExpenseReport> getMonthlyReport(@RequestParam Long userId,
                                                                 @RequestParam int year,
                                                                 @RequestParam int month) {
        MonthlyExpenseReport report = expenseService.generateMonthlyReport(userId, year, month);
        return ResponseEntity.ok(report);
    }
}
