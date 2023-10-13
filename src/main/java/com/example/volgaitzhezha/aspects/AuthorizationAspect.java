package com.example.volgaitzhezha.aspects;

import com.example.volgaitzhezha.exceptions.ApiRequestException;
import com.example.volgaitzhezha.services.AccountsService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
@RequiredArgsConstructor
public class AuthorizationAspect {
    private final AccountsService accountsService;

    @Before("execution(* com.example.volgaitzhezha.services.*.*(..))" +
            " && @annotation(com.example.volgaitzhezha.annotations.AdminAction)")
    public void AdminActionAdvice() {
        if (!accountsService.getAuthenticated().isAdmin()) {
            throw new ApiRequestException("Недостаточно прав");
        }
    }
}
