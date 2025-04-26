
package expensetracker.example.expense_tracker.service;

import expensetracker.example.expense_tracker.dto.CreateExpenseRequest;
import expensetracker.example.expense_tracker.model.Expense;
import expensetracker.example.expense_tracker.model.User;
import expensetracker.example.expense_tracker.repository.ExpenseRepository;
import expensetracker.example.expense_tracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ExpenseService expenseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateExpense() {
        CreateExpenseRequest request = new CreateExpenseRequest();
        request.setAmount(100.0);
        request.setCategory("Food");
        request.setDescription("Lunch");
        request.setDate(LocalDate.now());

        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Expense savedExpense = new Expense();
        savedExpense.setId(1L);
        savedExpense.setAmount(100.0);
        savedExpense.setCategory("Food");
        savedExpense.setUser(user);

        when(expenseRepository.save(any(Expense.class))).thenReturn(savedExpense);

        Expense result = expenseService.createExpense(request, 1L);

        assertNotNull(result);
        assertEquals(100.0, result.getAmount());
        assertEquals("Food", result.getCategory());
    }

    @Test
    void testGetTotalExpensesInDateRange() {
        when(expenseRepository.findByUserIdAndDateBetween(eq(1L), any(), any()))
                .thenReturn(List.of(new Expense(1L, 50.0, "Food", "Lunch", LocalDate.now(), null),
                        new Expense(2L, 100.0, "Travel", "Bus", LocalDate.now(), null)));

        double total = expenseService.getTotalExpensesInDateRange(1L, LocalDate.now().minusDays(1), LocalDate.now());

        assertEquals(150.0, total);
    }
}
