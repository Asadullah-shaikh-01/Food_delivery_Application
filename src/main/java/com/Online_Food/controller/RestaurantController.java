package com.Online_Food.controller;


import com.Online_Food.model.Restaurant;
import com.Online_Food.model.User;
import com.Online_Food.request.CreateRestaurantRequest;
import com.Online_Food.service.RestaurantService;
import com.Online_Food.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private RestaurantService restaurantService;

    @GetMapping()
   public ResponseEntity<List<Restaurant>> getAllRestaurant (
           @RequestHeader("Authorization") String jwt

    ) throws  Exception {
        User user =userService.findUserByJwtToken(jwt);
        List<Restaurant> restaurant=restaurantService.getAllRestaurant();

        return  new ResponseEntity<>(restaurant, HttpStatus.OK);
    }























































}
