package org.csu.mypetstorebyssm.listener;

import org.csu.mypetstorebyssm.domain.Log;
import org.csu.mypetstorebyssm.domain.User;
import org.csu.mypetstorebyssm.service.LogService;
import org.csu.mypetstorebyssm.service.UserService;
import org.csu.mypetstorebyssm.util.InjectServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@WebListener
@Component
public class PrefStatListener implements ServletContextListener, ServletRequestListener, HttpSessionAttributeListener {
    private static LogService logService;

    public PrefStatListener(){

    }

    public void contextInitialized(ServletContextEvent sec){
        ServletContext servletContext = sec.getServletContext();
        servletContext.setAttribute("userCounter",new AtomicInteger());
    }

    public void contextDestroyed(ServletContextEvent sec){
        ServletContext servletContext = sec.getServletContext();
        servletContext.removeAttribute("userCounter");
    }

    public void attributeAdded(HttpSessionBindingEvent e){
        String name = e.getName();
        ServletContext servletContext = e.getSession().getServletContext();

        if(name.equals("user")){
            AtomicInteger userCounter = (AtomicInteger)servletContext.getAttribute("userCounter");
            int userCount = userCounter.incrementAndGet();
            System.out.println("userCount incremented to:" + userCount);

            User user = (User) e.getSession().getAttribute("user");
            Log log = new Log();
            log.setUsername(user.getUsername());
            log.setLoginTime(new Timestamp(new Date().getTime()).toString());
            e.getSession().setAttribute("log", log);
        }
    }

    public void attributeRemoved(HttpSessionBindingEvent e) {
        String name = e.getName();
        ServletContext servletContext = e.getSession().getServletContext();

        if(name.equals("user")) {
            AtomicInteger userCounter = (AtomicInteger) servletContext.getAttribute("userCounter");
            int userCount = userCounter.decrementAndGet();
            System.out.println("---------userCount decremented to:" + userCount);
            e.getSession().removeAttribute("log");
        }
    }

    public void attributeReplaced(HttpSessionBindingEvent e) {
    }

    @Override
    public void requestInitialized(ServletRequestEvent sec){
        ServletRequest servletRequest = sec.getServletRequest();
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        Log log = (Log) httpServletRequest.getSession().getAttribute("log");
        HttpSession session = httpServletRequest.getSession();
        servletRequest.setAttribute("start",System.nanoTime());
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sec){
        ServletRequest servletRequest = sec.getServletRequest();
        Long start = (Long) servletRequest.getAttribute("start");
        Long end = System.nanoTime();
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        String uri = httpServletRequest.getRequestURI();
        System.out.println("time taken to execute " + uri
                + ":" + ((end-start)/1000) + "microseconds");

        System.out.println("bot");
        Log log = (Log) httpServletRequest.getSession().getAttribute("log");
        if(log != null) {
            String strUrl = "http://" + httpServletRequest.getServerName() + ":" + httpServletRequest.getServerPort()
                    + httpServletRequest.getContextPath() + httpServletRequest.getServletPath() + "?" + (httpServletRequest.getQueryString());
            log.setStrURL(strUrl);
            logService = InjectServiceUtil.getLogService();
            logService.insertLogByUserNameAndLoginTimeAndUrl(log);
        }
    }
}
