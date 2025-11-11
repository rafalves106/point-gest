/**
 * @author falvesmac
 */

package com.gestpoint.gestpoint_api.controllers;
import com.gestpoint.gestpoint_api.dto.LoginRequestDTO;
import com.gestpoint.gestpoint_api.dto.RegisterRequestDTO;
import com.gestpoint.gestpoint_api.dto.ResponseDTO;
import com.gestpoint.gestpoint_api.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body){
        ResponseDTO response = this.authService.login(body);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body){
        ResponseDTO response = this.authService.register(body);
        return ResponseEntity.ok(response);
    }
}