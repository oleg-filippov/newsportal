package net.filippov.newsportal.web;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.filippov.newsportal.domain.User;
import net.filippov.newsportal.exception.NotUniqueUserFieldException;
import net.filippov.newsportal.service.UserService;
import net.filippov.newsportal.web.constants.URL;
import net.filippov.newsportal.web.constants.View;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	public UserController() {}

	// SignIn-page
	@RequestMapping(method = RequestMethod.GET, value = URL.SIGN_IN)
	public void signinPage() {}
	
	// SignIn failure
	@RequestMapping(method = RequestMethod.GET, value = URL.SIGN_IN_FAILURE)
	public String signinFailure(Model model) {

		model.addAttribute("error", "true");

		return View.SIGN_IN;
	}
	
	// SignUp-page
	@RequestMapping(method=RequestMethod.GET, value = URL.SIGN_UP)
	public String signupPage() {
		
		return View.SIGN_UP;
	}
	
	// SignUp submit
	@RequestMapping(method=RequestMethod.POST, value = URL.SIGN_UP)
	public String registerSubmit(Model model, @Valid @ModelAttribute User user,
			BindingResult result, RedirectAttributes attr, HttpServletRequest request) {
		
		if (result.hasErrors()) {
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.user", result);
			attr.addFlashAttribute("user", user);
			return "redirect:" + URL.SIGN_UP;
		}
		
		try {
			userService.add(user);
		} catch (NotUniqueUserFieldException e) {
			String field = e.getMessage();
			result.rejectValue(field, "validation.user.unique." + field);
			user.setPassword("");
			return View.SIGN_UP;
		}
		LOG.info("ADDED: " + user);

		model.addAttribute("messageProperty", "success.registration");
		model.addAttribute("url", request.getServletContext().getContextPath());
		
		return View.SUCCESS;
	}
	
	@RequestMapping(method=RequestMethod.GET, value = URL.CHECK_LOGIN)
	@ResponseBody
	public String checkLogin(@RequestParam String login) {
		
		return userService.getByLogin(login) == null
			? "ok"
			: "no";
	}
	
	@RequestMapping(method=RequestMethod.GET, value = URL.CHECK_EMAIL)
	@ResponseBody
	public String checkEmail(@RequestParam String email) {
		
		return userService.getByEmail(email) == null
			? "ok"
			: "no";
	}
	
	// Profile-page
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(method=RequestMethod.GET, value = URL.USER_PROFILE)
	public String profilePage(Model model, @PathVariable("id") Long userId) {
		
		User user = userService.get(userId);
		model.addAttribute("user", user);
		
		return View.PROFILE;
	}
	
	@ModelAttribute("user")
	public User populateUser() {
		return new User();
	}
}