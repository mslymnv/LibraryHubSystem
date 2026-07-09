package az.company.books.client;

import az.company.books.model.client.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.OK;

@FeignClient(
        name = "user-service",
        url = "http://localhost:8081/v1/users",
        configuration = CustomFeignErrorDecoder.class
)
public interface UserClient {
    @GetMapping("/{id}")
    @ResponseStatus(OK)
    UserResponse getUser(@PathVariable Long id);
}
