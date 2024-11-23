package com.example.foto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.foto.Payment;
import com.example.foto.PaymentRepository;

@Controller
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping("/payment")
    public String loadPaymentPage(@RequestParam(required = false) String cartData, Model model) {
        model.addAttribute("cart", cartData);
        return "payment"; // Ensure payment.html is in src/main/resources/templates
    }

    @PostMapping("/processPayment")
    public String processPayment(@RequestParam("productName") String productName,
                                 @RequestParam("amount") String amountString, 
                                 @RequestParam("remarks") String remarks,
                                 RedirectAttributes redirectAttributes) {
        try {
            // Convert the amount string to a double, removing the currency symbol if present
            Double amount = Double.valueOf(amountString.replace("रु", "").trim());

            // Create Payment object and set its attributes
            Payment payment = new Payment();
            payment.setProductName(productName);
            payment.setAmount(amount);
            payment.setRemarks(remarks);
            payment.setStatus("SUCCESS"); // Example status

            // Save the payment to the database
            paymentRepository.save(payment); 

            // Add attributes to redirect to the success page
            redirectAttributes.addAttribute("product", productName);
            redirectAttributes.addAttribute("total", amount);
            return "redirect:/paymentSuccess"; // Redirect to the success page
        } catch (NumberFormatException e) {
            // Redirect with an error message if there's an issue with the amount format
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid amount format. Please try again.");
            return "redirect:/payment"; // Redirect to payment page on error
        } catch (Exception e) {
            // Handle any other exceptions (e.g., database errors)
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while processing the payment. Please try again.");
            return "redirect:/payment"; // Redirect to payment page on error
        }
    }

    @GetMapping("/paymentSuccess")
    public String showSuccessPage(@RequestParam("product") String productName,
                                  @RequestParam("total") Double totalPrice,
                                  Model model) {
        model.addAttribute("productName", productName);
        model.addAttribute("totalPrice", totalPrice);
        return "success"; // Ensure success.html is in src/main/resources/templates
    }
}
