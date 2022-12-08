package com.learning.LoginAndRegistrationByAmigosCode.registration;

import com.learning.LoginAndRegistrationByAmigosCode.appuser.AppUser;
import com.learning.LoginAndRegistrationByAmigosCode.appuser.AppUserRole;
import com.learning.LoginAndRegistrationByAmigosCode.appuser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final EmailValidator emailValidator;
    private final AppUserService appUserService;
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
}
