package com.learning.LoginAndRegistrationByAmigosCode.registration.token;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    public void saveConfirmationToken(ConfirmationToken confirmationToken){
        confirmationTokenRepository.save(confirmationToken);
    }

    public Optional<ConfirmationToken> getByToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }

//    public void setConfirmedAt(String token) {
//        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
//                .orElseThrow(() ->
//                        new IllegalStateException("Token not found"));
//        confirmationToken.setConfirmedAt(LocalDateTime.now());
//    }
}
