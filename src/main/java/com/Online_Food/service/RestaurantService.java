package com.Online_Food.service;

import com.Online_Food.dto.RestaurantDto;
import com.Online_Food.model.Restaurant;
import com.Online_Food.model.User;
import com.Online_Food.request.CreateRestaurantRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RestaurantService {
    public Restaurant createRestaurant(CreateRestaurantRequest req, User user);

    public  Restaurant updateRestaurant(Long restaurantId,CreateRestaurantRequest updatedRestaurant) throws  Exception;


    public  void  deleteRestaurant(Long restaurantId) throws  Exception;

    public List<Restaurant> getAllRestaurant ();

    public  List<Restaurant> SearchRestaurant (String keyword);

    public  Restaurant findRestaurantById(Long id) throws  Exception;

    public  Restaurant getRestaurantByUserId(Long userId) throws  Exception;

    public RestaurantDto addToFavorites(Long restaurantId, User user) throws  Exception;

    public Restaurant updateRestaurantStatus(Long id) throws  Exception;


}





















