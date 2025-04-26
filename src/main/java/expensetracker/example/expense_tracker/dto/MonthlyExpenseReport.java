package expensetracker.example.expense_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
@Builder
public class MonthlyExpenseReport {
    private double totalExpenses;
    private Map<String, Double> expensesByCategory;
}
