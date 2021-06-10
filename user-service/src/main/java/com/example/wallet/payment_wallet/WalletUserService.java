package com.example.wallet.payment_wallet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class WalletUserService {

    @Autowired
    WalletUserRepository walletUserRepository;

    @Autowired
    RedisTemplate<String,WalletUser> redisTemplate;
    private static String REDIS_USER_PREFIX="user::";

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    private static final String USER_CREATE_TOPIC="user-created";

    @Value("${user.account.create.default.balance}")
    int defaultWalletBalance;

    @Autowired
    ObjectMapper objectMapper;

    public void createWalletUser(WalletUser walletUser) throws JsonProcessingException {
        // 1. create an entry in DB and redis

        walletUserRepository.save(walletUser);
        redisTemplate.opsForValue().set(REDIS_USER_PREFIX+walletUser.getUserId(),walletUser);

        // 2. Crete a wallet of this user

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("userId",walletUser.getUserId());
        jsonObject.put("balance",defaultWalletBalance);

        kafkaTemplate.send(USER_CREATE_TOPIC,walletUser.getUserId(),objectMapper.writeValueAsString(jsonObject));
    }

    public WalletUser getWalletUser(String userId) {

        // 1. search in cache, if not found search in db and add it in cache
        WalletUser walletUser = (WalletUser) redisTemplate.opsForValue().get(REDIS_USER_PREFIX + userId);

        if (walletUser == null) {
            walletUser = walletUserRepository.findByUserId(userId);
            if (walletUser != null) {
                redisTemplate.opsForValue().set(REDIS_USER_PREFIX + userId, walletUser);
            }
        }
        return walletUser;
    }
}
