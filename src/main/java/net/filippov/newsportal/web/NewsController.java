package net.filippov.newsportal.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import net.filippov.newsportal.domain.Comment;
import net.filippov.newsportal.domain.News;
import net.filippov.newsportal.domain.User;
import net.filippov.newsportal.exception.ServiceException;
import net.filippov.newsportal.service.CommentService;
import net.filippov.newsportal.service.NewsService;
import net.filippov.newsportal.service.UserService;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/news")
@SessionAttributes("news")
public class NewsController {
	
	private static final Logger LOG = LoggerFactory.getLogger(NewsController.class);
	
	// URL's
	private final String SHOW_NEWS_URL = "/{id}";
	private final String ADD_COMMENT_URL = "/{id}/addcomment";
	private final String ADD_NEWS_URL = "/add";
	private final String EDIT_NEWS_URL = "/{id}/edit";
	private final String DELETE_NEWS_URL = "/{id}/delete";
	private final String CANCEL_URL = "/cancel";
	private final String UPLOAD_IMAGE_URL = "/uploadimage";
	private final String SHOW_IMAGE_URL = "/images";
	
	// Constants operating with images
	private final String NEWS_IMAGES_PATH = "c:/Newsportal/news_images/";
	private final String JPG_EXTENSION = ".jpg";
	private final String PNG_EXTENSION = ".png";
	private final String JPG_CONTENT_TYPE = "image/jpeg";
	private final String PNG_CONTENT_TYPE = "image/png";

	@Autowired
	private NewsService newsService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private UserService userService;
	
	public NewsController() {}
	
	// Upload image
	@RequestMapping(method = RequestMethod.POST, value = UPLOAD_IMAGE_URL)
	@ResponseBody
	public String uploadimage(@RequestParam("file") MultipartFile image,
			@ModelAttribute("news") News news, BindingResult result) {

		String imageId = String.valueOf(Calendar.getInstance().getTimeInMillis());
		String imagePath = NEWS_IMAGES_PATH + imageId;
		
		if (!image.isEmpty()) {
			String imageType = image.getContentType();
			if (!(imageType.equals("image/jpeg")
					|| imageType.equals("image/png"))
					|| imageType.equals("image/gif")) {
				return "typeError";
			}
            try {
            	File file = new File(imagePath);
        		FileUtils.writeByteArrayToFile(file, image.getBytes());
            } catch (IOException e) {
            	return "ioError";
            } catch (MaxUploadSizeExceededException e) {
            	return "sizeError";
			}
        }
		return "images?id=" + imageId;
	}

	// Get image
	@RequestMapping(method=RequestMethod.GET, value = SHOW_IMAGE_URL)
	public void showImg(HttpServletRequest req, @RequestParam("id") Long imageId,
			HttpServletResponse response) {

		if (imageId != null) {
			InputStream in;
			try {
				in = new FileInputStream(NEWS_IMAGES_PATH + imageId + ".jpg");
				FileCopyUtils.copy(in, response.getOutputStream());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// Current news view-page
	@RequestMapping(method = RequestMethod.GET, value = SHOW_NEWS_URL)
	public String viewNewsPage(Model model, @PathVariable("id") Long newsId,
			SessionStatus status, HttpSession session) {
		
		User loggedUser = (User) session.getAttribute("loggedUser");
		
		try {
			News currentNews = newsService.getById(newsId);
			model.addAttribute("news", currentNews);
			List<Comment> comments = commentService.getAllByNewsId(newsId);
			model.addAttribute("comments", comments);
		
			if (loggedUser == null
					|| loggedUser.getId() != currentNews.getAuthor().getId()) {
				newsService.increaseViewsCountById(newsId);
			}
		} catch (ServiceException e) {
			LOG.error(e.getMessage(), e);
			model.addAttribute("message", e.getMessage());
			
			return "/errors/exception";
		}
		
		status.setComplete();

		return "viewnews";
	}
	
	// Add comment submit
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(method = RequestMethod.POST, value = ADD_COMMENT_URL)
	public String addCommentSubmit(Model model, @ModelAttribute("news") News news,
			@Valid @ModelAttribute("comment") Comment comment, BindingResult result,
			RedirectAttributes attr, SessionStatus status, HttpSession session) {
		
		if (result.hasErrors()) {
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.comment", result);
			attr.addFlashAttribute("comment", comment);
			
			return "redirect:/news/" + news.getId();
		}
		
		User loggedUser = (User) session.getAttribute("loggedUser");
		
		try {
			comment.setAuthor(loggedUser);
			comment.setNews(news);
			commentService.add(comment);
			LOG.info(comment + " has been added successfully");
		} catch (ServiceException e) {
			LOG.error(e.getMessage(), e);
			model.addAttribute("message", e.getMessage());
			
			return "/errors/exception";
		}
		
		status.setComplete();
		
		return "redirect:/news/" + news.getId();
	}

	// Page of news to be added
	@PreAuthorize("hasRole('ROLE_AUTHOR')")
	@RequestMapping(method = RequestMethod.GET, value = ADD_NEWS_URL)
	public String addNewsPage() {
		
		return "editnews";
	}

	// Add news submit
	@PreAuthorize("hasRole('ROLE_AUTHOR')")
	@RequestMapping(method = RequestMethod.POST, value = ADD_NEWS_URL)
	public String addNewsSubmit(Model model, @Valid @ModelAttribute("news") News news,
			BindingResult result, RedirectAttributes attr,
			SessionStatus status, HttpSession session) {

		if (result.hasErrors()) {
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.news", result);
			attr.addFlashAttribute("news", news);
			
			return "redirect:/news/add";
		}
		
		User loggedUser = (User) session.getAttribute("loggedUser");
		
		try {
			news.setAuthor(loggedUser);
			newsService.add(news);
			LOG.info(news + " has been added successfully");
		} catch (ServiceException e) {
			LOG.error(e.getMessage(), e);
			model.addAttribute("message", e.getMessage());
			
			return "/errors/exception";
		}
		
		status.setComplete();

		return "redirect:/news/" + news.getId();
	}
	
	// Page of news to be edited
	@PreAuthorize("hasRole('ROLE_AUTHOR')")
	@RequestMapping(method = RequestMethod.GET, value = EDIT_NEWS_URL)
	public String editNewsPage(Model model, @PathVariable("id") Long newsId,
			HttpSession session) {
		
		User loggedUser = (User) session.getAttribute("loggedUser");
		
		try {
			News newsToEdit = newsService.getById(newsId);
			if (loggedUser.getId() != newsToEdit.getAuthor().getId()) {
				return "redirect:/news/" + newsId;
			}
			model.addAttribute("news", newsToEdit);
		} catch (ServiceException e) {
			LOG.error(e.getMessage(), e);
			model.addAttribute("message", e.getMessage());
			
			return "/errors/exception";
		}
		
		return "editnews";
	}
	
	// Edit news submit
	@PreAuthorize("hasRole('ROLE_AUTHOR')")
	@RequestMapping(method = RequestMethod.POST, value = EDIT_NEWS_URL)
	public String editNewsSubmit(Model model, @Valid @ModelAttribute("news") News news,
			BindingResult result, RedirectAttributes attr,
			SessionStatus status, HttpSession session) {
		
		if (result.hasErrors()) {
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.news", result);
			attr.addFlashAttribute("news", news);
			
			return "redirect:/news/" + news.getId() + "/edit";
		}

		User loggedUser = (User) session.getAttribute("loggedUser");
		
		try {
			news.setAuthor(loggedUser);
			news.setLastModified(new Date());
			newsService.update(news);
			LOG.info(news + " has been updated successfully");
		} catch (ServiceException e) {
			LOG.error(e.getMessage(), e);
			model.addAttribute("message", e.getMessage());
			
			return "/errors/exception";
		}
		
		status.setComplete();

		return "redirect:/news/" + news.getId();
	}
	
	// Delete news
	@PreAuthorize("hasRole('ROLE_AUTHOR')")
	@RequestMapping(method = RequestMethod.GET, value = DELETE_NEWS_URL)
	public String deleteNews(Model model, @PathVariable("id") Long id,
			SessionStatus status) {

		try {
			newsService.deleteById(id);
		} catch (ServiceException e) {
			LOG.error(e.getMessage(), e);
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
