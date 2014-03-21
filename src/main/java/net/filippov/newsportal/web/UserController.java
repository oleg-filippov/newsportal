package net.filippov.newsportal.web;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
	
	// URL's
	private final String SIGNIN_URL = "/signin";
	private final String SIGNIN_FAILURE_URL = "/signin/failure";
	private final String REGISTER_URL = "/register";
	private final String USER_PROFILE_URL = "/user/{id}";
	
	@Autowired
	private UserService userService;
	
	public UserController() {}

	// SignIn-page
	@RequestMapping(method = RequestMethod.GET, value = SIGNIN_URL)
	public void signin() {}
	
	// SignIn failure
	@RequestMapping(method = RequestMethod.GET, value = SIGNIN_FAILURE_URL)
	public String signinFailure(Model model) {
		
		model.addAttribute("messageProperty", "error.signIn");
		return "/errors/error";
	}
	
	// Register-page
	@RequestMapping(method=RequestMethod.GET, value = REGISTER_URL)
	public String registerPage() {
		
		return "register";
	}
	
	// Register submit
	@RequestMapping(method=RequestMethod.POST, value = REGISTER_URL)
	public String registerSubmit(Model model, @Valid @ModelAttribute User user,
			BindingResult result, RedirectAttributes attr, HttpServletRequest request) {
		
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
			user.setPassword(null);
			return "register";
		} catch (NotUniqueUserEmailException e) {
			result.rejectValue("email", "validation.user.emailUnique");
			return "register";
		} catch (ServiceException e) {
			LOG.error(e.getMessage(), e);
			model.addAttribute("message", e.getMessage());
			
			return "/errors/exception";
		}
		
		model.addAttribute("messageProperty", "success.registration");
		model.addAttribute("url", request.getServletContext().getContextPath());
		return "success";
	}
	
	// Profile-page
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(method=RequestMethod.GET, value = USER_PROFILE_URL)
	public String profilePage(Model model, @PathVariable("id") Long userId) {
		
		try {
			User user = userService.getById(userId);
			model.addAttribute("user", user);
		} catch (ServiceException e) {
			LOG.error(e.getMessage(), e);
			model.addAttribute("message", e.getMessage());
			
			return "/errors/exception";
		}
		
		return "profile";
	}
	
	@ModelAttribute("user")
	public User populateUser() {
		return new User();
	}
}
