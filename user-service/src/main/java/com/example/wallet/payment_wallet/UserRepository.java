package com.example.wallet.payment_wallet;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,User>{

    User findByUserId(String userId);
}
