package net.filippov.newsportal.web;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import net.filippov.newsportal.domain.Article;
import net.filippov.newsportal.domain.Comment;
import net.filippov.newsportal.domain.Tag;
import net.filippov.newsportal.service.ArticleService;
import net.filippov.newsportal.service.CategoryService;
import net.filippov.newsportal.service.TagService;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/article")
@SessionAttributes("article")
public class ArticleController {
	
	private static final Logger LOG = LoggerFactory.getLogger(ArticleController.class);
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private TagService tagService;
	
	public ArticleController() {}
	
	// Tags Autocomplete
	@RequestMapping(method = RequestMethod.GET, value = URL.TAGS_AUTOCOMPLETE)
	@ResponseBody
	public String tagsAutocomplete() {
		
		return tagService.getAutocompleteJson();
	}
	
	// Current article view-page
	@RequestMapping(method = RequestMethod.GET, value = URL.SHOW_ARTICLE)
	public String viewArticlePage(Model model, @PathVariable("id") Long articleId,
			HttpSession session, HttpServletRequest request, HttpServletResponse resp) {
		
		boolean needIncreaseViewCount = request.getHeader("Referer") != null
				&& !request.getRequestURL().toString().equals(request.getHeader("Referer"));
		Long loggedUserId  = (Long) session.getAttribute("loggedUserId"); // null for unauthorized
		
		Article currentArticle = articleService.get(
				articleId, loggedUserId, needIncreaseViewCount);
		
		model.addAttribute("article", currentArticle);

		return View.VIEW_ARTICLE;
	}
	
	// Add comment submit
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(method = RequestMethod.POST, value = URL.ADD_COMMENT)
	public String addCommentSubmit(Model model, @ModelAttribute("article") Article article,
			@Valid @ModelAttribute("comment") Comment comment, BindingResult result,
			RedirectAttributes attr, HttpSession session) {

		if (result.hasErrors()) {
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.comment", result);
			attr.addFlashAttribute("comment", comment);
			return "redirect:/article/" + article.getId();
		}
		
		Long authorId  = (Long) session.getAttribute("loggedUserId");
		articleService.addComment(comment, authorId, article);
		LOG.info("ADDED: " + comment);
		
		return "redirect:/article/" + article.getId();
	}
	
	// Page of article to be added
	@PreAuthorize("hasRole('ROLE_AUTHOR')")
	@RequestMapping(method = RequestMethod.GET, value = URL.ADD_ARTICLE)
	public String addArticlePage(Model model) {
		
		// clear article-modelAttribute
		model.addAttribute("article", populateArticle());
		
		return View.EDIT_ARTICLE;
	}

	// Add article submit
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
		LOG.info("ADDED: " + article);
		
		return "redirect:/article/" + article.getId();
	}
	
	// Page of article to be edited
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
		model.addAttribute("article", articleToEdit);

		return View.EDIT_ARTICLE;
	}
	
	// Edit article submit
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
		LOG.info("UPDATED: " + article);

		return "redirect:/article/" + article.getId();
	}
	
	// Delete article
	@PreAuthorize("hasRole('ROLE_AUTHOR')")
	@RequestMapping(method = RequestMethod.GET, value = URL.DELETE_ARTICLE)
	public String deleteArticle(Model model, @PathVariable("id") Long id,
			HttpSession session, SessionStatus status, HttpServletRequest request) {

		articleService.deleteByIdTransactionally(id);
		status.setComplete();
		updateSessionAttributes(session);
		LOG.info(String.format("DELETED: Article[id=%d]", id));
		
		model.addAttribute("messageProperty", "success.article.deleted");
		model.addAttribute("url", request.getServletContext().getContextPath());
		
		return View.SUCCESS;
	}
	
	// Cancel article edit
    @RequestMapping(method=RequestMethod.GET, value = URL.CANCEL)
    public String cancelArticleEdit(@PathVariable("id") Long id,
    		SessionStatus status) {

    	status.setComplete();
    	if (id == 0)
    		return "redirect:" + URL.HOME;
    	
    	return "redirect:/article/" + id;
    }
    
    @ModelAttribute("article")
	public Article populateArticle() {
		return new Article();
	}
    
	@ModelAttribute("comment")
	public Comment populateComment() {
		return new Comment();
	}
	
	private void updateSessionAttributes(HttpSession session) {
		session.setAttribute("categories", categoryService.getAllTransactionally());
		session.setAttribute("tags", tagService.getAllTransactionally());
	}
}
