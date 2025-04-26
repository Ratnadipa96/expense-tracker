package expensetracker.example.expense_tracker.dto;

import lombok.Data;

@Data
public class CreateExpenseRequest {
    private double amount;
    private String description;
    private String category;
}
