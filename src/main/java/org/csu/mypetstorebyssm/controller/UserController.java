package org.csu.mypetstorebyssm.controller;

import org.csu.mypetstorebyssm.domain.User;
import org.csu.mypetstorebyssm.service.UserService;
import org.csu.mypetstorebyssm.util.HtmlText;
import org.csu.mypetstorebyssm.util.JavaMailUtil;
import org.csu.mypetstorebyssm.util.RandomNumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.UnresolvedAddressException;
import java.sql.DataTruncation;
import java.util.Properties;
import java.util.Random;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserService userService;

    @GetMapping("/viewSignin")
    public String viewSignin(Model model){
        String msg = "";
        model.addAttribute("msg",msg);
        return "account/signin";
    }

    @GetMapping("/viewRegister")
    public String viewRegister(Model model){
        String reminder = "";
        model.addAttribute("reminder", reminder);
        return "account/register";
    }

    @GetMapping("/viewMyAccount")
    public String viewMyAccount(Model model){
        String message = "";
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        if(user == null){
            message = "请先登录或注册";
            model.addAttribute("message", message);
            return "common/error";
        }else{
            model.addAttribute("message", message);
            return "account/myAccount";
        }
    }

    @PostMapping("/signin")
    public String sigin(String username, String password, String inputCode, Model model)
      throws ServletException, IOException{
        String msg = "";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        HttpSession session = request.getSession();
        String authCode = session.getAttribute("authCode").toString();
        if(authCode.equals(inputCode)){
            if(userService.findUserByUsernameAndPassword(user) != null){
                model.addAttribute("user",user);
                session.setAttribute("user", user);
                return "catalog/main";
            } else{
                msg = "用户名或密码错误";
                model.addAttribute("msg", msg);
                return "account/signin";
            }
        } else{
            msg="验证码错误";
        }
        model.addAttribute("msg", msg);
        return "account/signin";
    }

    @PostMapping("/register")
    public String register(String username, String password, String email, String vcode, Model model){
        HttpSession session = request.getSession();
        String sessionCode = session.getAttribute("vCode").toString();
        if(sessionCode != null){
            if(sessionCode.equals(vcode)){
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                user.setEmail(email);
                if(userService.insertUserByUsernameAndPassword(user) > 0){
                    session.setAttribute("user", user);
                    return "catalog/main";
                }else {
                    String reminder = "用户名已存在";
                    model.addAttribute("reminder", reminder);
                    return "account/register";
                }
            }else {
                String reminder = "验证码错误";
                model.addAttribute("reminder", reminder);
                return "account/register";
            }
        }
        String reminder = "邮件发送失败";
        model.addAttribute("reminder", reminder);
        return "account/register";
    }

    @GetMapping("/usernameIsExist")
    @ResponseBody
    public void usernameIsExist(HttpServletResponse response, String username, Model model) throws IOException{
        User user = userService.findUserByUsername(username);
        response.setContentType("text/plain");

        PrintWriter out = response.getWriter();
        if(user != null){
            out.print("Exist");
        }
        else {
            out.print("Not Exist");
        }

        out.flush();
        out.close();
    }

    @PostMapping("/sendVCode")
    @ResponseBody
    public void sendVCode(String email, Model model) throws IOException{
        String reminder = null;
        String vCode = null;
        try{
            JavaMailUtil.receiveMailAccount = email;

            Properties pops = new Properties();
            pops.setProperty("mail.debug","true");
            pops.setProperty("mail.smtp.auth","true");
            pops.setProperty("mail.host",JavaMailUtil.emailSMTPHost);
            pops.setProperty("mail.transport.protocol","smtp");
            Session session = Session.getInstance(pops);
            session.setDebug(true);
            vCode = RandomNumberUtil.getRandomNumber();
            System.out.println("邮箱验证码" + vCode);
            String html = HtmlText.html(vCode);
            MimeMessage message = JavaMailUtil.creatMimeMessage(session, JavaMailUtil.emailAccount,
                    JavaMailUtil.receiveMailAccount,html);
            Transport transport = session.getTransport();
            transport.connect(JavaMailUtil.emailAccount,JavaMailUtil.emailPassword);
            transport.sendMessage(message,message.getAllRecipients());
            transport.close();

            reminder = "验证码发送成功";
            model.addAttribute("reminder",reminder);
            request.getSession().setAttribute("vCode",vCode);
        }
        catch (MessagingException m){
            m.printStackTrace();
            request.getSession().setAttribute("error","邮件发送失败");
        }
    }

    @PostMapping("/saveAccount")
    public String saveAccount(String username, String password, String repeatpwd, String firstname, String  lastname, String email, String phone,
                              String address1, String address2, String city, String state, String zip, String country,
                              String languagePreference, String favoriteCatagoryId, String listOption, String bannerOption, Model model){
         if(!password.equals(repeatpwd)){
             String message = "两次输入的密码不一致";
             model.addAttribute("message", message);
             return "account/myAccount";
         }else{
             User user = new User();
             user.setUsername(username);
             user.setPassword(password);
             user.setFirstname(firstname);
             user.setLastname(lastname);
             user.setEmail(email);
             user.setPhone(phone);
             user.setAddress1(address1);
             user.setAddress2(address2);
             user.setCity(city);
             user.setState(state);
             user.setZip(zip);
             user.setCountry(country);
             user.setLanguagepre(languagePreference);
             user.setFavoritecata(favoriteCatagoryId);
             user.setIflist(listOption);
             user.setIfbanner(bannerOption);
             if(userService.updateUserByUsername(user) > 0){
                 HttpSession session = request.getSession();
                 session.setAttribute("user", user);
                 return "catalog/main";
             }else{
                 String message = "修改失败";
                 model.addAttribute("message", message);
                 return "account/myAccount";
             }
         }
    }

    @GetMapping("/signout")
    public String signOut(Model model){
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        if(user == null){
            String message = "您尚未登录，无法退出";
            model.addAttribute("message", message);
            return "common/error";
        }else{
            session.removeAttribute("user");
            return "catalog/main";
        }
    }
}
