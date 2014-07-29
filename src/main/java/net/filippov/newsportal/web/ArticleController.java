package net.filippov.newsportal.web;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import net.filippov.newsportal.domain.Article;
import net.filippov.newsportal.domain.Tag;
import net.filippov.newsportal.service.ArticleService;
import net.filippov.newsportal.service.CategoryService;
import net.filippov.newsportal.service.TagService;
import net.filippov.newsportal.web.constants.URL;
import net.filippov.newsportal.web.constants.View;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
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
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for article-related actions
 * 
 * @author Oleg Filippov
 */
@Controller
@RequestMapping("/article")
@SessionAttributes("article")
public class ArticleController {
	
	private ApplicationContext context;
	private ArticleService articleService;
	private CategoryService categoryService;
	private TagService tagService;
	
	/**
	 * Default constructor
	 */
	public ArticleController() {}
	
	/**
	 * Constructor autowiring needed services
	 */
	@Autowired
	public ArticleController(ArticleService articleService,
			CategoryService categoryService,
			TagService tagService,
			ApplicationContext context) {
		this.context = context;
		this.articleService = articleService;
		this.categoryService = categoryService;
		this.tagService = tagService;
	}
	
	/**
	 * Tags Autocomplete
	 * 
	 * @return tags-json
	 */
	@RequestMapping(method = RequestMethod.GET, value = URL.TAGS_AUTOCOMPLETE)
	@ResponseBody
	public String tagsAutocomplete() {
		
		return tagService.getAutocompleteJson();
	}
	
	/**
	 * Current article view-page
	 */
	@RequestMapping(method = RequestMethod.GET, value = URL.SHOW_ARTICLE)
	public String viewArticlePage(Model model, @PathVariable("id") Long articleId,
			HttpSession session, HttpServletRequest request, HttpServletResponse resp) {
		
		boolean needIncreaseViewCount = request.getHeader("Referer") != null
				&& !request.getHeader("Referer").equals(request.getRequestURL().toString());
		Long loggedUserId  = (Long) session.getAttribute("loggedUserId"); // null for unauthorized
		
		Article currentArticle = articleService.get(
				articleId, loggedUserId, needIncreaseViewCount);
		
		model.addAttribute("article", currentArticle);

		return View.VIEW_ARTICLE;
	}
	
	/**
	 * Add comment submit
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(method = RequestMethod.POST, value = URL.ADD_COMMENT)
	@ResponseBody
	public String addCommentSubmit(HttpSession session,
			@RequestParam(value = "articleId") Long articleId,
			@RequestParam(value = "content", defaultValue = "") String content) {
		
		if (content.isEmpty()) {
			return context.getMessage("validation.comment.content", null,
					LocaleContextHolder.getLocale());
		}
		Long authorId  = (Long) session.getAttribute("loggedUserId");
		articleService.addComment(content, authorId, articleId);
		
		return "ok";
	}
	
	/**
	 * Page of article to be added
	 */
	@PreAuthorize("hasRole('ROLE_AUTHOR')")
	@RequestMapping(method = RequestMethod.GET, value = URL.ADD_ARTICLE)
	public String addArticlePage(Model model) {
		
		// clear article-sessionAttribute
		model.addAttribute("article", populateArticle());
		
		return View.EDIT_ARTICLE;
	}
	
	/**
	 * Add article submit
	 */
	@PreAuthorize("hasRole('ROLE_AUTHOR')")
	@RequestMapping(method = RequestMethod.POST, value = URL.ADD_ARTICLE)
	public String addArticleSubmit(Model model, @Valid @ModelAttribute("article") Article article,
			BindingResult result, HttpSession session, SessionStatus status,
			@RequestParam(value = "categoryName", defaultValue = "") String categoryName,
			@RequestParam(value = "tagString", defaultValue = "") String tagString) {

		if (result.hasErrors()) {
			model.addAttribute("tagString", tagString);
			return View.EDIT_ARTICLE;
		}
		
		Long authorId  = (Long) session.getAttribute("loggedUserId");
		articleService.add(article, authorId, categoryName, tagString);
		status.setComplete();
		updateSessionAttributes(session);
		
		return "redirect:/article/" + article.getId();
	}
	
	/**
	 * Page of article to be edited
	 */
	@PreAuthorize("hasRole('ROLE_AUTHOR')")
	@RequestMapping(method = RequestMethod.GET, value = URL.EDIT_ARTICLE)
	public String editArticlePage(Model model, @PathVariable("id") Long articleId,
			HttpSession session, HttpServletRequest request) {
		
		Long loggedUserId  = (Long) session.getAttribute("loggedUserId");
		Article articleToEdit = articleService.getTransactionally(articleId);

		if (loggedUserId != articleToEdit.getAuthor().getId()
				&& !request.isUserInRole("ROLE_ADMIN")) {
			return "redirect:/article/" + articleId;
		}
		
		Set<Tag> tags = articleToEdit.getTags();
		if (tags != null && !tags.isEmpty()) {
			String tagString = tagService.getTagString(tags);
			model.addAttribute("tagString", tagString);
		}
		model.addAttribute("category", articleToEdit.getCategory().getName());
		model.addAttribute("article", articleToEdit);

		return View.EDIT_ARTICLE;
	}
	
	/**
	 * Edit article submit
	 */
	@PreAuthorize("hasRole('ROLE_AUTHOR')")
	@RequestMapping(method = RequestMethod.POST, value = URL.EDIT_ARTICLE)
	public String editArticleSubmit(Model model, @Valid @ModelAttribute("article") Article article,
			BindingResult result, HttpSession session, SessionStatus status,
			@RequestParam(value = "categoryName", defaultValue = "") String categoryName,
			@RequestParam(value = "tagString", defaultValue = "") String tagString) {

		if (result.hasErrors()) {
			model.addAttribute("tagString", tagString);
			return View.EDIT_ARTICLE;
		}
		
		articleService.update(article, categoryName, tagString);
		status.setComplete();
		updateSessionAttributes(session);

		return "redirect:/article/" + article.getId();
	}
	
	/**
	 * Delete article
	 */
	@PreAuthorize("hasRole('ROLE_AUTHOR')")
	@RequestMapping(method = RequestMethod.GET, value = URL.DELETE_ARTICLE)
	public ModelAndView deleteArticle(@PathVariable("id") Long id,
			HttpSession session, SessionStatus status, HttpServletRequest request) {

		articleService.deleteByIdTransactionally(id);
		status.setComplete();
		updateSessionAttributes(session);

		return new ModelAndView(View.SUCCESS)
				.addObject("messageProperty", "success.article.deleted")
				.addObject("url", request.getServletContext().getContextPath());
	}
	
    /**
     * Cancel article edit
     */
    @RequestMapping(method=RequestMethod.GET, value = URL.CANCEL)
    public String cancelArticleEdit(@PathVariable("id") Long id,
    		SessionStatus status) {

    	status.setComplete();
    	if (id == 0)
    		return "redirect:" + URL.HOME;
    	
    	return "redirect:/article/" + id;
    }
    
    /**
     * Fill model attribute
     * 
     * @return new instance of {@link Article}
     */
    @ModelAttribute("article")
	public Article populateArticle() {
		return new Article();
	}
	
	/**
	 * Update session attributes after add or edit article
	 * 
	 * @param session
	 */
	private void updateSessionAttributes(HttpSession session) {
		session.setAttribute("categories", categoryService.getAllTransactionally());
		session.setAttribute("tags", tagService.getAllTransactionally());
	}
}
