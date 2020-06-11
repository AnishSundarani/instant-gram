package com.Instantgram.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Instantgram.model.Post;
import com.Instantgram.model.User;
import com.Instantgram.repository.PostRepository;
import com.Instantgram.service.AmazonClient;
import com.Instantgram.service.StorageService;
import com.Instantgram.service.UserService;

@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "*")
public class PostController {
	
	private AmazonClient amazonClient;

    @Autowired
    PostController(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }
	
	@Autowired
	UserService userService;

	@Autowired
	PostRepository postRepository;

	@Autowired
	StorageService storageService;

	@PostMapping("/")
	@ResponseBody
	public String upload(@RequestParam("file") MultipartFile file, Authentication authentication) throws Exception {
		System.out.println("uploaded");
		String uri = this.amazonClient.uploadFile(file);

		String username = authentication.getName();

		User user = userService.findByUsername(username);

		Post post = new Post(uri, user);

		postRepository.save(post);

		return uri;
	}
	
	
	@GetMapping("/pictures/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws FileNotFoundException {

		Resource resource = storageService.loadAsResource(filename);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	@GetMapping("/{uname}")
	@ResponseBody
	public Set<Post> getPosts(@PathVariable String uname, Authentication authentication) {

		System.out.println(uname);

		 User currentUser = userService.findByUsername(uname);
		 //List<Post> list = postRepository.findAll();
		 Set<Post> posts = currentUser.getPosts();
		 
		return posts;

	}
	
	@DeleteMapping("/{postid}")
	@ResponseBody
	public String deletePost(@PathVariable Long postid, Authentication authentication) throws IOException {
		Post post = postRepository.findById(postid).get();
		postRepository.deleteById(postid);
		this.amazonClient.deleteFileFromS3Bucket(post.getPicPath());
		return "Post deleted";
	}
	
	@GetMapping("/followingPost")
	@ResponseBody
	public Set<Post> getFollowingPost(Authentication authentication) {
		String username = authentication.getName();
		User currentUser = userService.findByUsername(username);
		Set<Post> result = new HashSet<Post>(currentUser.getPosts());
		Set<User> following = currentUser.getFollowing();
		following.forEach((e) -> {
			System.out.println(e);
			result.addAll(e.getPosts());
			});
		
		return result;
	}
	
}
