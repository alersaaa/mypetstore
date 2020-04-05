package org.csu.mypetstorebyssm.service;

import org.csu.mypetstorebyssm.domain.*;
import org.csu.mypetstorebyssm.persistence.CartMapper;
import org.csu.mypetstorebyssm.persistence.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CartMapper cartMapper;

    public List<Order> getOrdersByUsername(String username){
        return orderMapper.getOrdersByUsername(username);
    }

    public Order getOrder(int orderId){
        return orderMapper.getOrder(orderId);
    }

    public int insertOrder(Order order){
        return orderMapper.insertOrder(order);
    }

    public Order initOrder(User account, List<CartItem> cart) {
        Order order = new Order();

        order.setUsername(account.getUsername());
        order.setOrderDate(new Date(System.currentTimeMillis()));

        order.setShipToFirstName(account.getFirstname());
        order.setShipToLastName(account.getLastname());
        order.setShipAddress1(account.getAddress1());
        order.setShipAddress2(account.getAddress2());
        order.setShipCity(account.getCity());
        order.setShipState(account.getState());
        order.setShipZip(account.getZip());
        order.setShipCountry(account.getCountry());

        order.setBillToFirstName("ABC");
        order.setBillToLastName("XYX");
        order.setBillAddress1("901 San Antonio Road");
        order.setBillAddress2("MS UCUP02-206");
        order.setBillCity("Palo Alto");
        order.setBillState("CA");
        order.setBillZip("94303");
        order.setBillCountry("USA");

        order.setTotalPrice(getSubTotal(cart));

        order.setCreditCard("999 9999 9999 9999");
        order.setExpiryDate("12/03");
        order.setCardType("Visa");
        order.setCourier("UPS");
        order.setLocale("CA");
        order.setStatus("P");

        Iterator<CartItem> i = cart.iterator();
        while (i.hasNext()) {
            CartItem cartItem = (CartItem) i.next();
            addLineItem(order, cartItem);
        }
        return order;
    }

    public void addLineItem(Order order, CartItem cartItem) {
        LineItem lineItem = new LineItem(order.getLineItems().size() + 1, cartItem);
        addLineItem(order, lineItem);
    }

    public void addLineItem(Order order, LineItem lineItem) {
        order.getLineItems().add(lineItem);
    }

    public List<CartItem> selectItemByUsername(String username){
        return cartMapper.selectCartItemByUsername(username);
    }

    public BigDecimal getSubTotal(List<CartItem> cartItemList){
        BigDecimal subTotal = new BigDecimal("0");
        Iterator<CartItem> items = cartItemList.iterator();
        while(items.hasNext()){
            CartItem cartItem = (CartItem) items.next();
            Item item = cartItem.getItem();
            BigDecimal listPrice = item.getListPrice();
            BigDecimal quantity = new BigDecimal(String.valueOf(cartItem.getQuantity()));
            subTotal = subTotal.add(listPrice.multiply(quantity));
        }
        return subTotal;
    }
}
