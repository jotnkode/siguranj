package io.jotnkode.siguranj.api.schema;
import lombok.Data;

@Data
public class UserPayload {
    private String email;
    private String password;
}
