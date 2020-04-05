package org.csu.mypetstorebyssm.persistence;

import org.csu.mypetstorebyssm.domain.Log;

public interface LogMapper {
    public int deleteByUserNameAndLoginTime(Log log);
    public int insertLogByUserNameAndLoginTimeAndUrl(Log log);
}
