package expensetracker.example.expense_tracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double amount;

    private String description;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private LocalDate date = LocalDate.now();  // Default to current date

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
