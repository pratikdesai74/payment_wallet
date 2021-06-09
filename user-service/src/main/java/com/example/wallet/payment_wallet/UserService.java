package com.example.wallet.payment_wallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RedisTemplate<String,User> redisTemplate;
    private static String REDIS_USER_PREFIX="user::";

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;
    private static final String USER_CREATE_TOPIC="user-created";

    public void createUser(User user){
        // 1. create an entry in DB and redis

        userRepository.save(user);
        redisTemplate.opsForValue().set(REDIS_USER_PREFIX+user.getUserId(),user);

        // 2. Crete a wallet of this user
        kafkaTemplate.send(USER_CREATE_TOPIC,user.getUserId(),null);
    }

    public User getUser(String userId) {

        // 1. search in cache, if not found search in db and add it in cache
        User user = (User) redisTemplate.opsForValue().get(REDIS_USER_PREFIX + userId);

        if (user == null) {
            user = userRepository.findByUserId(userId);
            if (user != null) {
                redisTemplate.opsForValue().set(REDIS_USER_PREFIX + userId, user);
            }
        }
        return user;
    }
}
