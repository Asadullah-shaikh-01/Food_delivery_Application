package com.Online_Food.request;

import com.Online_Food.model.Address;
import com.Online_Food.model.ContactInformation;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Data
public class CreateRestaurantRequest {
    private  Long id;
    private String name;
    private String description;
    private Address address;
    private String cuisineTye;
    private ContactInformation contactInformation;
    private String openingHours;
    private List<String>images;
}
