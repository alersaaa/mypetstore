package org.csu.mypetstorebyssm.controller;

import org.csu.mypetstorebyssm.domain.Category;
import org.csu.mypetstorebyssm.domain.Item;
import org.csu.mypetstorebyssm.domain.Product;
import org.csu.mypetstorebyssm.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
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
@RequestMapping("/catalog")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;
    @Autowired
    private HttpServletRequest request;

    @GetMapping("/view")
    public String view(){
        return "catalog/main";
    }

    @GetMapping("/viewCategory")
    public String getCategory(String categoryId, Model model){
        if(categoryId != null){
            Category category = catalogService.getCategory(categoryId);
            List<Product> productList = catalogService.getProductListByCategory(categoryId);
            model.addAttribute("category",category);
            model.addAttribute("productList",productList);
            return "catalog/category";
        }
        return "catalog/main";
    }

    @GetMapping("/viewProduct")
    public String getProduct(String productId, Model model){
        if(productId != null){
            Product product = catalogService.getProduct(productId);
            List<Item> itemList = catalogService.getItemListByProduct(productId);
            model.addAttribute("product",product);
            model.addAttribute("itemList", itemList);
            return "catalog/product";
        }
        return "catalog/category";
    }

    @GetMapping("/viewItem")
    public String getItemList(String itemId, Model model){
        if(itemId != null){
            int quantity = catalogService.getInventoryQuantity(itemId);
            Item item = catalogService.getItem(itemId);
            Product product = catalogService.getProduct(item.getProductId());
            model.addAttribute("quantity",quantity);
            model.addAttribute("item",item);
            model.addAttribute("product",product);
            return "catalog/item";
        }
        return "catalog/product";
    }

    @PostMapping("/searchProducts")
    public String searchProducts(String keyword, Model model){
        Product product=new Product();
        Item item=new Item();
        List<Product> productList=new ArrayList<Product>();
        productList=catalogService.searchProductList(keyword);
        List<Item> itemList=new ArrayList<>();

        HttpSession session=request.getSession();

        session.setAttribute("productList",productList);
        return "catalog/searchProducts";
    }
}
