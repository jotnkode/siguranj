package io.jotnkode.siguranj.user;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

/**
    Wrapper for org.springframework.security.crypto.argon2.Argon2PasswordEncoder. Configures the algorithm with costs, iterations and parallelism.
*/
public class Argon2 {
    private static final int MEMORY_COST = 5000;
    private static final int THREADS = 1;
    private static final int ITERATIONS = 2;
    private static final int HASH_LENGTH = 32;
    private static final int SALT_LENGTH = 16;

    private Argon2PasswordEncoder encoder = new Argon2PasswordEncoder(SALT_LENGTH, HASH_LENGTH, THREADS, MEMORY_COST, ITERATIONS);

    /**
    * Encode password using Argon2
    * @param password
    * @return The password hash.
    */
    public String encode(String password) {
        String hash = encoder.encode(password);
        return hash;
    }

    /**
    * Checks if provide cleartext password matches the hashed password.
    * @param password
    * @param hash
    * @return true if passwords matches.
    */
    public Boolean matches(String password, String hash) {
        return encoder.matches(password, hash);
    }
}
