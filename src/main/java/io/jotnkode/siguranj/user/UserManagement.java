package io.jotnkode.siguranj.user;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.function.Function;
import java.math.*;

import io.jotnkode.siguranj.api.schema.UserPayload;
import io.jotnkode.siguranj.user.data.User;
import io.jotnkode.siguranj.user.data.UserRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;


interface PasswordRuleScore {
    Double score(String password);
}

class PasswordChecker {
    private static final int PRECISION = 2;

    private List<PasswordRuleScore> rules;

    PasswordChecker() {
        this.rules = new ArrayList<PasswordRuleScore>();

        PasswordRuleScore containsCapitalLetter = (s) -> {
            if (s.matches(".*[A-Z].*")) {
                return 1.0d;
            }
            return 0.0d;
        };

        PasswordRuleScore containsNumber = (s) -> {
            if (s.matches(".*[0-9].*")) {
                return 1.0d;
            }
            return 0.0d;
        };

        PasswordRuleScore containsSpecialCharacter = (s) -> {
            if (s.matches(".*[^\\w\\s].*")) {
                return 1.0d;
            }
            return 0.0d;
        };

        this.rules.add(containsCapitalLetter);
        this.rules.add(containsNumber);
        this.rules.add(containsSpecialCharacter);
    }

    public Double score(String password) {
        Double totalScore = rules.stream().map(r -> r.score(password)).reduce((a, b) -> a + b).get();
        return totalScore;
    }

    public Double shannonDoherty(String password) {
        List<String> list = List.of(password.split(""));
        double length = list.size();
        Map<String, Double> res = list
            .stream()
            .distinct()
            .collect(Collectors.toMap(Function.identity(), v -> Collections.frequency(list, v) / length));

        res.replaceAll((_, v) -> -v * Math.log(v) / Math.log(2));

        Double entropy = res.values().stream().reduce((a, b) -> a + b).get();

        return new BigDecimal(entropy).setScale(PRECISION, RoundingMode.HALF_UP).doubleValue();
    }

}

@Service
public class UserManagement {



    @Autowired
    private UserRepository repo;


    public Optional<User> createNewUser(UserPayload userPayload) {
        String uuid = UUID.randomUUID().toString();
        PasswordChecker checker = new PasswordChecker();
        Double passwordScore = checker.score(userPayload.getPassword());
        Double shannonEntropy = checker.shannonDoherty(userPayload.getPassword());
        String encodedPassword = new Argon2().encode(userPayload.getPassword());

        User user = new User();

        user.setId(uuid);
        user.setEmail(userPayload.getEmail());
        user.setPassword(encodedPassword);
        user.setPasswordScore(passwordScore);
        user.setShannonEntropy(shannonEntropy);
        repo.save(user);

        return Optional.of(user);
    }
}
