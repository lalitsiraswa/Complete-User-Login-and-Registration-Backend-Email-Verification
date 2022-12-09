package com.learning.LoginAndRegistrationByAmigosCode.registration;

import com.learning.LoginAndRegistrationByAmigosCode.appuser.AppUser;
import com.learning.LoginAndRegistrationByAmigosCode.appuser.AppUserRole;
import com.learning.LoginAndRegistrationByAmigosCode.appuser.AppUserService;
import com.learning.LoginAndRegistrationByAmigosCode.registration.token.ConfirmationToken;
import com.learning.LoginAndRegistrationByAmigosCode.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final EmailValidator emailValidator;
    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;
    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if(!isValidEmail){
            throw new IllegalStateException(String.format("Email: %s not valid!", request.getEmail()));
        }
        return appUserService.signUpUser(
                new AppUser(
                        request.getFirstname(),
                        request.getLastname(),
                        request.getEmail(),
                        request.getPassword(),
                        AppUserRole.USER
                )
        );
    }
    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getByToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("Token not found"));
        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException(String.format("Email: %s already confirmed", confirmationToken.getAppUser().getEmail()));
        }
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired");
        }
        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(
                confirmationToken.getAppUser().getEmail());
        return "confirmed";
    }
}
