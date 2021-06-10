package com.example.wallet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    ObjectMapper objectMapper;

    private static final String USER_CREATE_TOPIC="user-created";

    @KafkaListener(topics = {USER_CREATE_TOPIC},groupId = "wallet-group")
    public void createWallet(String message) throws JsonProcessingException {
        JSONObject jsonObjectOFUserData=objectMapper.readValue(message, JSONObject.class);
        Wallet wallet=Wallet.builder().
                userId((String) jsonObjectOFUserData.get("userId")).
                balance((int)jsonObjectOFUserData.get("balance")).
                build();
        walletRepository.save(wallet);
    }
}
