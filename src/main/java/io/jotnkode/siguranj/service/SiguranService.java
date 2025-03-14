package io.jotnkode.siguranj.service;

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


@Service
public class SiguranService {
    @Autowired
    private UserManagement mgmt;
    @Autowired
    private Authentication auth;

    public Mono<ServerResponse> addUser(ServerRequest request) {
        return request.bodyToMono(UserPayload.class).flatMap( data -> {
            System.out.println(data.toString());
            mgmt.createNewUser(data);
            return ServerResponse.ok().build();
        });
    }

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
}
