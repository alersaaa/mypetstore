package org.csu.mypetstorebyssm.service;

import org.csu.mypetstorebyssm.domain.CartItem;
import org.csu.mypetstorebyssm.domain.Item;
import org.csu.mypetstorebyssm.domain.Product;
import org.csu.mypetstorebyssm.persistence.CartMapper;
import org.csu.mypetstorebyssm.persistence.ItemMapper;
import org.csu.mypetstorebyssm.persistence.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ProductMapper productMapper;

    public int updateCartItemByUsernameAndItemId(CartItem cartItem){
        return cartMapper.updateCartItemByUsernameAndItemId(cartItem);
    }

    public CartItem selectCartItemByUsernameAndItemId(CartItem cartItem){
        return cartMapper.selectCartItemByUsernameAndItemId(cartItem);
    }

    public int insertCartItemByUsernameAndItemId(CartItem cartItem){
        return cartMapper.insertCartItemByUsernameAndItemId(cartItem);
    }

    public List<CartItem> selectCartItemByUsername(String username){
        List<CartItem> cartItemList = cartMapper.selectCartItemByUsername(username);
        for(int i = 0; i < cartItemList.size(); i++){
            CartItem cartItem = cartItemList.get(i);
            Item item = itemMapper.getItem(cartItem.getItemId());
            Product product = productMapper.getProduct(item.getProductId());
            item.setProduct(product);
            cartItem.setItem(item);
        }
        return cartItemList;
    }

    public int removeCartItemByUsernameAndItemId(CartItem cartItem){
        return cartMapper.removeCartItemByUsernameAndItemId(cartItem);
    }

    public int updateCartItemByItemIdAndPay(CartItem cartItem){
        return cartMapper.updateCartItemByItemIdAndPay(cartItem);
    }

    public void calculatetotalcost(CartItem cartItem){
        Item item = itemMapper.getItem(cartItem.getItemId());
        if(item != null && item.getListPrice() != null){
            BigDecimal totalcost = item.getListPrice().multiply(new BigDecimal(cartItem.getQuantity()));
            cartItem.setTotalcost(totalcost);
        }
        else{
            BigDecimal totalcost = null;
            cartItem.setTotalcost(totalcost);
        }
    }

    public void incrementQuantity(CartItem cartItem) {
        int quantity = cartItem.getQuantity();
        cartItem.setQuantity(++quantity);
        calculatetotalcost(cartItem);
        updateCartItemByUsernameAndItemId(cartItem);
    }

    public void updateQuantity(CartItem cartItem, int quantity){
        cartItem.setQuantity(quantity);
        calculatetotalcost(cartItem);
        updateCartItemByUsernameAndItemId(cartItem);
    }
}
