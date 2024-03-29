package com.disertatie.subscription.controller;

import com.disertatie.subscription.dto.BenefitDTO;
import com.disertatie.subscription.dto.ResultDTO;
import com.disertatie.subscription.service.BenefitService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/benefits")
public class BenefitController {

    private BenefitService benefitService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{page}/{size}")
    public Page<BenefitDTO> getAllBenefitsPaged(@PathVariable(value = "page") int page,
                                                @PathVariable(value = "size") int size) {
        return benefitService.getAllBenefitsPaged(page, size);
    }

    @GetMapping("/user/{page}/{size}")
    public Page<BenefitDTO> getAllUserBenefitsPaged(@PathVariable(value = "page") int page,
                                                    @PathVariable(value = "size") int size,
                                                    Principal principal) {
        return benefitService.getAllUSerBenefitsPaged(page, size, principal);
    }

    @GetMapping("/unpagedBenefits")
    public List<BenefitDTO> getAllBenefits() {
        return benefitService.getAllBenefits();
    }

    @GetMapping("/bySubscription/{id}")
    public List<BenefitDTO> getBenefitsBySubscription(@PathVariable(value = "id") int id) {
        return benefitService.getBenefitsBySubscription(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public BenefitDTO createBenefit(@RequestBody BenefitDTO benefitDTO) {
        return benefitService.createBenefit(benefitDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResultDTO deleteBenefit(@RequestParam(value = "id") int id) {
        return benefitService.deleteBenefit(id);
    }

}
