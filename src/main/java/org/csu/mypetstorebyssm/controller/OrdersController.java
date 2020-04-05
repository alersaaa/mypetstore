package org.csu.mypetstorebyssm.controller;

import org.csu.mypetstorebyssm.domain.CartItem;
import org.csu.mypetstorebyssm.domain.Order;
import org.csu.mypetstorebyssm.domain.User;
import org.csu.mypetstorebyssm.persistence.OrderMapper;
import org.csu.mypetstorebyssm.service.CartService;
import org.csu.mypetstorebyssm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    HttpServletRequest request;
    @Autowired
    CartService cartService;
    @Autowired
    OrderService orderService;

    @GetMapping("/view")
    public String viewOrder(Model model){
        HttpSession session = request.getSession();
        // 获得购物车
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        User user = (User)session.getAttribute("user");
        if (cart == null){
            cart = cartService.selectCartItemByUsername(user.getUsername());
            session.setAttribute("cart",cart);
        }

        // 获得账户
        if(user == null){
            String message = "请先登录或注册。";
            model.addAttribute("message", message);
            return "common/error";
        }else {
            Order order = orderService.initOrder(user, cart);

            session.setAttribute("creditCardTypes", order.getCardType());
            session.setAttribute("order", order);
            return "order/order";
        }
    }

    @PostMapping("confirmOrder")
    public String confirmOrder(String cardType, String creditCard, String expiryDate, String  shippingAddressRequired, Model model){
        HttpSession session = request.getSession();
        User account = (User)session.getAttribute("user");

        // 修改订单消息
        Order order = (Order)session.getAttribute("order");
        order.setCardType(cardType);
        order.setCreditCard(creditCard);
        order.setExpiryDate(expiryDate);
        order.setShipToFirstName(account.getFirstname());

        order.setShipToLastName(account.getLastname());
        order.setShipAddress1(account.getAddress1());
        order.setShipAddress2(account.getAddress2());
        order.setShipCity(account.getCity());
        order.setShipState(account.getState());
        order.setShipZip(account.getZip());
        order.setShipCountry(account.getCountry());

        session.setAttribute("order",order);

        if(shippingAddressRequired != null){
            return "order/shopping";
        }else{
            return "order/order";
        }
    }

    @PostMapping("/confirmShip")
    public String confirmShip(String shipToFirstName, String shipToLastName, String shipAddress1, String shipAddress2,
                              String shipCity, String shipState, String shipZip, String shipCountry, Model model){
        HttpSession session = request.getSession();
        Order order = (Order)session.getAttribute("order");

        order.setShipToFirstName(shipToFirstName);
        order.setShipToLastName(shipToLastName);
        order.setShipAddress1(shipAddress1);
        order.setShipAddress2(shipAddress2);
        order.setShipCity(shipCity);
        order.setShipState(shipState);
        order.setShipZip(shipZip);
        order.setShipCountry(shipCountry);
        //覆盖原来的order
        session.setAttribute("order",order);

        return "order/confirmOrder";
    }

    @PostMapping("/order")
    public String order(Model model){
        HttpSession session = request.getSession();

        Order order = (Order) session.getAttribute("order");
        session.setAttribute("lineItems",order.getLineItems());

        if(orderService.insertOrder(order) > 0) {
            // 重置购物车
            CartService cartService = new CartService();
            User user = (User) session.getAttribute("user");
            String username = user.getUsername();
            List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
            for (int i = 0; i < cart.size(); i++) {
                CartItem res = new CartItem();
                res.setUsername(username);
                res.setItemId(cart.get(i).getItem().getItemId());
                res.setPay(true);
                cartService.updateCartItemByItemIdAndPay(res);
            }
            session.removeAttribute("cart");
        }

        return "order/viewOrder";
    }
}
