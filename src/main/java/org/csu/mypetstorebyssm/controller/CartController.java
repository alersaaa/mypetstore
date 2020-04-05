package org.csu.mypetstorebyssm.controller;

import org.csu.mypetstorebyssm.domain.CartItem;
import org.csu.mypetstorebyssm.domain.Item;
import org.csu.mypetstorebyssm.domain.User;
import org.csu.mypetstorebyssm.service.CartService;
import org.csu.mypetstorebyssm.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    HttpServletRequest request;
    @Autowired
    CartService cartService;
    @Autowired
    CatalogService catalogService;

    @GetMapping("/view")
    public String viewCart(Model model){
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        if(user == null) {
            String message = "请先登录或注册。";
            model.addAttribute("message", message);
            return "common/error";
        }else {
            List<CartItem> cart = (List<CartItem>)session.getAttribute("cart");
            if(cart == null){
                String username = user.getUsername();
                cart = cartService.selectCartItemByUsername(username);
                session.setAttribute("cart", cart);
            }
            return "cart/cartView";
        }
    }

    @GetMapping("/addItem")
    public String addItem(String workingItemId, Model model){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(user == null){
            String message = "您尚未登录，请先登录或注册";
            model.addAttribute("message", message);
            return "common/error";
        } else {
            String username = user.getUsername();
            CartItem res = new CartItem();
            res.setUsername(username);
            res.setItemId(workingItemId);
            CartItem cartItem = cartService.selectCartItemByUsernameAndItemId(res);

            if (cartItem != null) {
                if(!cartItem.isPay()) {
                    cartService.incrementQuantity(res);
                }
                else {
                    res.setPay(false);
                    cartService.updateCartItemByItemIdAndPay(res);
                    cartService.updateQuantity(cartItem, 1);
                    System.out.println("success!");
                }
            }else {
                Item item = catalogService.getItem(workingItemId);
                int quantity = catalogService.getInventoryQuantity(workingItemId);
                if(quantity > 0){
                    res.setInStock(true);
                    res.setQuantity(1);
                    res.setPay(false);
                    cartService.calculatetotalcost(res);
                    cartService.insertCartItemByUsernameAndItemId(res);
                }
            }
            List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
            if (cart == null) {
                cart = new ArrayList<CartItem>();
            }
            cart = cartService.selectCartItemByUsername(username);
            session.setAttribute("cart", cart);
            return "cart/cartView";
        }
    }

    @GetMapping("/removeItemFromCart")
    public String  removeItemFromCart(String workingItemId, Model model){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(user == null){
            String message = "请先登录。";
            model.addAttribute("message", message);
            return "common/error";
        }
        else {
            String username = user.getUsername();
            CartItem res = new CartItem();
            res.setUsername(username);
            res.setItemId(workingItemId);
            CartItem cartItem = cartService.selectCartItemByUsernameAndItemId(res);

            if (cartItem == null) {
                String message = "Attempted to remove all null CartItem from Cart.";
                model.addAttribute("message", message);
                return "common/error";
            } else {
                cartService.removeCartItemByUsernameAndItemId(res);
                List<CartItem> cart = cartService.selectCartItemByUsername(username);
                session.setAttribute("cart", cart);
                return "cart/cartView";
            }
        }
    }

    @PostMapping("/updateCart")
    public String updateCart(){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String username = user.getUsername();

        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()){
            String itemId = paramNames.nextElement();
            String quantityString = request.getParameter(itemId);
            int quantity = 0;
            CartItem res = new CartItem();
            res.setItemId(itemId);
            res.setUsername(username);
            if(quantityString == "" || quantityString.equals(null)){
                cartService.removeCartItemByUsernameAndItemId(res);
            }
            else {
                quantity = Integer.parseInt(quantityString);
                if(quantity == 0){
                    cartService.removeCartItemByUsernameAndItemId(res);
                }
                else {
                    cartService.updateQuantity(res, quantity);
                }
            }
        }

        List<CartItem> cart = cartService.selectCartItemByUsername(username);
        session.setAttribute("cart", cart);
        return "cart/cartView";
    }

    @PostMapping("/updateCartItem")
    @ResponseBody
    public void updateCartItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String username = user.getUsername();
        String itemId = request.getParameter("itemId");
        String quantityStr = request.getParameter("quantity");
        int quantity = 0;
        CartItem res = new CartItem();
        res.setUsername(username);
        res.setItemId(itemId);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if(quantityStr == "" || quantityStr.equals(null)){
            cartService.removeCartItemByUsernameAndItemId(res);
            out.write("{\"isRemoved\":\""+true+"{\"itemId\":\"" + itemId + "\"}");
        }
        else {
            quantity = Integer.parseInt(quantityStr);
            System.out.println(itemId+"数量"+quantity);
            if(quantity == 0){
                cartService.removeCartItemByUsernameAndItemId(res);
                out.write("{\"isRemoved\":\"" + true + "\",\"itemId\":\"" + itemId + "\"}");
            }
            else {
                res.setQuantity(quantity);
                cartService.updateCartItemByUsernameAndItemId(res);
                CartItem item = cartService.selectCartItemByUsernameAndItemId(res);
                String html = "<fmt:formatNumber type='number' pattern='$#,##0.00'>$" + item.getTotalcost() + ".00</fmt:formatNumber>";
                System.out.println("html"+html);
                out.write("{\"isRemoved\":\"" + false + "\",\"itemId\":\"" + itemId + "\",\"quantity\":\"" + quantity +
                        "\",\"totalcost\":\"" + item.getTotalcost() + "\",\"html\":\"" + html + "\"}");
            }
        }

        List<CartItem> cart = cartService.selectCartItemByUsername(username);
        session.setAttribute("cart", cart);
        out.flush();
        out.close();
    }
}
