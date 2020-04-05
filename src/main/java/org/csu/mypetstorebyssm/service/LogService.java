package org.csu.mypetstorebyssm.service;

import org.csu.mypetstorebyssm.domain.Log;
import org.csu.mypetstorebyssm.persistence.LogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {
    @Autowired
    private LogMapper logMapper;

    public int deleteByUserNameAndLoginTime(Log log){
        return logMapper.deleteByUserNameAndLoginTime(log);
    }
    public int insertLogByUserNameAndLoginTimeAndUrl(Log log){
        return logMapper.insertLogByUserNameAndLoginTimeAndUrl(log);
    }
}
