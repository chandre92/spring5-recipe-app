package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.exception.NotFoundException;
import guru.springframework.services.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipe/{id}/show")
    public String showById(@PathVariable String id, Model model) {
        model.addAttribute("recipe", recipeService.findById(id));

        return "recipe/show";
    }

    @GetMapping("recipe/new")
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());

        return "recipe/recipeform";
    }

    @GetMapping("recipe/{id}/update")
    public String updateRecipe(@PathVariable String id, Model model) {
        model.addAttribute("recipe", recipeService.findCommandById(id));

        return "recipe/recipeform";
    }

    @PostMapping("recipe")
    // @ModelAttribute("recipe") here for cases when has some validation errors added by @Valid annotation
    // so the recipe will be added to the model and re-displayed on the form.
    public String saveOrUpdate(@Valid @ModelAttribute("recipe") RecipeCommand recipeCommand, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "recipe/recipeform";
        }
        RecipeCommand savedCommand = recipeService.saveRecipeCommand(recipeCommand);

        return "redirect:/recipe/" + savedCommand.getId() + "/show";
    }

    @GetMapping("/recipe/{id}/delete")
    public String deleteById(@PathVariable String id) {
        recipeService.deleteById(id);

        return "redirect:/";
    }

    // Have to duplicate no matter what is specified in the @ResponseStatus in the NotFoundException
    // Otherwise there will be HTTP status 200 with 404error page
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFound(Exception exception) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error/404error");
        modelAndView.addObject("exception", exception);

        return modelAndView;
    }
}
