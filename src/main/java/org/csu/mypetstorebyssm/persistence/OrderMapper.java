package org.csu.mypetstorebyssm.persistence;

import org.csu.mypetstorebyssm.domain.Order;

import java.util.List;

public interface OrderMapper {
    List<Order> getOrdersByUsername(String username);

    Order getOrder(int orderId);

    int insertOrder(Order order);
}
