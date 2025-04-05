package com.linasdeli.api.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncryptor {
    public static void main(String[] args) {
        // BCryptPasswordEncoder 객체 생성
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // 암호화할 비밀번호
        String rawPassword = "yourPassword123";

        // 비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(rawPassword);

        // 암호화된 비밀번호 출력
        System.out.println("Encrypted Password: " + encryptedPassword);
    }
}

