package net.filippov.newsportal.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import net.filippov.newsportal.domain.Comment;
import net.filippov.newsportal.domain.News;
import net.filippov.newsportal.domain.User;
import net.filippov.newsportal.domain.UserRole;
import net.filippov.newsportal.service.CommentService;
import net.filippov.newsportal.service.NewsService;

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
@RequestMapping("/news")
public class NewsController {
	
	private static final Logger LOG = LoggerFactory.getLogger(NewsController.class);
	
	// URL's
	private static final String SHOW_NEWS_URL = "/{id}";
	private static final String ADD_COMMENT_URL = "/{id}/addcomment";
	private static final String ADD_NEWS_URL = "/add";
	private static final String EDIT_NEWS_URL = "/{id}/edit";
	private static final String DELETE_NEWS_URL = "/{id}/delete";
	private static final String CANCEL_URL = "/{id}/cancel";
	
	@Autowired
	private NewsService newsService;

	@Autowired
	private CommentService commentService;
	
	public NewsController() {}
	
	// Current news view-page
	@RequestMapping(method = RequestMethod.GET, value = SHOW_NEWS_URL)
	public String viewNewsPage(Model model, @PathVariable("id") Long newsId,
			HttpSession session) {

		User loggedUser = (User) session.getAttribute("loggedUser");
		
		News currentNews = newsService.getById(newsId);
		if (currentNews == null) {
			model.addAttribute("message", "NO NEWS");
			return "/error";
		}
		model.addAttribute("news", currentNews);
		List<Comment> comments = commentService.getAllByNewsId(newsId);
		model.addAttribute("comments", comments);
		
		if (loggedUser == null
				|| loggedUser.getId() != currentNews.getAuthor().getId()) {
			newsService.increaseViewsCountById(newsId);
		}

		return "viewnews";
	}
	
	// Add comment submit
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(method = RequestMethod.POST, value = ADD_COMMENT_URL)
	public String addCommentSubmit(Model model, @ModelAttribute("news") News news,
			@Valid @ModelAttribute("comment") Comment comment, BindingResult result,
			RedirectAttributes attr, HttpSession session) {
		
		if (result.hasErrors()) {
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.comment", result);
			attr.addFlashAttribute("comment", comment);
			
			return "redirect:/news/" + news.getId();
		}
		
		User loggedUser = (User) session.getAttribute("loggedUser");
		
		comment.setAuthor(loggedUser);
		comment.setNews(news);
		commentService.add(comment);
		LOG.info(comment + " has been added successfully");
		
		return "redirect:/news/" + news.getId();
	}

	// Page of news to be added
	@PreAuthorize("hasRole('ROLE_AUTHOR')")
	@RequestMapping(method = RequestMethod.GET, value = ADD_NEWS_URL)
	public String addNewsPage(HttpServletResponse r) {
		if (true) {
		

		}
		return "editnews";
	}

	// Add news submit
	@PreAuthorize("hasRole('ROLE_AUTHOR')")
	@RequestMapping(method = RequestMethod.POST, value = ADD_NEWS_URL)
	public String addNewsSubmit(Model model, @Valid @ModelAttribute("news") News news,
			BindingResult result, RedirectAttributes attr, HttpSession session) {

		if (result.hasErrors()) {
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.news", result);
			attr.addFlashAttribute("news", news);
			
			return "redirect:/news/add";
		}
		
		User loggedUser = (User) session.getAttribute("loggedUser");
		
		news.setAuthor(loggedUser);
		newsService.add(news);
		LOG.info(news + " has been added successfully");

		return "redirect:/news/" + news.getId();
	}
	
	// Page of news to be edited
	@PreAuthorize("hasRole('ROLE_AUTHOR')")
	@RequestMapping(method = RequestMethod.GET, value = EDIT_NEWS_URL)
	public String editNewsPage(Model model, @PathVariable("id") Long newsId,
			HttpSession session) {

		User loggedUser = (User) session.getAttribute("loggedUser");

		News newsToEdit = newsService.getById(newsId);
		UserRole roleAdmin = new UserRole();
		roleAdmin.setId((long) 1);


		if (loggedUser.getId() != newsToEdit.getAuthor().getId()
				&& !loggedUser.getRoles().contains(roleAdmin)) {
			return "redirect:/news/" + newsId;
		}
		model.addAttribute("news", newsToEdit);

		return "editnews";
	}
	
	// Edit news submit
	@PreAuthorize("hasRole('ROLE_AUTHOR')")
	@RequestMapping(method = RequestMethod.POST, value = EDIT_NEWS_URL)
	public String editNewsSubmit(Model model, @Valid @ModelAttribute("news") News news,
			BindingResult result, RedirectAttributes attr, HttpSession session) {
		
		if (result.hasErrors()) {
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.news", result);
			attr.addFlashAttribute("news", news);
			
			return "redirect:/news/" + news.getId() + "/edit";
		}

		User loggedUser = (User) session.getAttribute("loggedUser");
		
		news.setAuthor(loggedUser);
		news.setLastModified(new Date());
		newsService.update(news);
		LOG.info(news + " has been updated successfully");

		return "redirect:/news/" + news.getId();
	}
	
	// Delete news
	@PreAuthorize("hasRole('ROLE_AUTHOR')")
	@RequestMapping(method = RequestMethod.GET, value = DELETE_NEWS_URL)
	public String deleteNews(Model model, @PathVariable("id") Long id) {

		newsService.deleteById(id);
		LOG.info("News[id=" + id + "] has been deleted successfully");
		
		return "redirect:/";
	}
	
	// Cancel news edit
    @RequestMapping(method=RequestMethod.GET, value = CANCEL_URL)
    public String cancelNewsEdit(@PathVariable("id") Long id) {

    	if (id == 0) {
    		return "redirect:/";
    	}
    	
    	return "redirect:/news/" + id;
    }

	@ModelAttribute("news")
	public News populateNews() {
		return new News();
	}

	@ModelAttribute("comment")
	public Comment populateComment() {
		return new Comment();
	}
}
