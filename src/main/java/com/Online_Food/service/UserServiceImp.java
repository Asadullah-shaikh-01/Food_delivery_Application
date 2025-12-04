package com.Online_Food.service;

import com.Online_Food.config.JwtProvider;
import com.Online_Food.model.User;
import com.Online_Food.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
public class UserServiceImp implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public User findUserByJwtToken(String jwt) throws Exception {
      String email= jwtProvider.getEmailFromJwtToken(jwt);

      User user=findUserByEmail(email);
      return  user;
    }


    @Override
    public User findUserByEmail(String email) throws Exception {
        User user= userRepository.findByEmail(email);


        if(user==null){
            throw new Exception("user not Found");
        }

        return user;

    }
}
