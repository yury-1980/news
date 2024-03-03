package ru.clevertec.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    /**
     * Извлекает имя пользователя из токена JWT.
     *
     * @param token Токен JWT, из которого нужно извлечь имя пользователя.
     * @return Имя пользователя, извлеченное из токена.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Извлекает определенное утверждение (claim) из токена JWT с помощью функции разрешения утверждений.
     *
     * @param token          Токен JWT, из которого нужно извлечь утверждение.
     * @param claimsResolver Функция, которая разрешает требуемое утверждение из объекта Claims.
     * @param <T>            Тип извлекаемого утверждения.
     * @return Извлеченное значение утверждения.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Генерирует токен для указанных пользовательских данных.
     *
     * @param userDetails пользовательские данные, используемые для генерации токена
     * @return сгенерированный токен в виде строки
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

//    private String SECRET_KEY = "your_secret_key";
//    private int TOKEN_VALIDITY = 1000 * 60 * 60 * 10; // 10 hours

//    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
//        return Jwts.builder()
//                   .setClaims(extraClaims)
//                   .setSubject(userDetails.getUsername())
//                   .setIssuedAt(new Date(System.currentTimeMillis()))
//                   .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
//                   .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                   .compact();
//    }
//}

    /**
     * Генерирует токен с дополнительными данными (extraClaims) для указанных пользовательских данных.
     *
     * @param extraClaims дополнительные данные, которые требуется добавить в токен
     * @param userDetails пользовательские данные, используемые для генерации токена
     * @return сгенерированный токен в виде строки
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {

        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * Генерирует обновляющий токен для указанных пользовательских данных.
     *
     * @param userDetails пользовательские данные, используемые для генерации обновляющего токена
     * @return сгенерированный обновляющий токен в виде строки
     */
    public String generateRefreshToken(UserDetails userDetails) {

        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    /**
     * Строит токен с дополнительными данными (extraClaims) для указанных пользовательских данных и заданным сроком действия.
     *
     * @param extraClaims  дополнительные данные, которые требуется добавить в токен
     * @param userDetails пользовательские данные, используемые для построения токена
     * @param expiration   срок действия токена в миллисекундах
     * @return построенный токен в виде строки
     */
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Проверяет, является ли токен действительным для указанных пользовательских данных.
     *
     * @param token       токен, который требуется проверить
     * @param userDetails пользовательские данные, используемые для проверки токена
     * @return true, если токен действителен для указанных пользовательских данных, в противном случае - false
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Проверяет, истек ли срок действия токена.
     *
     * @param token токен, который требуется проверить
     * @return true, если срок действия токена истек, в противном случае - false
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Извлекает дату истечения срока действия из токена.
     *
     * @param token токен, из которого требуется извлечь дату истечения срока действия
     * @return дата истечения срока действия токена
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Извлекает все утверждения (claims) из токена JWT.
     *
     * @param token Токен JWT для извлечения утверждений.
     * @return Объект Claims, содержащий все утверждения из токена.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Получает ключ для подписи JWT.
     *
     * @return Ключ для подписи JWT.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}