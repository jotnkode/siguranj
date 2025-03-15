package io.jotnkode.siguranj.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.util.Optional;
import java.util.Random;
import org.paseto4j.commons.PrivateKey;
import org.paseto4j.commons.PublicKey;
import org.paseto4j.commons.Version;
import org.paseto4j.version4.*;
import java.security.*;
import lombok.extern.slf4j.Slf4j;

import io.jotnkode.siguranj.api.schema.AuthenticationPayload;
import io.jotnkode.siguranj.api.schema.UserPayload;
import io.jotnkode.siguranj.api.schema.response.AuthenticationTokenResponsePayload;
import io.jotnkode.siguranj.user.data.User;
import io.jotnkode.siguranj.user.data.UserRepository;
import io.jotnkode.siguranj.user.exceptions.NoSuchUserException;
import io.jotnkode.siguranj.user.exceptions.AuthenticationFailedException;

@Service
@Slf4j
public class Authentication {

    @Autowired
    private UserRepository repo;

    String generateToken(String tokenBody) {
        byte[] seed = new byte[32];
        Random rand = new Random();
        rand.nextBytes(seed);


        try {
            Security.addProvider(new BouncyCastleProvider());
            KeyPairGenerator generator = KeyPairGenerator.getInstance("EdDSA");
            generator.initialize(256);
            KeyPair keyPair = generator.generateKeyPair();

            PublicKey publicKey = new PublicKey(keyPair.getPublic(), Version.V4);
            PrivateKey privateKey = new PrivateKey(keyPair.getPrivate(), Version.V4);

            String token = Paseto.sign(privateKey, tokenBody);
            return token;
        } catch(NoSuchAlgorithmException e) {
            log.error("Can't initialize generator with RSA.");
        }

        return "";
    }

    public AuthenticationTokenResponsePayload authenticate(AuthenticationPayload authPayload) throws NoSuchUserException, AuthenticationFailedException {
        log.info("Start authenticating user...");
        User user = repo.findByEmail(authPayload.getEmail()).orElseThrow(NoSuchUserException::new);

        Boolean pwdOk = new Argon2().matches(authPayload.getPassword(), user.getPassword());
        if (!pwdOk) {
            throw new AuthenticationFailedException();
        }
        log.info("User is authenticated.");

        String token = generateToken("SIGURAN_TEST");
        AuthenticationTokenResponsePayload resp = new AuthenticationTokenResponsePayload();
        resp.setToken(token);
        log.info("".format("Authentication complete, token: [%s] created.", token));
        return resp;
    }
}
