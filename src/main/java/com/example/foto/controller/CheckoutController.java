package com.example.foto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CheckoutController {

    // Handles checkout and redirects to the payment page with cart and total data as parameters
    @GetMapping("/checkout")
    public String showCheckoutPage() {
        return "checkout"; // Refers to checkout.html in static folder
    }

    @GetMapping("/initiatePayment")
    public String initiatePayment(@RequestParam("cart") String cartData,
                                  @RequestParam("product") String productName,
                                  @RequestParam("total") String totalPrice,
                                  Model model) {
        // Pass cart data, product name, and total price to the payment view
        model.addAttribute("cart", cartData);
        model.addAttribute("product", productName);
        model.addAttribute("total", totalPrice);

        return "payment"; // Refers to payment.html in templates folder
    }
}
