package com.example.blogpost.service;

import com.example.blogpost.model.Blog;
import com.example.blogpost.model.User;
import com.example.blogpost.model.UserPrincipal;
import com.example.blogpost.repository.BlogRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private User getUserFromSecurityContext(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        return userService.findByUsername(userPrincipal.getUsername());
    }

    private void checkBlogPostBelongsToUser(Blog currentBlog) throws Exception{
        User user = getUserFromSecurityContext();
        if(!currentBlog.getUser().getId().equals(user.getId())){
            throw new Exception("Blog post does not belong to this user");
        }
    }

    public List<Blog> findAll(){
        return this.blogRepository.findAll();
    }

    public Blog findById(UUID id) throws Exception{
        return this.getBlogByID(id);
    }

    public Blog create(Blog blog){
        blog.setUser(getUserFromSecurityContext());
        return this.blogRepository.save(blog);
    }

    public void deleteById(UUID id) throws Exception{
        Blog blog = this.getBlogByID(id);
        this.checkBlogPostBelongsToUser(blog);
        this.blogRepository.delete(blog);
    }

    public Blog update(UUID blogId, Blog blog) throws Exception{
        Blog currentBlog = this.getBlogByID(blogId);
        this.checkBlogPostBelongsToUser(currentBlog);
        currentBlog.setTitle(blog.getTitle());
        currentBlog.setContent(blog.getContent());
        return this.blogRepository.save(currentBlog);
    }
}
