package com.Online_Food.service;

import com.Online_Food.model.User;

public interface UserService {

    public User findUserByJwtToken(String jwt) throws Exception;

    public User findUserByEmail(String email) throws  Exception;

}
