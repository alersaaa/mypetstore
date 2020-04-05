package org.csu.mypetstorebyssm.service;

import org.csu.mypetstorebyssm.domain.User;
import org.csu.mypetstorebyssm.persistence.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User findUserByUsername(String username){
        return userMapper.findUserByUsername(username);
    }
    public int updateUserByUsername(User user){
        return userMapper.updateUserByUsername(user);
    }
    public User findUserByUsernameAndPassword(User user){
        return userMapper.findUserByUsernameAndPassword(user);
    }
    public int insertUserByUsernameAndPassword(User user) {
        User user1 = findUserByUsername(user.getUsername());
        if(user1 == null) {
            return userMapper.insertUserByUsernameAndPassword(user);
        }else {
            return 0;
        }
    }
}
