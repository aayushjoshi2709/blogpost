package com.example.blogpost.service;

import com.example.blogpost.model.Blog;
import com.example.blogpost.model.User;
import com.example.blogpost.model.UserPrincipal;
import com.example.blogpost.repository.BlogRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
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

    private Blog getBlogByID(UUID id) throws Exception{
        Blog blog = this.blogRepository.findById(id).orElse(null);
        if(blog == null){
            throw new Exception("Blog post not found");
        }
        return blog;
    }

    public List<Blog> findAll(){
        return this.blogRepository.findAll();
    }

    public Blog findById(UUID id) throws Exception{
        return this.getBlogByID(id);
    }

    public Blog create(Blog blog){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        User user = userService.findByUsername(userPrincipal.getUsername());
        blog.setUser(user);
        return this.blogRepository.save(blog);
    }

    public void deleteById(UUID id) throws Exception{
        Blog blog = this.getBlogByID(id);
        this.blogRepository.delete(blog);
    }

    public Blog update(UUID blogId, Blog blog) throws Exception{
        Blog currentBlog = this.getBlogByID(blogId);
        currentBlog.setTitle(blog.getTitle());
        currentBlog.setContent(blog.getContent());
        return this.blogRepository.save(currentBlog);
    }
}
