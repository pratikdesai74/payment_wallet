package com.example.wallet;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    private static final String USER_CREATE_TOPIC="user-created";

    @KafkaListener(topics = {USER_CREATE_TOPIC})
    public void createWallet(){

    }
}
