package az.company.books.client;

import az.company.books.exception.ErrorResponse;
import az.company.books.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class CustomFeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            String body = IOUtils.toString(response.body().asInputStream(), StandardCharsets.UTF_8);
            ErrorResponse errorResponse = objectMapper.readValue(body, ErrorResponse.class);

            if (response.status() == 404) {
                return new NotFoundException(errorResponse.getCode(), errorResponse.getMessage());
            }
            return new RuntimeException(errorResponse.getMessage());
        } catch (Exception e) {
            return new RuntimeException("Feign decoding error", e);
        }
    }
}
