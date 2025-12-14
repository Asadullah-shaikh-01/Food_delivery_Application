package com.Online_Food.controller;

import com.Online_Food.config.JwtProvider;
import com.Online_Food.model.Cart;
import com.Online_Food.model.CartItem;
import com.Online_Food.model.USER_ROLE;
import com.Online_Food.model.User;
import com.Online_Food.repository.CartRepository;
import com.Online_Food.repository.UserRepository;
import com.Online_Food.request.LoginRequest;
import com.Online_Food.response.AuthResponse;
import com.Online_Food.service.CustomerUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RestController // Marks this class as a REST controller to handle HTTP requests
@RequestMapping("/auth") // Base URL for all endpoints in this controller
public class AuthController {

    @Autowired
    private UserRepository userRepository; // Repository for User database operations

    @Autowired
    private PasswordEncoder passwordEncoder; // Used for securely encoding passwords

    @Autowired
    private JwtProvider jwtProvider; // Handles JWT token creation

    @Autowired
    private CustomerUserDetailsService customerUserDetailsService; // Custom user details service for authentication

    @Autowired
    private CartRepository cartRepository; // Repository for managing user carts

    /**
     * Handles user registration (sign-up)
     * @param user User details sent in the request body
     * @return Response containing JWT token and registration message
     */
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {

        // Check if an account already exists with the given email
        User isEmailExist = userRepository.findByEmail(user.getEmail());
        if (isEmailExist != null) {
            throw new Exception("Email already used in another Account");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));


        // Create a new user object to store in the database
        User createdUser = new User();
//        System.out.println("Incoming user: " + createdUser);
        createdUser.setEmail(user.getEmail());
        createdUser.setFullName(user.getFullName());
        createdUser.setRole(user.getRole());
        createdUser.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt password before saving
        System.out.println("Password: " + user.getPassword());
        // Save the new user to the database
        User savedUser = userRepository.save(createdUser);

// Create a new cart for the user
        Cart cart = new Cart();
        cart.setCustomer(savedUser);         // Link user to cart
        cart.setTotal(0L);                   // Initialize total
        cart.setCertItems(new ArrayList<>()); // Empty list of CartItems
        cartRepository.save(cart);



        // Create an authentication object for the new user
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(), user.getPassword()
        );

        // Store authentication in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate a JWT token for the authenticated user
        String jwt = jwtProvider.generateToken(authentication);


        // Prepare the response object with token and message
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Register Success");
        authResponse.setRole(savedUser.getRole());

        // Return response with HTTP 201 (Created)
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/signing")
    public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest req){

        String username = req.getEmail();
        String password = req.getPassword();

        // Perform manual authentication using our custom authentication method
        Authentication authentication=authentication(username,password);
        // Extract user roles/authorities from the Authentication object
        Collection<? extends GrantedAuthority> authorities=authentication.getAuthorities();

        String role=authorities.isEmpty()?null:authorities.iterator().next().getAuthority();

        // Generate JWT Token using the authenticated user's details
        String jwt=jwtProvider.generateToken(authentication);
        // Prepare Response Body (DTO)
        AuthResponse authResponse=new AuthResponse();

        authResponse.setJwt(jwt);
        authResponse.setMessage("Register Successfully");


        authResponse.setRole(USER_ROLE.valueOf(role));
// Return ResponseEntity with 200 OK status
        return new ResponseEntity<>(authResponse,HttpStatus.OK);

    }

    // --------------------------------------------------------
// ✔ Custom Authentication Method
//    - Checks user existence
//    - Validates password
//    - Returns Authentication Object
// --------------------------------------------------------
    private Authentication authentication(String username, String password) {

        UserDetails userDetails = customerUserDetailsService.loadUserByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username");
        }

        // Check if password matches
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid Credential");
        }

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }


}
