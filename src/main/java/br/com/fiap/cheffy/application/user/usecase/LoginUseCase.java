//package br.com.fiap.cheffy.application.user.usecase;
//
//import br.com.fiap.cheffy.application.user.dto.LoginResultPort;
//import br.com.fiap.cheffy.domain.user.entity.AuthenticatedUser;
//import br.com.fiap.cheffy.domain.user.port.input.AuthenticationManagerPort;
//import br.com.fiap.cheffy.domain.user.port.input.LoginInput;
//import br.com.fiap.cheffy.domain.user.port.input.TokenGeneratorPort;
//
//public class LoginUseCase implements LoginInput {
//
//    private final AuthenticationManagerPort authenticationManager;
//    private final TokenGeneratorPort tokenGenerator;
//
//    public LoginUseCase(
//            AuthenticationManagerPort authenticationManager,
//            TokenGeneratorPort tokenGenerator
//    ) {
//        this.authenticationManager = authenticationManager;
//        this.tokenGenerator = tokenGenerator;
//    }
//
//
//    @Override
//    public LoginResultPort execute(String login, String password) {
//
//        AuthenticatedUser user = authenticationManager.authenticate(login, password);
//         String token = tokenGenerator.generate(user);
//
//         return new LoginResultPort(token);
//
//    }
//}
