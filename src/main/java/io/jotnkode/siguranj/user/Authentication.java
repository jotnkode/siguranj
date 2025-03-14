package io.jotnkode.siguranj.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Random;
import org.paseto4j.commons.PrivateKey;
import org.paseto4j.commons.PublicKey;
import org.paseto4j.commons.Version;
import org.paseto4j.version4.*;
import org.apache.tuweni.crypto.sodium.Signature;

import io.jotnkode.siguranj.api.schema.AuthenticationPayload;
import io.jotnkode.siguranj.api.schema.UserPayload;
import io.jotnkode.siguranj.api.schema.response.AuthenticationTokenResponsePayload;
import io.jotnkode.siguranj.user.data.User;
import io.jotnkode.siguranj.user.data.UserRepository;
import io.jotnkode.siguranj.user.exceptions.NoSuchUserException;
import io.jotnkode.siguranj.user.exceptions.AuthenticationFailedException;

@Service
public class Authentication {

    @Autowired
    private UserRepository repo;

    String generateToken(String tokenBody) {
        byte[] seed = new byte[32];
        Random rand = new Random();
        rand.nextBytes(seed);

        Signature.KeyPair keyPair = Signature.KeyPair.fromSeed(Signature.Seed.fromBytes(seed));
        PublicKey publicKey = new PublicKey(keyPair.publicKey().bytesArray(), Version.V4);
        PrivateKey privateKey = new PrivateKey(keyPair.secretKey().bytesArray(), Version.V4);

        String token = Paseto.sign(privateKey, tokenBody);
        return token;

    }

    public AuthenticationTokenResponsePayload authenticate(AuthenticationPayload authPayload) throws NoSuchUserException, AuthenticationFailedException {
        User user = repo.findByEmail(authPayload.getEmail()).orElseThrow(NoSuchUserException::new);

        Boolean pwdOk = new Argon2().matches(authPayload.getPassword(), user.getPassword());
        if (!pwdOk) {
            throw new AuthenticationFailedException();
        }

        String token = generateToken("SIGURAN_TEST");
        AuthenticationTokenResponsePayload resp = new AuthenticationTokenResponsePayload();
        resp.setToken(token);

        return resp;
    }
}
