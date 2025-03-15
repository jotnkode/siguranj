package io.jotnkode.siguranj.filters;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import java.time.Instant;
import reactor.core.publisher.Mono;

/**
 Utility filters for siguranj.
*/
@Component
public class SiguranjHeaderFilter implements WebFilter {
    /**
     Adds x-siguranj-timestamp header with the request timestamp.
    */
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        Instant now = Instant.now();
        serverWebExchange
            .getResponse()
            .getHeaders()
            .add("x-siguranj-timestamp", now.toString());
                return webFilterChain.filter(serverWebExchange);
    }
}
