package org.csu.mypetstorebyssm.service;

import org.csu.mypetstorebyssm.domain.Category;
import org.csu.mypetstorebyssm.domain.Item;
import org.csu.mypetstorebyssm.domain.Product;
import org.csu.mypetstorebyssm.persistence.CategoryMapper;
import org.csu.mypetstorebyssm.persistence.ItemMapper;
import org.csu.mypetstorebyssm.persistence.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ItemMapper itemMapper;

    public Category getCategory(String categoryId){
         return categoryMapper.getCategory(categoryId);
    }

    public List<Product> getProductListByCategory(String categoryId){
        return productMapper.getProductListByCategory(categoryId);
    }

    public Product getProduct(String productId){
        return productMapper.getProduct(productId);
    }

    public List<Product> searchProductList(String keywords){
        return productMapper.searchProductList(keywords);
    }

    public int getInventoryQuantity(String itemId){
        return itemMapper.getInventoryQuantity(itemId);
    }

    public List<Item> getItemListByProduct(String productId){
        return itemMapper.getItemListByProduct(productId);
    }

    public Item getItem(String itemId){
        return itemMapper.getItem(itemId);
    }
}
