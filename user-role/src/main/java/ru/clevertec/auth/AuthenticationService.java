package ru.clevertec.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.clevertec.config.JwtService;
import ru.clevertec.entity.User;
import ru.clevertec.entity.type.Role;
import ru.clevertec.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    //  private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String encodedPassword = encoder.encode(password);

        var user = User.builder()
                       .userName(request.getUserName())
                       .email(request.getEmail())
                       .password(encoder.encode(request.getPassword()))
                       .role(Role.SUBSCRIBER)
                       .build();

        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
//        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                                     .token(jwtToken)
//                                     .refreshToken(refreshToken)
                                     .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
                                          );

        var user = repository.findByEmail(request.getEmail())
                             .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
//        revokeAllUserTokens(user);
//        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                                     .token(jwtToken)
//                                     .refreshToken(refreshToken)
                                     .build();
    }

//  private void saveUserToken(User user, String jwtToken) {
//    var token = Token.builder()
//                     .user(user)
//                     .token(jwtToken)
//                     .tokenType(TokenType.BEARER)
//                     .expired(false)
//                     .revoked(false)
//                     .build();
//    tokenRepository.save(token);
//  }
//
//  private void revokeAllUserTokens(User user) {
//    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
//    if (validUserTokens.isEmpty())
//      return;
//    validUserTokens.forEach(token -> {
//      token.setExpired(true);
//      token.setRevoked(true);
//    });
//    tokenRepository.saveAll(validUserTokens);
//  }
//
//  public void refreshToken(
//          HttpServletRequest request,
//          HttpServletResponse response
//  ) throws IOException {
//    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//    final String refreshToken;
//    final String userEmail;
//    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
//      return;
//    }
//    refreshToken = authHeader.substring(7);
//    userEmail = jwtService.extractUsername(refreshToken);
//    if (userEmail != null) {
//      var user = this.repository.findByEmail(userEmail)
//              .orElseThrow();
//      if (jwtService.isTokenValid(refreshToken, user)) {
//        var accessToken = jwtService.generateToken(user);
//        revokeAllUserTokens(user);
//        saveUserToken(user, accessToken);
//        var authResponse = AuthenticationResponse.builder()
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .build();
//        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
//      }
//    }
//  }
}
