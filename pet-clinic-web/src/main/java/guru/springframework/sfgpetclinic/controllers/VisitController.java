package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.services.OwnerService;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.VisitService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/owners/{ownerId}/pets/{petId}")
public class VisitController {

    private final String VIEW_CREATE_OR_UPDATE_VISIT_FORM = "pets/createOrUpdateVisitForm";
    private final VisitService visitService;
    private final PetService petService;

    public VisitController(VisitService visitService, PetService petService, OwnerService ownerService) {
        this.visitService = visitService;
        this.petService = petService;
    }

//    @ModelAttribute("owner")
//    public Owner findOwner(@PathVariable Long ownerId) {
//        return ownerService.findById(ownerId);
//    }
//    @ModelAttribute("pet")
//    public Pet findPet(@PathVariable Long petId) {
//        return petService.findById(petId);
//    }
    @InitBinder//("owner")
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }
    @ModelAttribute("visit")
    public Visit loadPetWithVisit(@PathVariable Long petId, Model model) {
        Pet pet = petService.findById(petId);
        model.addAttribute("pet",pet);
        Visit visit = new Visit();
        pet.getVisits().add(visit);
        visit.setPet(pet);
        return visit;
    }
    @GetMapping("/visits/new")
    public String initCreateVisitForm(@PathVariable Long petId, Model model) {
        return VIEW_CREATE_OR_UPDATE_VISIT_FORM;
    }
    @PostMapping("/visits/new")
    public String processCreateVisitForm(@Valid Visit visit, BindingResult result) {
        if (result.hasErrors()) {
            return VIEW_CREATE_OR_UPDATE_VISIT_FORM;
        } else {
            Visit savedVisit = visitService.save(visit);
            Long ownerId = savedVisit.getPet().getOwner().getId();
            return "redirect:/owners/" + ownerId;
        }
    }
}
