package expensetracker.example.expense_tracker.repository;

import expensetracker.example.expense_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
