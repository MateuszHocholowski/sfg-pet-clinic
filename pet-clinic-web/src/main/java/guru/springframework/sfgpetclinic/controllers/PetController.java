package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.PetType;
import guru.springframework.sfgpetclinic.services.OwnerService;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.PetTypeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Controller
@RequestMapping("/owners/{ownerId}/")
public class PetController {

    private final String VIEW_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";
    private final PetService petService;
    private final PetTypeService petTypeService;
    private final OwnerService ownerService;

    public PetController(PetService petService, PetTypeService petTypeService, OwnerService ownerService) {
        this.petService = petService;
        this.petTypeService = petTypeService;
        this.ownerService = ownerService;
    }

    @ModelAttribute("types")
    public Collection<PetType> populatePetTypes() {
        return petTypeService.findAll();
    }
    @ModelAttribute("owner")
    public Owner findOwner(@PathVariable Long ownerId) {
        return ownerService.findById(ownerId);
    }
    @InitBinder("owner")
    public void initOwnerBinder(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }
    @GetMapping("/pets/new")
    public String initCreateForm(Model model, Owner owner) {
        Pet pet = new Pet();
        owner.getPets().add(pet);
        model.addAttribute("pet",pet);

        return VIEW_PETS_CREATE_OR_UPDATE_FORM;
    }
    @PostMapping("/pets/new")
    public String processCreateForm(Owner owner, @Valid Pet pet, BindingResult result, Model model) {
        if (StringUtils.hasLength(pet.getName()) && pet.isNew() && (owner.getPet(pet.getName(),false) != null)) {
            result.rejectValue("name","duplicate","already exists");
        }
        if (result.hasErrors()) {
            model.addAttribute("pet",pet);
            return VIEW_PETS_CREATE_OR_UPDATE_FORM;
        } else {
            Pet savedPet = petService.save(pet);
            return "redirect:/owners/" + owner.getId();
        }
    }
    @GetMapping("/pets/{petId}/edit")
    public String initUpdateForm(Owner owner, @PathVariable Long petId, Model model) {
        model.addAttribute("pet",petService.findById(petId));
        return VIEW_PETS_CREATE_OR_UPDATE_FORM;
    }
    @PostMapping("/pets/{petId}/edit")
    public String processUpdateForm(Owner owner, @Valid Pet pet, BindingResult result, Model model) {
        if (result.hasErrors()) {
            pet.setOwner(owner);
            model.addAttribute("pet",pet);
            return VIEW_PETS_CREATE_OR_UPDATE_FORM;
        } else {
            owner.getPets().add(pet);
            Pet savedPet = petService.save(pet);
            return "redirect:/owners/" + owner.getId();
        }
    }
}
