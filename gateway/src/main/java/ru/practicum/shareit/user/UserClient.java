package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.validators.UserNotBlankNameValidator;

@Service
public class UserClient extends BaseClient {

    public static final String API_PREFIX = "/users";

    private final UserNotBlankNameValidator userNotBlankNameValidator;

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl,
                      RestTemplateBuilder builder,
                      UserNotBlankNameValidator userNotBlankNameValidator) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );

        this.userNotBlankNameValidator = userNotBlankNameValidator;
    }

    public ResponseEntity<Object> create(UserDto userDto) {
        userNotBlankNameValidator.validate(userDto);

        return post("", userDto);
    }

    public ResponseEntity<Object> retieve() {
        return get("");
    }

    public ResponseEntity<Object> update(long userId, UserDto userDto) {

        return patch("/" + userId, userDto);

    }

    public ResponseEntity<Object> retieve(long userId) {
        return get("/" + userId);
    }

    public ResponseEntity<Object> delete(long userId) {
        return delete("/" + userId);
    }

}
