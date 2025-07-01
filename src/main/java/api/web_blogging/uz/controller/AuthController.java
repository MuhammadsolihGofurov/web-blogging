package api.web_blogging.uz.controller;


import api.web_blogging.uz.dto.registerDTO;
import api.web_blogging.uz.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody registerDTO registerDto) {
        return ResponseEntity.ok().body(authService.registration(registerDto));
    }

    @GetMapping("/register/verification/{id}")
    public ResponseEntity<String> registerVerification(@PathVariable("id") Integer profileId) {
        return ResponseEntity.ok().body(authService.regVerification(profileId));
    }

}
