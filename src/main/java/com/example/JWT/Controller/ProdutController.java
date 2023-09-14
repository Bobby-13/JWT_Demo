package com.example.JWT.Controller;


import com.example.JWT.Dto.AuthRequest;
import com.example.JWT.Dto.Product;
import com.example.JWT.Entity.UserInfo;
import com.example.JWT.Service.JwtService;
import com.example.JWT.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProdutController {

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
    public List<Product> getAllTheProducts() {
        return service.getProducts();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Product getProductById(@PathVariable int id) {
        return service.getProduct(id);
    }

   @PostMapping("/authenticate")
    public String authenticationAndGetToken(@RequestBody AuthRequest authRequest){

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword()));
      if(authentication.isAuthenticated()){
          return jwtService.generateToken(authRequest.getUsername());
      }
      else{
          throw new UsernameNotFoundException("invalid user Request");
      }

    }


}
