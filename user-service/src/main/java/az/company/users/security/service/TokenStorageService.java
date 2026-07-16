package az.company.users.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenStorageService {

    private static final String ACCESS_TOKEN_PREFIX = "access_token:";
    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";

    private final StringRedisTemplate redisTemplate;
    private final JwtService jwtService;

    public void storeAccessToken(String username, String token) {
        String key = ACCESS_TOKEN_PREFIX + username;
        redisTemplate.opsForValue().set(key, token, jwtService.getAccessTokenExpiration(), TimeUnit.MINUTES);
    }
    public void storeRefreshToken(String userName,String refreshToken) {
        String key = REFRESH_TOKEN_PREFIX + userName;
        redisTemplate.opsForValue().set(key, refreshToken, jwtService.getRefreshTokenExpiration(), TimeUnit.HOURS);
    }

    public Boolean isAccessTokenValid(String username, String token) {
        String key = ACCESS_TOKEN_PREFIX + username;
        String storedToken = redisTemplate.opsForValue().get(key);
        return token.equals(storedToken);
    }
public Boolean isRefreshTokenValid(String username, String token) {
        String key = REFRESH_TOKEN_PREFIX + username;
        String storedToken = redisTemplate.opsForValue().get(key);
        return token.equals(storedToken);
    }
    public void deleteAccessToken(String username) {
        redisTemplate.delete( ACCESS_TOKEN_PREFIX + username);
    }
    public void deleteRefreshToken(String username) {
        redisTemplate.delete( REFRESH_TOKEN_PREFIX + username);
    }
}
