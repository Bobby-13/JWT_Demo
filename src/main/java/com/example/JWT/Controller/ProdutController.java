package com.example.JWT.Controller;


import com.example.JWT.Config.UserInfoUserDetails;
import com.example.JWT.Dto.*;
import com.example.JWT.Entity.RefreshToken;
import com.example.JWT.Entity.UserInfo;
import com.example.JWT.Repository.RefreshTokenRepo;
import com.example.JWT.Service.JwtService;
import com.example.JWT.Service.ProductService;
import com.example.JWT.Service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/product")
public class ProdutController {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private ProductService service;

    @Autowired
    private JwtService jwtService;

      @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/new")
   public String AddnewUser(@RequestBody UserInfo userInfo){
        return service.addUser(userInfo);
   }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<Product> getAllTheProducts()
    {
//        System.out.println(authHeader);
//        System.out.println("Received authHeader: " + authHeader);
        return service.getProducts();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Product getProductById(@PathVariable int id) {
        return service.getProduct(id);
    }

   @PostMapping("/authenticate")
    public JwtResponse authenticationAndGetToken(@RequestBody AuthRequest authRequest){

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword()));
      if(authentication.isAuthenticated()){
          RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.getUsername());
        return JwtResponse.builder()
                  .accessToken(jwtService.generateToken(authRequest.getUsername()))
                  .token(refreshToken.getToken()).build();
      }
      else{
          throw new UsernameNotFoundException("invalid user Request");
      }

    }

    @PostMapping("/change-password")
    public String ChangePassword(@RequestBody PasswordChangeRequest passwordChangeRequest, Authentication authentication)
    {
        UserInfoUserDetails userDetails = (UserInfoUserDetails)authentication.getPrincipal();
        System.out.println(userDetails.getUsername()+"==> "+userDetails.getPassword());
        passwordChangeRequest.setEmail(userDetails.getUsername());
        return service.changepassword(passwordChangeRequest);
    }

@PostMapping("/refreshToken")
    public JwtResponse refreshToken(@RequestBody RefreshTokenRequest request){
    return refreshTokenService.findByToken(request.getToken())
            .map(refreshTokenService::verifyExpiration)
            .map(RefreshToken::getUserInfo)
            .map( userInfo -> {
             String accessToken = jwtService.generateToken(userInfo.getEmail());
               return JwtResponse.builder()
                       .accessToken(accessToken)
                       .token(request.getToken())
                       .build();
            }).orElseThrow(()-> new RuntimeException("Refresh token is not in database!"));
    }


}
