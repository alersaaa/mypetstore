package org.csu.mypetstorebyssm.util;

import org.csu.mypetstorebyssm.domain.User;
import org.csu.mypetstorebyssm.service.LogService;
import org.csu.mypetstorebyssm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class InjectServiceUtil {
    @Autowired
    private LogService logService;

    @PostConstruct
    public void init(){
        InjectServiceUtil.getInstance().logService = this.logService;
    }

    /**
     *  实现单例 start
     */
    private static class SingletonHolder {
        private static final InjectServiceUtil INSTANCE = new InjectServiceUtil();
    }
    private InjectServiceUtil(){}
    public static final InjectServiceUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }
    /**
     *  实现单例 end
     */
    public static LogService getLogService(){
        return InjectServiceUtil.getInstance().logService;
    }

}
