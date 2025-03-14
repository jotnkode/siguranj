package io.jotnkode.siguranj.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.jotnkode.siguranj.api.schema.UserPayload;
import io.jotnkode.siguranj.user.data.User;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
class UserManagmentTests {
    @Test
    void testPasswordRules() {
        PasswordChecker checker = new PasswordChecker();
        Double result = checker.score("ljhasDd89€%&");
        assertEquals(3.0d, result, "score is 3.0.");
    }

    @Test
    void testShannonDoherty() {
        PasswordChecker checker = new PasswordChecker();
        Double result = checker.shannonDoherty("fniaojjlkekkkkefhaiuhbias");
        assertEquals(3.48, result, "Shannon entropy should be 3.48");
    }

    @Autowired
    private UserManagement mgmt;

    @Test
    void testCreateNewUser() {
        UserPayload payload = new UserPayload();
        payload.setEmail("test@test.com");
        payload.setPassword("test345gwg€€€");
        User user = mgmt.createNewUser(payload).get();
        assertEquals(payload.getEmail(), user.getEmail(), "payload and entity email match.");

    }


}
