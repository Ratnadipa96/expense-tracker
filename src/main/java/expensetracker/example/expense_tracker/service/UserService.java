package expensetracker.example.expense_tracker.service;

import expensetracker.example.expense_tracker.dto.RegisterRequest;
import expensetracker.example.expense_tracker.dto.LoginRequest;
import expensetracker.example.expense_tracker.model.User;
import expensetracker.example.expense_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void registerUser(RegisterRequest registerRequest) {
        User user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())  // This will be hashed in the User model
                .build();
        userRepository.save(user);
    }

    public String loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            // You can return a JWT token here
            return "Login successful"; // Placeholder
        }
        return "Invalid credentials"; // Placeholder
    }
}
