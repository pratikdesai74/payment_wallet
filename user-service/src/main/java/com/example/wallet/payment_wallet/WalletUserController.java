package com.example.wallet.payment_wallet;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class WalletUserController {

    @Autowired
    WalletUserService walletUserService;

    @PostMapping("/user")
    public void createWalletUser(@RequestBody WalletUser walletUser) throws JsonProcessingException {
        walletUserService.createWalletUser(walletUser);
    }

    @GetMapping("/user/{userId}")
    public WalletUser getUser(@PathVariable("userId") String userId){
        return walletUserService.getWalletUser(userId);
    }

}
