package net.filippov.newsportal.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import javax.servlet.http.HttpServletResponse;

import net.filippov.newsportal.exception.UnacceptableFileFormatException;
import net.filippov.newsportal.web.constants.URL;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {

	// Constants operating with images
	private static final String ARTICLE_IMAGES_PATH = "c:/Newsportal/article_images/";
	private static final String JPG_CONTENT_TYPE = "image/jpeg";
	private static final String PNG_CONTENT_TYPE = "image/png";

	public FileUploadController() {}

	// Upload image
	@RequestMapping(method = RequestMethod.POST, value = URL.UPLOAD_IMAGE)
	@ResponseBody
	public String uploadimage(@RequestParam("file") MultipartFile image)
			throws IOException {

		String imageName = Calendar.getInstance().getTimeInMillis()
				+ image.getOriginalFilename();

		if (!image.isEmpty()) {
			String imageType = image.getContentType();
			if (!(imageType.equals(JPG_CONTENT_TYPE) || imageType
					.equals(PNG_CONTENT_TYPE))) {

				// TODO: additional validation based on the content of the file

				throw new UnacceptableFileFormatException();
			}

			File file = new File(ARTICLE_IMAGES_PATH + imageName);
			FileUtils.writeByteArrayToFile(file, image.getBytes());
		}

		return "images/" + imageName;
	}

	// Get image
	@RequestMapping(method = RequestMethod.GET, value = URL.SHOW_IMAGE)
	public void showImg(@PathVariable("name") String imageName,
			@PathVariable("type") String type, HttpServletResponse response)
			throws IOException {

		InputStream in = new FileInputStream(ARTICLE_IMAGES_PATH
				+ imageName + "." + type);
		FileCopyUtils.copy(in, response.getOutputStream());
		in.close();
	}
}
