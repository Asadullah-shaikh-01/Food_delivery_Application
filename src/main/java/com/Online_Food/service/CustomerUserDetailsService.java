//package com.Online_Food.service;
//
//
//import com.Online_Food.model.USER_ROLE;
//import com.Online_Food.model.User;
//import com.Online_Food.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import javax.management.relation.Role;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class CustomerUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByEmail(username);
//        if(user != null){
//            throw  new UsernameNotFoundException("User Not Found");
//        }
//
//        USER_ROLE role = user.getRole();
//        if(role ==null)role=USER_ROLE.ROLE_CUSTOMER;
//        List<GrantedAuthority> authorities= new ArrayList<>();
//
//        authorities.add(new SimpleGrantedAuthority(role.toString()));
//
//
//        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
//    }
//}

package com.Online_Food.service;

import com.Online_Food.model.USER_ROLE;
import com.Online_Food.model.User;
import com.Online_Food.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


//#Todo: Its Stop Auto generated Password by Spring Security
@Service // Marks this class as a Spring-managed service component
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired // Injects the UserRepository dependency automatically
    private UserRepository userRepository;

    /**
     * This method is automatically called by Spring Security when a user tries to log in.
     * It loads user details (email, password, roles) from the database using the given username (email).
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find user by email (username)
        User user = userRepository.findByEmail(username);

        // If user is not found, throw exception — Spring Security will stop authentication
        if (user == null) {
            throw new UsernameNotFoundException("User Not Found" + username);
        }

        // Get the user's role (e.g., ROLE_ADMIN, ROLE_CUSTOMER, etc.)
        USER_ROLE role = user.getRole();

//        if(role == null) role=USER_ROLE.ROLE_CUSTOMER;

        // Create a list of authorities (permissions/roles) for Spring Security
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Add the user's role as a GrantedAuthority
        // Spring expects roles in the format "ROLE_<NAME>"
        authorities.add(new SimpleGrantedAuthority(role.toString()));

        /**
         * Return a Spring Security User object that contains:
         * - username (email)
         * - password (hashed)
         * - authorities (roles)
         *
         * Spring Security will use this object to authenticate and authorize the user.
         */
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}

