package com.tradeBrite.banking.AppUser;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor

public class AppUserService {

    private AppUserRepository appUserRepository;

    @Transactional(readOnly = true)
    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<AppUser> getUserById(Long id) {
        return appUserRepository.findById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public String createNewUser(String name) {
        AppUser appUser = new AppUser(name);
        appUserRepository.save(appUser);
        return name + " user created";
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String depositMoney(Long id, BigDecimal deposit) {
        if (getUserById(id).isEmpty()) {
            return "User with the id: " + id + " not found.";
        }
        AppUser appUser = getUserById(id).get();
        BigDecimal balance = appUser.getBalance();
        balance = balance.add(deposit);
        appUser.setBalance(balance);
        appUserRepository.save(appUser);
        return deposit + " deposited to the account with the id: " + id;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String withdrawMoney(Long id, BigDecimal withdraw) {
        if (getUserById(id).isEmpty()) {
            return "User with the id: " + id + " not found.";
        }
        AppUser appUser = getUserById(id).get();
        BigDecimal balance = appUser.getBalance();

        if (withdraw.compareTo(balance) > 0){
            return "Not enough balance for the withdrawal.";
        }

        balance = balance.subtract(withdraw);
        appUser.setBalance(balance);
        appUserRepository.save(appUser);
        return withdraw + " withdraw from the account with the id: " + id;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String transferMoney(Long senderId,Long receiverId, BigDecimal transfer) {
        if (getUserById(senderId).isEmpty()) {
            return "Sender with the id: " + senderId + " not found.";
        }
        if (getUserById(receiverId).isEmpty()) {
            return "Receiver with the id: " + receiverId + " not found.";
        }

        AppUser sender = getUserById(senderId).get();
        BigDecimal senderBalance = sender.getBalance();

        if (transfer.compareTo(senderBalance) > 0){
            return "Not enough balance for the transfer.";
        }
        senderBalance = senderBalance.subtract(transfer);
        sender.setBalance(senderBalance);

        AppUser receiver = getUserById(receiverId).get();
        BigDecimal receiverBalance = receiver.getBalance();
        receiverBalance = receiverBalance.add(transfer);
        receiver.setBalance(receiverBalance);

        appUserRepository.save(sender);
        appUserRepository.save(receiver);

        return "Transfer successful.";
    }

    @Transactional(readOnly = true)
    public BigDecimal checkBalance(Long id) {
        if (getUserById(id).isEmpty()) {
            return null;
        }
        return getUserById(id).get().getBalance();
    }

}