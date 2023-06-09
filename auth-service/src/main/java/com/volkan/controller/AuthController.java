package com.volkan.controller;

import com.volkan.dto.request.*;
import com.volkan.dto.response.AuthRegisterResponseDto;
import com.volkan.repository.entity.Auth;
import com.volkan.repository.enums.ERole;
import com.volkan.service.AuthService;
import com.volkan.utility.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;
import java.util.Optional;

import static com.volkan.constants.ApiUrls.*;

/**
 * dışarıdan login olmak için gerekli parametreleri alalım
 * eğer bilgiler doğru ise bize true yanlış ise false dönsün
 */

/**
 * status u aktif hale getirdiğim zaman user micro servisinde de statusum aktif hale gelsi istiyorum
 */

/**
 * login methodumuzu düzeltelim bize bir token üretip tokenı dönsün sadece active kullanıcılar
 * login olabilsin
 */
/**
 * delete işleminde verimizi silmiyoruz sadece statusunu değiştiriyoruz.
 */

@RestController
@RequestMapping(AUTH)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtTokenManager tokenManager;
    private final CacheManager cachemanager;
    @PostMapping(REGISTER)
    public ResponseEntity<AuthRegisterResponseDto> register(@RequestBody @Valid AuthRegisterRequestDto dto) {
        return ResponseEntity.ok(authService.register(dto));
    }
    @PostMapping(REGISTER+2)
    public ResponseEntity<AuthRegisterResponseDto> registerWithRabbitMq(@RequestBody @Valid AuthRegisterRequestDto dto) {
        return ResponseEntity.ok(authService.registerWithRabbitMq(dto));
    }
    @PostMapping(LOGIN)
    public ResponseEntity<String> doLogin(@RequestBody DoLoginRequestDto dto) {
        return ResponseEntity.ok(authService.doLogin(dto));
    }
    @PostMapping(ACTIVATESTATUS)
    public ResponseEntity<Boolean> activateStatus (@RequestBody ActivateRequestDto dto) {
        return ResponseEntity.ok(authService.activateStatus(dto));
    }
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(FINDALL)
    public ResponseEntity<List<Auth>> findAll() {
        return ResponseEntity.ok(authService.findAll());
    }
    @GetMapping("/createtoken")
    public ResponseEntity<String> createToken(Long id, ERole role) {
       return ResponseEntity.ok(tokenManager.createToken(id,role).get());
    }
    @GetMapping("/createtoken2")
    public ResponseEntity<String> createToken(Long id) {
        return ResponseEntity.ok(tokenManager.createToken(id).get());
    }
    @GetMapping("/getidfromtoken")
    public ResponseEntity<Long> getIdFromToken(String token) {
        return ResponseEntity.ok(tokenManager.getIdFromToken(token).get());
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getrolefromtoken")
    public ResponseEntity<String> getRoleFromToken(String token) {
        return ResponseEntity.ok(tokenManager.getRoleFromToken(token).get());
    }
    @PutMapping(UPDATE)
    public ResponseEntity<Boolean> updateEmailOrUsername(@RequestHeader(value="Authorization") String token,
                                                         @RequestBody UpdateEmailOrUsernameRequestDto dto) {
        return ResponseEntity.ok(authService.updateEmailOrUsername(dto));
    }
    @DeleteMapping(DEACTIVATESTATUS)
    public ResponseEntity<Boolean> deactivateStatus (@RequestBody DeactivateRequestDto dto) {
        return ResponseEntity.ok(authService.DeactivateStatus(dto));
    }
    @DeleteMapping(DELETEBYID)
    public ResponseEntity<Boolean> delete (Long id) {
        return ResponseEntity.ok(authService.delete(id));
    }
    @GetMapping("/redis")
    @Cacheable(value = "redisexample")
    public String redisExample(String value) {
        try {
            Thread.sleep(2000);
            return value;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/redisdelete")
    @CacheEvict(cacheNames = "redisexample",allEntries = true)
    public void redisDelete() {
    }

    @GetMapping("/redisdelete2")
    public Boolean redisDelete2() {
        try {
           // cachemanager.getCache("redisexample").clear(); //ayn9 isimle cache lenmiş bütün verileri siler
            cachemanager.getCache("redisexample").evict("mustafa");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    @GetMapping(FINDBYROLE)
    public ResponseEntity<List<Long>> findByRole(@RequestHeader(value="Authorization") String token,
                                                 @RequestParam String role) {
        return ResponseEntity.ok(authService.findByRole(role));
    }
}
