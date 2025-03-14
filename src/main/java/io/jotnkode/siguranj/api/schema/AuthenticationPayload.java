package io.jotnkode.siguranj.api.schema;
import lombok.Data;

@Data
public class AuthenticationPayload {
    private String email;
    private String password;
}
