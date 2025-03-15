package io.jotnkode.siguranj.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import io.jotnkode.siguranj.api.schema.AuthenticationPayload;
import io.jotnkode.siguranj.api.schema.UserPayload;
import io.jotnkode.siguranj.api.schema.response.AuthenticationTokenResponsePayload;
import io.jotnkode.siguranj.user.UserManagement;
import io.jotnkode.siguranj.user.Authentication;
import io.jotnkode.siguranj.user.exceptions.NoSuchUserException;
import io.jotnkode.siguranj.user.exceptions.AuthenticationFailedException;

import java.io.Serial;
import java.rmi.server.ServerRef;
import java.sql.Ref;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

/**
The api handler service for the siguranj. Contains the web flux functional endpoints for
user creation and authentication.
*/
@Service
public class SiguranService {
    @Autowired
    private UserManagement mgmt;
    @Autowired
    private Authentication auth;

    /**
    * Handles the api request to create a new user. Returns http status 200 ok and empty body if successful.
    * otherwise returns http status 500 internal server error.
    * @param request
    * @return {@link ServerResponse}
    */
    public Mono<ServerResponse> addUser(ServerRequest request) {
        return request.bodyToMono(UserPayload.class).flatMap( data -> {
            mgmt.createNewUser(data);
            return ServerResponse.ok().build();
        });
    }

    /**
    * Handles the api request to authenticate a user. Returns http status 200 ok and a json body {@link AuthenticationTokenResponsePayload}.
    * If the user can't be found or authentication failes it will return 404 and 401 respectivley.
    * @param request
    * @return {@link ServerResponse}
    */
    public Mono<ServerResponse> authenticate(ServerRequest request) {
        return request.bodyToMono(AuthenticationPayload.class).flatMap( data -> {
            try {
                AuthenticationTokenResponsePayload resp = auth.authenticate(data);
                return ServerResponse.ok().bodyValue(resp);
            } catch(NoSuchUserException e ) {
                return ServerResponse.notFound().build();
            } catch(AuthenticationFailedException e) {
                return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
            }

        });
    }

    /**
    * Heartbeat utility endpoint to remotely monitor if server is up and available.
    * @param request
    * @return {@link ServerResponse}
    */
    public Mono<ServerResponse> heartbeat(ServerRequest request) {
        return ServerResponse.ok().build();
    }
}
