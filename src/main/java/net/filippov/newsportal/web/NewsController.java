package net.filippov.newsportal.web;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import net.filippov.newsportal.domain.Comment;
import net.filippov.newsportal.domain.News;
import net.filippov.newsportal.domain.Tag;
import net.filippov.newsportal.service.NewsService;
import net.filippov.newsportal.service.TagService;

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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/news")
@SessionAttributes("news")
public class NewsController {
	
	private static final Logger LOG = LoggerFactory.getLogger(NewsController.class);
	
	// URL's
	private static final String SHOW_NEWS_URL = "/{id}";
	private static final String ADD_COMMENT_URL = "/{id}/addcomment";
	private static final String ADD_NEWS_URL = "/add";
	private static final String EDIT_NEWS_URL = "/{id}/edit";
	private static final String DELETE_NEWS_URL = "/{id}/delete";
	private static final String CANCEL_URL = "/{id}/cancel";
	private static final String TAGS_AUTOCOMPLETE_URL = "/tags/autocomplete";
	
	@Autowired
	private NewsService newsService;
	
	@Autowired
	private TagService tagService;
	
	public NewsController() {}
	
	// Tags Autocomplete
	@RequestMapping(method = RequestMethod.GET, value = TAGS_AUTOCOMPLETE_URL)
	@ResponseBody
	public String viewNewsPage() {
		String r = tagService.getAutocompleteJson();
		System.out.println("** r:" + r);
		return r;
	}
	
	// Current news view-page
	@RequestMapping(method = RequestMethod.GET, value = SHOW_NEWS_URL)
	public String viewNewsPage(Model model, @PathVariable("id") Long newsId,
			HttpSession session) {
		
		Long loggedUserId  = (Long) session.getAttribute("loggedUserId");	// null for unauthorized
		News currentNews = newsService.get(newsId, loggedUserId);
		if (currentNews == null) {
			return "/error";
		}
		
		model.addAttribute("news", currentNews);
		model.addAttribute("comment", new Comment());

		return "viewnews";
	}
	
	// Add comment submit
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(method = RequestMethod.POST, value = ADD_COMMENT_URL)
	public String addCommentSubmit(Model model, @ModelAttribute("news") News news,
			@Valid @ModelAttribute("comment") Comment comment, BindingResult result,
			HttpSession session) {

		if (result.hasErrors()) {
			return "viewnews";
		}
		
		Long authorId  = (Long) session.getAttribute("loggedUserId");
		newsService.addComment(comment, authorId, news);
		LOG.info("ADDED: " + comment);
		
		return "redirect:/news/" + news.getId();
	}
	
	// Page of news to be added
	@PreAuthorize("hasRole('ROLE_AUTHOR')")
	@RequestMapping(method = RequestMethod.GET, value = ADD_NEWS_URL)
	public String addNewsPage(Model model) {
		
		model.addAttribute("news", new News());
		
		return "editnews";
	}

	// Add news submit
	@PreAuthorize("hasRole('ROLE_AUTHOR')")
	@RequestMapping(method = RequestMethod.POST, value = ADD_NEWS_URL)
	public String addNewsSubmit(Model model, @Valid @ModelAttribute("news") News news,
			BindingResult result, HttpSession session, SessionStatus status,
			@RequestParam(value = "categoryName", defaultValue = "") String categoryName,
			@RequestParam(value = "tagString", defaultValue = "") String tagString) {

		if (result.hasErrors()) {
			model.addAttribute("tagString", tagString);
			return "editnews";
		}
		
		Long authorId  = (Long) session.getAttribute("loggedUserId");
		newsService.add(news, authorId, categoryName, tagString);
		status.setComplete();
		LOG.info("ADDED: " + news);
		
		return "redirect:/news/" + news.getId();
	}
	
	// Page of news to be edited
	@PreAuthorize("hasRole('ROLE_AUTHOR')")
	@RequestMapping(method = RequestMethod.GET, value = EDIT_NEWS_URL)
	public String editNewsPage(Model model, @PathVariable("id") Long newsId,
			HttpSession session, HttpServletRequest request) {
		
		Long loggedUserId  = (Long) session.getAttribute("loggedUserId");
		News newsToEdit = newsService.getTransactionally(newsId);

		if (loggedUserId != newsToEdit.getAuthor().getId()
				&& !request.isUserInRole("ROLE_ADMIN")) {
			return "redirect:/news/" + newsId;
		}
		
		Set<Tag> tags = newsToEdit.getTags();
		if (tags != null && !tags.isEmpty()) {
			String tagString = tagService.getTagString(tags);
			model.addAttribute("tagString", tagString);
		}
		model.addAttribute("news", newsToEdit);

		return "editnews";
	}
	
	// Edit news submit
	@PreAuthorize("hasRole('ROLE_AUTHOR')")
	@RequestMapping(method = RequestMethod.POST, value = EDIT_NEWS_URL)
	public String editNewsSubmit(Model model, @Valid @ModelAttribute("news") News news,
			BindingResult result, SessionStatus status,
			@RequestParam(value = "categoryName", defaultValue = "") String categoryName,
			@RequestParam(value = "tagString", defaultValue = "") String tagString) {

		if (result.hasErrors()) {
			model.addAttribute("tagString", tagString);
			return "editnews";
		}
		
		newsService.update(news, categoryName, tagString);
		status.setComplete();
		LOG.info("UPDATED: " + news);

		return "redirect:/news/" + news.getId();
	}
	
	// Delete news
	@PreAuthorize("hasRole('ROLE_AUTHOR')")
	@RequestMapping(method = RequestMethod.GET, value = DELETE_NEWS_URL)
	public String deleteNews(Model model, @PathVariable("id") Long id,
			SessionStatus status, HttpServletRequest request) {

		newsService.deleteByIdTransactionally(id);
		status.setComplete();
		LOG.info("DELETED: " + "News[id=" + id + "]");
		
		model.addAttribute("messageProperty", "success.news.deleted");
		model.addAttribute("url", request.getServletContext().getContextPath());
		
		return "success";
	}
	
	// Cancel news edit
    @RequestMapping(method=RequestMethod.GET, value = CANCEL_URL)
    public String cancelNewsEdit(@PathVariable("id") Long id,
    		SessionStatus status) {

    	status.setComplete();
    	if (id == 0)
    		return "redirect:/";
    	
    	return "redirect:/news/" + id;
    }
}
