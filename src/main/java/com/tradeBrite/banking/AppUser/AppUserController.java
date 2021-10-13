package com.tradeBrite.banking.AppUser;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

@RestController
@AllArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;

    @PostMapping("add-user/{name}")
    public void addUser(@PathVariable(value = "name") String name, HttpServletResponse response){
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setStatus(200);
        appUserService.createNewUser(name);
    }

    @GetMapping("get-all-users")
    public List<AppUser> getAllUsers(HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setStatus(200);
        return appUserService.getAllUsers();
    }

    @PostMapping("deposit/{id}/{deposit}")
    public void depositMoney(@PathVariable(value = "id") Long id, @PathVariable(value = "deposit") BigDecimal deposit,HttpServletResponse response){
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setStatus(200);
        appUserService.depositMoney(id, deposit);
    }

    @PostMapping("withdraw/{id}/{withdraw}")
    public void withdrawMoney(@PathVariable(value = "id") Long id, @PathVariable(value = "withdraw") BigDecimal withdraw,HttpServletResponse response){
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setStatus(200);
        appUserService.withdrawMoney(id, withdraw);
    }

    @PostMapping("transfer/{sender-id}/{receiver-id}/{transfer}")
    public void transferMoney(@PathVariable(value = "sender-id") Long senderId, @PathVariable(value = "receiver-id") Long receiverId ,@PathVariable(value = "transfer") BigDecimal transfer, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setStatus(200);
        appUserService.transferMoney(senderId, receiverId, transfer);
    }

    @GetMapping("check-balance/{id}")
    public BigDecimal checkBalance(@PathVariable(value = "id") Long id, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setStatus(200);
        return appUserService.checkBalance(id);
    }
}