package com.godcoder.myhome.repository;

import com.godcoder.myhome.model.User;

import java.util.List;

public interface CustomizedUserRepository {

    List<User> findByUsernameCustom(String user);

    List<User> findByUsernameJdbc(String user);
}
