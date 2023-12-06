package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

@RequestMapping("/owners")
@Controller
public class OwnerController {

    private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FROM = "owners/createOrUpdateOwnerForm";
    private final OwnerService ownerService;

    @Autowired
    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @RequestMapping("/find")
    public String findOwners(Model model) {
        model.addAttribute("owner", Owner.builder().build());
        return "owners/findOwners";
    }
    @GetMapping({"","/"})
    public String processFindForm(Owner owner, BindingResult result,Model model) {
        if (owner.getLastName() == null) {
            owner.setLastName("");
        }
        Collection<Owner> results = ownerService.findAllByLastNameLike("%" + owner.getLastName() + "%");
        if(results.isEmpty()) {
            result.rejectValue("lastName","notFound","notFound");
            return "owners/findOwners";
        } else if(results.size() == 1) {
            owner = results.iterator().next();
            return "redirect:/owners/" + owner.getId();
        } else {
            model.addAttribute("selections",results);
            return "owners/ownersList";
        }
    }

    @GetMapping("/{ownerId}")
    public ModelAndView showOwner(@PathVariable Long ownerId) {
        ModelAndView mav = new ModelAndView("owners/ownerDetails");
        mav.addObject(ownerService.findById(ownerId));
        return mav;
    }
    @GetMapping("/new")
    public String initCreationForm(Model model) {
        model.addAttribute("owner",Owner.builder().build());
        return VIEWS_OWNER_CREATE_OR_UPDATE_FROM;
    }
    @PostMapping("/new")
    public String processCreationForm(@Valid Owner owner, BindingResult result) {
        if (result.hasErrors()) {
            return VIEWS_OWNER_CREATE_OR_UPDATE_FROM;
        } else {
            Owner savedOwner = ownerService.save(owner);
            return "redirect:/owners/" + savedOwner.getId();
        }
    }
    @GetMapping("/{ownerId}/edit")
    public String initUpdateForm(@PathVariable Long ownerId,Model model) {
        Owner owner = ownerService.findById(ownerId);
        if (owner == null) {
            return "redirect:/owners";
        } else {
            model.addAttribute("owner", owner);
            return VIEWS_OWNER_CREATE_OR_UPDATE_FROM;
        }
    }
    @PostMapping("/{ownerId}/edit")
    public String processUpdateOwnerForm(@PathVariable Long ownerId, @Valid Owner owner, BindingResult result) {
        if(result.hasErrors()) {
            return VIEWS_OWNER_CREATE_OR_UPDATE_FROM;
        } else {
            owner.setId(ownerId);
            Owner savedOwner = ownerService.save(owner);
            return "redirect:/owners/" + ownerId;
        }
    }
}
