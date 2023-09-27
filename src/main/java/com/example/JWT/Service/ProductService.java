package com.example.JWT.Service;

import com.example.JWT.Dto.PasswordChangeRequest;
import com.example.JWT.Dto.Product;
import com.example.JWT.Entity.UserInfo;
import com.example.JWT.Repository.UserInfoRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@EnableCaching
public class ProductService {

    @Autowired
            private UserInfoRepo userInfoRepo;
    @Autowired
            private PasswordEncoder encoder;
     List<Product> productList = null;

    public String changepassword(PasswordChangeRequest passwordChangeRequest) {

        System.out.println(passwordChangeRequest.getEmail());
        Optional<UserInfo> Optuser= userInfoRepo.findByEmail(passwordChangeRequest.getEmail());
        UserInfo userInfo = Optuser.orElseThrow(() -> new NoSuchElementException("User not found"));

//      System.out.println(userInfo.getPassword()+" == > "+encoder.encode(passwordChangeRequest.getOldpassword()));
//      System.out.println("==>"+encoder.matches(passwordChangeRequest.getOldpassword(),userInfo.getPassword()));
        if(encoder.matches(passwordChangeRequest.getOldpassword(),userInfo.getPassword()))
        {
          String encodednewpassword = encoder.encode(passwordChangeRequest.getNewpassword());
            if(!encoder.matches(passwordChangeRequest.getOldpassword(),encodednewpassword)){
                userInfo.setPassword(encodednewpassword);
                userInfoRepo.save(userInfo);
                return "Password Changed Successfully";
            }

            return "New password cannot be the same as the old password.";
        }


        return "OldPassword Doesn't Match";

    }

    @PostConstruct
    public void loadProductsFromDB() {
        productList = IntStream.rangeClosed(1, 100)
                .mapToObj(i -> Product.builder()
                        .productId(i)
                        .name("product " + i)
                        .qty(new Random().nextInt(10))
                        .price(new Random().nextInt(5000)).build()
                ).collect(Collectors.toList());
    }


    public List<Product> getProducts() {
        return productList;
    }

    @Cacheable(value = "products", key = "#id")
    public Product getProduct(int id) {
        System.out.println("Fetching from DB");
        return productList.stream()
                .filter(product -> product.getProductId() == id)
                .findAny()
                .orElseThrow(() -> new RuntimeException("product " + id + " not found"));
    }


    public String addUser(UserInfo userInfo) {
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));

        userInfoRepo.save(userInfo);

        return "User Added Successfully";
    }
}
