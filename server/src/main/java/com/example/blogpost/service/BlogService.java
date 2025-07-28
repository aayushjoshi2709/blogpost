package com.example.blogpost.service;

import com.example.blogpost.model.Blog;
import com.example.blogpost.model.User;
import com.example.blogpost.model.UserPrincipal;
import com.example.blogpost.repository.BlogRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class BlogService {
    public final BlogRepository blogRepository;
    private final UserService userService;

    BlogService(BlogRepository blogRepository, UserService userService) {
        this.blogRepository = blogRepository;
        this.userService = userService;
    }

    private Blog getBlogByID(UUID id) throws ResponseStatusException{
        return this.blogRepository.findById(id).orElseThrow(
                () -> {
                    System.out.println("throwing exception");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found!");
                }
        );
    }

    private User getUserFromSecurityContext(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        return userService.findByUsername(userPrincipal.getUsername());
    }

    private void checkBlogPostBelongsToUser(Blog currentBlog) throws ResponseStatusException{
        User user = getUserFromSecurityContext();
        if(!currentBlog.getUser().getId().equals(user.getId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Blog post does not belong to this user");
        }
    }

    public List<Blog> findAll(){
        return this.blogRepository.findAll();
    }

    public Blog findById(UUID id) throws ResponseStatusException{
        return this.getBlogByID(id);
    }

    public Blog create(Blog blog){
        blog.setUser(getUserFromSecurityContext());
        return this.blogRepository.save(blog);
    }

    public void deleteById(UUID id) throws ResponseStatusException{
        Blog blog = this.getBlogByID(id);
        this.checkBlogPostBelongsToUser(blog);
        this.blogRepository.delete(blog);
    }

    public Blog update(UUID blogId, Blog blog) throws ResponseStatusException{
        Blog currentBlog = this.getBlogByID(blogId);
        this.checkBlogPostBelongsToUser(currentBlog);
        currentBlog.setTitle(blog.getTitle());
        currentBlog.setContent(blog.getContent());
        return this.blogRepository.save(currentBlog);
    }
}
