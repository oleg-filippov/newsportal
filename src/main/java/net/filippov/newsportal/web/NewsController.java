package net.filippov.newsportal.web;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import net.filippov.newsportal.domain.Comment;
import net.filippov.newsportal.domain.News;
import net.filippov.newsportal.domain.User;
import net.filippov.newsportal.exception.ServiceException;
import net.filippov.newsportal.service.CommentService;
import net.filippov.newsportal.service.NewsService;
import net.filippov.newsportal.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/news")
@SessionAttributes("news")
public class NewsController {
	
	private static final Logger LOG = LoggerFactory.getLogger(NewsController.class);
	
	private final String SHOW_NEWS_URL = "/{id}";
	private final String ADD_COMMENT_URL = "/{id}/addcomment";
	private final String ADD_NEWS_URL = "/add";
	private final String EDIT_NEWS_URL = "/{id}/edit";
	private final String DELETE_NEWS_URL = "/{id}/delete";
	private final String CANCEL_URL = "/cancel";

	@Autowired
	private NewsService newsService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private UserService userService;
	
	public NewsController() {}

	// Current news view-page
	@RequestMapping(method = RequestMethod.GET, value = SHOW_NEWS_URL)
	public String viewNewsPage(Model model, @PathVariable("id") Long newsId) {
		
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		
		try {
			News currentNews = newsService.getById(newsId);
			model.addAttribute("news", currentNews);
			List<Comment> comments = commentService.getAllByNewsId(newsId);
			model.addAttribute("comments", comments);
		
			if ("anonymousUser".equals(login)) {
				newsService.increaseViewsCountById(newsId);
			} else {
				User user = userService.getByLogin(login);
				if (user.getId() != currentNews.getAuthor().getId()) {
					newsService.increaseViewsCountById(newsId);
				}
			}
		} catch (ServiceException e) {
			LOG.error("Failed to show viewnewsPage", e);
			model.addAttribute("message", e.getMessage());
			
			return "/errors/exception";
		}
		
		return "viewnews";
	}
	
	// Add comment submit
	@RequestMapping(method = RequestMethod.POST, value = ADD_COMMENT_URL)
	public String addCommentSubmit(Model model, @ModelAttribute("news") News news,
			@Valid @ModelAttribute("comment") Comment comment,
			BindingResult result, RedirectAttributes attr, SessionStatus status) {
		
		if (result.hasErrors()) {
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.comment", result);
			attr.addFlashAttribute("comment", comment);
			
			return "redirect:/news/" + news.getId();
		}
		
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		
		try {
			User user = userService.getByLogin(login);

			comment.setAuthor(user);
			comment.setNews(news);
			commentService.add(comment);
			LOG.info(comment + " has been added successfully");
		} catch (ServiceException e) {
			LOG.error("Failed to add comment", e);
			model.addAttribute("message", e.getMessage());
			
			return "/errors/exception";
		}
		
		status.setComplete();
		
		return "redirect:/news/" + news.getId();
	}

	// Page of news to be added
	@PreAuthorize("hasAnyRole('ROLE_AUTHOR', 'ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.GET, value = ADD_NEWS_URL)
	public String addNewsPage() {
		
		return "editnews";
	}

	// Add news submit
	@PreAuthorize("hasAnyRole('ROLE_AUTHOR', 'ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.POST, value = ADD_NEWS_URL)
	public String addNewsSubmit(Model model, @Valid @ModelAttribute("news") News news,
			BindingResult result, RedirectAttributes attr, SessionStatus status) {

		if (result.hasErrors()) {
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.news", result);
			attr.addFlashAttribute("news", news);
			
			return "redirect:/news/add";
		}
		
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		
		try {
			User user = userService.getByLogin(login);
			news.setAuthor(user);
			newsService.add(news);
			LOG.info(news + " has been added successfully");
		} catch (ServiceException e) {
			LOG.error("Failed to add news", e);
			model.addAttribute("message", e.getMessage());
			
			return "/errors/exception";
		}
		
		status.setComplete();

		return "redirect:/news/" + news.getId();
	}
	
	// Page of news to be edited
	@PreAuthorize("hasAnyRole('ROLE_AUTHOR', 'ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.GET, value = EDIT_NEWS_URL)
	public String editNewsPage(Model model, @PathVariable("id") Long newsId) {
		
		try {
			News newsToEdit = newsService.getById(newsId);
			String login = SecurityContextHolder.getContext().getAuthentication().getName();
			
			if (!login.equals(newsToEdit.getAuthor().getLogin())) {
				return "redirect:/news/" + newsId;
			}
			
			model.addAttribute("news", newsToEdit);
		} catch (ServiceException e) {
			LOG.error("Failed to show editnews-page", e);
			model.addAttribute("message", e.getMessage());
			
			return "/errors/exception";
		}
		
		return "editnews";
	}
	
	// Edit news submit
	@PreAuthorize("hasAnyRole('ROLE_AUTHOR', 'ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.POST, value = EDIT_NEWS_URL)
	public String editNewsSubmit(Model model, @Valid @ModelAttribute("news") News news,
			BindingResult result,RedirectAttributes attr, SessionStatus status) {
		
		if (result.hasErrors()) {
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.news", result);
			attr.addFlashAttribute("news", news);
			
			return "redirect:/news/" + news.getId() + "/edit";
		}

		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		
		try {
			User user = userService.getByLogin(login);
			news.setAuthor(user);
			news.setLastModified(new Date());
			newsService.update(news);
			LOG.info(news + " has been updated successfully");
		} catch (ServiceException e) {
			LOG.error("Failed to update news", e);
			model.addAttribute("message", e.getMessage());
			
			return "/errors/exception";
		}
		
		status.setComplete();

		return "redirect:/news/" + news.getId();
	}
	
	// Delete news
	@PreAuthorize("hasAnyRole('ROLE_AUTHOR', 'ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.GET, value = DELETE_NEWS_URL)
	public String deleteNews(Model model,
			@PathVariable("id") Long id,
			SessionStatus status) {

		try {
			newsService.deleteById(id);
		} catch (ServiceException e) {
			LOG.error("Failed to delete news", e);
			model.addAttribute("message", e.getMessage());
			
			return "/errors/exception";
		}
		
		status.setComplete();
		
		return "redirect:/";
	}
	
	// Cancel news edit
    @RequestMapping(method=RequestMethod.GET, value = CANCEL_URL)
    public String cancelNewsEdit(@ModelAttribute("news") News news,
    		SessionStatus status) {

    	status.setComplete();
    	
    	if (news.getId() == null) {
    		return "redirect:/";
    	} else {
    		return "redirect:/news/" + news.getId();
    	}
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
