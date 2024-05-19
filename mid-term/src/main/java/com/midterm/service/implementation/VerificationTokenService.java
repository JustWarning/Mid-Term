package com.midterm.service.implementation;

import com.midterm.entity.User;
import com.midterm.entity.VerificationToken;
import com.midterm.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationTokenService {

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    public VerificationToken createVerificationToken(User user) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(Instant.now().plusMillis(86400000)); // 1 day
        verificationToken.setToken(UUID.randomUUID().toString());
        return verificationTokenRepository.save(verificationToken);
    }

    public Optional<VerificationToken> findByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    public void delete(VerificationToken token) {
        verificationTokenRepository.delete(token);
    }

    public boolean isTokenExpired(VerificationToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }
}
