package io.jotnkode.siguranj.user.data;
import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Data
@Entity
public class User {
    @Id
    private String id;
    private String email;
    private String password;
    private Double passwordScore;
    private Double shannonEntropy;
}
