package org.csu.mypetstorebyssm.persistence;

import org.csu.mypetstorebyssm.domain.User;

public interface UserMapper {
    public User findUserByUsername(String username);
    public int updateUserByUsername(User user);
    public User findUserByUsernameAndPassword(User user);
    public int insertUserByUsernameAndPassword(User user);
}
