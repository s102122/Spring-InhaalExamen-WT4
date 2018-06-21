package edu.ap.spring.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.ap.spring.jpa.Recipe;
import edu.ap.spring.jpa.RecipeRepo;

@Controller
public class RecipeController {
	
	@Autowired
	private RecipeRepo recipeRepo;
   
   @RequestMapping("/")
   @ResponseBody
   public String helloWorld() {

	   return "Hello World";
   }   
   
   @RequestMapping("/listall")
   @ResponseBody
   public String getAllIngredients() {
	   
	   List<Recipe> recipelist = new ArrayList<Recipe>();
	   //add recipes from db to arraylist
	   for(Recipe r : recipeRepo.findAll()) {
		   recipelist.add(r);
	   }
	   
	   //sort arraylist on name field
	   Collections.sort(recipelist, new Comparator<Recipe>() {
           @Override
           public int compare(Recipe item1, Recipe item2) {
               String s1 = item1.getName();
               String s2 = item2.getName();
               return s1.compareToIgnoreCase(s2);
           }
       });
	   
	   StringBuilder b = new StringBuilder();
	   b.append("<html><body><table>");
	   b.append("<tr><th>Name</th><th>Ingredients</th><th>CreateDate</th></tr>");
	   
	   for(Recipe recipe : recipelist) {
		   b.append("<tr>");
		       b.append("<td>");
	   			   b.append(recipe.getName());
	   		   b.append("</td>");
	   		   b.append("<td>");
	   			   b.append(recipe.getIngredients());
	   		   b.append("</td>");
	   		   b.append("<td>");
	   			   b.append(recipe.getCreateDate());
	   		   b.append("</td>");
	      b.append("</tr>");
       }
       b.append("</table></body></html>");
  
       return b.toString();
   }
   
   @PostMapping("/new")
   @ResponseBody
   public void saveRecipe(
		   @RequestParam("name") String name,
		   @RequestParam("ingredients") String ingredients
		   ) {
	   //create recipe object with parameters
	   Recipe recipe = new Recipe();
	   recipe.setName(name);
	   recipe.setIngredients(ingredients);
	   
	   //create date object with current time
	   DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	   Date date = new Date();
	   
	   recipe.setCreateDate(date);
	   
	   Boolean contains = false;	   
	   
	   //check for recipe with same ingredients
	   for(Recipe r : recipeRepo.findAll()) {
		   if(r.getIngredients().equals(recipe.getIngredients())) {
			   contains = true;
		   }
	   }
	   
	   //save if no recipe in db contains the ingredients of the new recipe
	   if(!contains) {
		   recipeRepo.save(recipe);
	   }	
   }
}
