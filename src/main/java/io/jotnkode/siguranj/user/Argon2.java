package io.jotnkode.siguranj.user;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

public class Argon2 {
    private static final int MEMORY_COST = 10000;
    private static final int THREADS = 1;
    private static final int ITERATIONS = 4;
    private static final int HASH_LENGTH = 32;
    private static final int SALT_LENGTH = 16;

    private Argon2PasswordEncoder encoder = new Argon2PasswordEncoder(SALT_LENGTH, HASH_LENGTH, THREADS, MEMORY_COST, ITERATIONS);

    public String encode(String password) {
        String hash = encoder.encode(password);
        return hash;
    }

    public Boolean matches(String password, String hash) {
        return encoder.matches(password, hash);
    }
}
