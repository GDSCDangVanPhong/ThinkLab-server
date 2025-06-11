package com.thinklab.platform.share.infrastructure.api;

import com.thinklab.platform.share.domain.exception.*;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.DataFetcherExceptionResolver;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
public class GraphQLExceptionResolver implements DataFetcherExceptionResolver {

    @Override
    public Mono<List<GraphQLError>> resolveException(Throwable exception, DataFetchingEnvironment env) {
        if (exception instanceof InputsInvalidateException) {
            return Mono.just(List.of(GraphqlErrorBuilder.newError(env)
                    .message(exception.getMessage())
                    .errorType(ErrorType.BAD_REQUEST)
                    .build()));
        }

        if (exception instanceof InternalErrorException) {
            return Mono.just(List.of(GraphqlErrorBuilder.newError(env)
                    .message(exception.getMessage())
                    .errorType(ErrorType.INTERNAL_ERROR)
                    .build()));
        }
        if (exception instanceof NotFoundException) {
            return Mono.just(List.of(GraphqlErrorBuilder.newError(env)
                    .message(exception.getMessage())
                    .errorType(ErrorType.BAD_REQUEST)
                    .build()));
        }
        if (exception instanceof UnauthorizedException) {
            return Mono.just(List.of(GraphqlErrorBuilder.newError(env)
                    .message(exception.getMessage())
                    .errorType(ErrorType.UNAUTHORIZED)
                    .build()));
        }
        if (exception instanceof ValidateException) {
            return Mono.just(List.of(GraphqlErrorBuilder.newError(env)
                    .message(exception.getMessage())
                    .errorType(ErrorType.BAD_REQUEST)
                    .build()));
        }

        return Mono.empty();
    }
}
