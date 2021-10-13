package com.tradeBrite.banking.AppUser;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class AppUserService {

    private AppUserRepository appUserRepository;

    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    public Optional<AppUser> getUserById(Long id) {
        return appUserRepository.findById(id);
    }

    public void createNewUser(String name) {
        AppUser appUser = new AppUser(name);
        appUserRepository.save(appUser);
    }

    public void depositMoney(Long id, BigDecimal deposit) {
        if (getUserById(id).isEmpty()) {
            return;
        }
        AppUser appUser = getUserById(id).get();
        BigDecimal balance = appUser.getBalance();
        balance = balance.add(deposit);
        appUser.setBalance(balance);
        appUserRepository.save(appUser);
    }

    public void withdrawMoney(Long id, BigDecimal withdraw) {
        if (getUserById(id).isEmpty()) {
            return;
        }
        AppUser appUser = getUserById(id).get();
        BigDecimal balance = appUser.getBalance();

        if (withdraw.compareTo(balance) > 0){
            return;
        }

        balance = balance.subtract(withdraw);
        appUser.setBalance(balance);
        appUserRepository.save(appUser);
    }

    public void transferMoney(Long senderId,Long receiverId, BigDecimal transfer) {
        if (getUserById(receiverId).isEmpty() || getUserById(senderId).isEmpty()) {
            return;
        }

        BigDecimal balance = getUserById(senderId).get().getBalance();

        if (transfer.compareTo(balance) > 0){
            return;
        }

        withdrawMoney(senderId, transfer);
        depositMoney(receiverId, transfer);
    }

    public BigDecimal checkBalance(Long id) {
        if (getUserById(id).isEmpty()) {
            return null;
        }
        return getUserById(id).get().getBalance();
    }

}