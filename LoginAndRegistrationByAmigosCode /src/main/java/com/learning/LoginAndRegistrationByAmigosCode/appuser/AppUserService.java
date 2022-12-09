package com.learning.LoginAndRegistrationByAmigosCode.appuser;
import com.learning.LoginAndRegistrationByAmigosCode.registration.token.ConfirmationToken;
import com.learning.LoginAndRegistrationByAmigosCode.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {
    private final static String USER_NOTE_FOUND_MSG = "user with email %s not found!";
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(()->
                        new UsernameNotFoundException(String.format(USER_NOTE_FOUND_MSG, email)));
    }
    public String signUpUser(AppUser appUser){
        boolean userExists = appUserRepository.findByEmail(appUser.getEmail()).isPresent();
        // TODO: if email not confirmed send confirmation email
        if(userExists && appUser.getEnabled()){
             throw new IllegalStateException(String.format("Email: %s already exists!", appUser.getEmail()));
        }
        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
        appUserRepository.save(appUser);
//      TODO: Send Conformation Token
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
//      TODO: SEND EMAIL
//        return String.format("User Registration Token: %s Has Benn Saved Successfully!", token);
        return token;
    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }

//    public void enableAppUser(String email) {
//        AppUser appUser = appUserRepository.findByEmail(email)
//                .orElseThrow(() ->
//                        new IllegalStateException(String.format("Email: %s not found!", email)));
//        appUser.setEnabled(true);
//    }
}
