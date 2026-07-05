package com.growthops.travelinsurance.controller;

import com.growthops.travelinsurance.dto.CustomerForm;
import com.growthops.travelinsurance.dto.JourneyForm;
import com.growthops.travelinsurance.entity.Policy;
import com.growthops.travelinsurance.repository.PolicyRepository;
import com.growthops.travelinsurance.service.InsuranceService;
import com.growthops.travelinsurance.service.PricingService;
import com.growthops.travelinsurance.validator.CustomerFormValidator;
import com.growthops.travelinsurance.validator.JourneyFormValidator;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class InsuranceController {

  private final PricingService pricingService;
  private final InsuranceService insuranceService;
  private final PolicyRepository policyRepository;
  private final JourneyFormValidator journeyFormValidator;
  private final CustomerFormValidator customerFormValidator;

  @Autowired
  public InsuranceController(
      PricingService pricingService,
      InsuranceService insuranceService,
      PolicyRepository policyRepository,
      JourneyFormValidator journeyFormValidator,
      CustomerFormValidator customerFormValidator) {
    this.pricingService = pricingService;
    this.insuranceService = insuranceService;
    this.policyRepository = policyRepository;
    this.journeyFormValidator = journeyFormValidator;
    this.customerFormValidator = customerFormValidator;
  }

  @InitBinder("journeyForm")
  protected void initJourneyBinder(WebDataBinder binder) {
    binder.addValidators(journeyFormValidator);
  }

  @InitBinder("customerForm")
  protected void initCustomerBinder(WebDataBinder binder) {
    binder.addValidators(customerFormValidator);
  }

  @GetMapping
  public String index() {
    return "redirect:/step1";
  }

  @GetMapping("/step1")
  public String step1(Model model, HttpSession session) {
    JourneyForm journeyForm = (JourneyForm) session.getAttribute("journeyForm");
    if (journeyForm == null) {
      journeyForm = new JourneyForm();
    }
    model.addAttribute("journeyForm", journeyForm);
    return "step1";
  }

  @PostMapping("/step1")
  public String postStep1(
      @Valid @ModelAttribute("journeyForm") JourneyForm journeyForm,
      BindingResult bindingResult,
      HttpSession session) {
    if (bindingResult.hasErrors()) {
      return "step1";
    }
    session.setAttribute("journeyForm", journeyForm);
    return "redirect:/step2";
  }

  @GetMapping("/step2")
  public String step2(Model model, HttpSession session) {
    JourneyForm journeyForm = (JourneyForm) session.getAttribute("journeyForm");
    if (journeyForm == null) {
      return "redirect:/step1";
    }

    BigDecimal priceA =
        pricingService.calculatePremium(
            journeyForm.getCoverageType(),
            journeyForm.getAreaOfTravel(),
            "PLAN_A",
            journeyForm.getStartDate(),
            journeyForm.getEndDate());

    BigDecimal priceB =
        pricingService.calculatePremium(
            journeyForm.getCoverageType(),
            journeyForm.getAreaOfTravel(),
            "PLAN_B",
            journeyForm.getStartDate(),
            journeyForm.getEndDate());

    model.addAttribute("journeyForm", journeyForm);
    model.addAttribute("priceA", priceA);
    model.addAttribute("priceB", priceB);

    String selectedPlan = (String) session.getAttribute("selectedPlan");
    model.addAttribute("selectedPlan", selectedPlan);

    return "step2";
  }

  @PostMapping("/step2")
  public String postStep2(
      @RequestParam(value = "plan", required = false) String plan,
      HttpSession session,
      RedirectAttributes redirectAttributes) {
    if (plan == null || (!plan.equals("PLAN_A") && !plan.equals("PLAN_B"))) {
      redirectAttributes.addFlashAttribute("error", "Please select a valid plan.");
      return "redirect:/step2";
    }
    session.setAttribute("selectedPlan", plan);
    return "redirect:/step3";
  }

  @GetMapping("/step3")
  public String step3(Model model, HttpSession session) {
    JourneyForm journeyForm = (JourneyForm) session.getAttribute("journeyForm");
    String selectedPlan = (String) session.getAttribute("selectedPlan");
    if (journeyForm == null || selectedPlan == null) {
      return "redirect:/step2";
    }

    CustomerForm customerForm = (CustomerForm) session.getAttribute("customerForm");
    if (customerForm == null) {
      customerForm = new CustomerForm();
    }
    model.addAttribute("customerForm", customerForm);
    return "step3";
  }

  @PostMapping("/step3")
  public String postStep3(
      @Valid @ModelAttribute("customerForm") CustomerForm customerForm,
      BindingResult bindingResult,
      HttpSession session) {
    if (bindingResult.hasErrors()) {
      return "step3";
    }
    session.setAttribute("customerForm", customerForm);
    return "redirect:/step4";
  }

  @GetMapping("/step4")
  public String step4(Model model, HttpSession session) {
    JourneyForm journeyForm = (JourneyForm) session.getAttribute("journeyForm");
    String selectedPlan = (String) session.getAttribute("selectedPlan");
    CustomerForm customerForm = (CustomerForm) session.getAttribute("customerForm");

    if (journeyForm == null || selectedPlan == null || customerForm == null) {
      return "redirect:/step3";
    }

    BigDecimal totalPrice =
        pricingService.calculatePremium(
            journeyForm.getCoverageType(),
            journeyForm.getAreaOfTravel(),
            selectedPlan,
            journeyForm.getStartDate(),
            journeyForm.getEndDate());

    model.addAttribute("journeyForm", journeyForm);
    model.addAttribute("selectedPlan", selectedPlan);
    model.addAttribute("customerForm", customerForm);
    model.addAttribute("totalPrice", totalPrice);

    return "step4";
  }

  @PostMapping("/step4")
  public String postStep4(HttpSession session, RedirectAttributes redirectAttributes) {
    JourneyForm journeyForm = (JourneyForm) session.getAttribute("journeyForm");
    String selectedPlan = (String) session.getAttribute("selectedPlan");
    CustomerForm customerForm = (CustomerForm) session.getAttribute("customerForm");

    if (journeyForm == null || selectedPlan == null || customerForm == null) {
      redirectAttributes.addFlashAttribute(
          "error", "Session expired or invalid state. Please start over.");
      return "redirect:/step1";
    }

    try {
      Policy savedPolicy =
          insuranceService.createInsurancePurchase(journeyForm, selectedPlan, customerForm);

      session.removeAttribute("journeyForm");
      session.removeAttribute("selectedPlan");
      session.removeAttribute("customerForm");

      session.setAttribute("confirmedPolicyId", savedPolicy.getId());
      return "redirect:/success";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Error creating purchase: " + e.getMessage());
      return "redirect:/step4";
    }
  }

  @GetMapping("/success")
  public String success(Model model, HttpSession session) {
    Long policyId = (Long) session.getAttribute("confirmedPolicyId");
    if (policyId == null) {
      return "redirect:/step1";
    }

    Optional<Policy> policyOpt = policyRepository.findById(policyId);
    if (policyOpt.isEmpty()) {
      return "redirect:/step1";
    }

    model.addAttribute("policy", policyOpt.get());
    session.removeAttribute("confirmedPolicyId");

    return "success";
  }
}
