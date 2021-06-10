package com.example.wallet.payment_wallet;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletUserRepository extends JpaRepository<WalletUser,Integer>{

    WalletUser findByUserId(String userId);
}
