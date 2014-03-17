package net.filippov.newsportal.web;

import javax.validation.Valid;

import net.filippov.newsportal.domain.User;
import net.filippov.newsportal.exception.NotUniqueUserEmailException;
import net.filippov.newsportal.exception.NotUniqueUserLoginException;
import net.filippov.newsportal.exception.ServiceException;
import net.filippov.newsportal.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
	
	private final String SIGNIN_URL = "/signin";
	private final String REGISTER_URL = "/register";
	
	@Autowired
	private UserService userService;
	
	public UserController() {}

	// SignIn-page
	@RequestMapping(method = RequestMethod.GET, value = SIGNIN_URL)
	public void signin() {}
	
	// Register-page
	@RequestMapping(method=RequestMethod.GET, value = REGISTER_URL)
	public String registerPage() {
		
		return "register";
	}
	
	// Register submit
	@RequestMapping(method=RequestMethod.POST, value = REGISTER_URL)
	public String registerSubmit(@Valid @ModelAttribute User user, BindingResult result,
			RedirectAttributes attr) {
		
		if (result.hasErrors()) {
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.user", result);
			attr.addFlashAttribute("user", user);
			return "redirect:/register";
		}
		
		try {
			userService.add(user);
			LOG.info(user + " has been added successfully");
		} catch (NotUniqueUserLoginException e) {
			result.rejectValue("login", "validation.user.loginUnique");
			return "register";
		} catch (NotUniqueUserEmailException e) {
			result.rejectValue("email", "validation.user.emailUnique");
			return "register";
		} catch (ServiceException e) {
			LOG.error("Failed to add new user", e);
		}
		
		return "redirect:/";
	}
	
	// Profile-page
	@PreAuthorize("hasAnyRole('ROLE_AUTHOR', 'ROLE_ADMIN')")
	@RequestMapping(method=RequestMethod.GET, value = { "/profile" })
	public String profilePage(Model model) {
		
		return "profile";
	}
	
	@ModelAttribute("user")
	public User populateUser() {
		return new User();
	}
}
