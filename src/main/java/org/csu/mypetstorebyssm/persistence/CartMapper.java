package org.csu.mypetstorebyssm.persistence;

import org.csu.mypetstorebyssm.domain.CartItem;
import org.csu.mypetstorebyssm.domain.Item;

import java.math.BigDecimal;
import java.util.List;

public interface CartMapper {
    public int updateCartItemByUsernameAndItemId(CartItem cartItem);
    public CartItem selectCartItemByUsernameAndItemId(CartItem cartItem);
    public int insertCartItemByUsernameAndItemId(CartItem cartItem);
    public List<CartItem> selectCartItemByUsername(String username);
    public int removeCartItemByUsernameAndItemId(CartItem cartItem);
    public int updateCartItemByItemIdAndPay(CartItem cartItem);
}
