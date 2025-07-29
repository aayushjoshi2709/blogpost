package com.example.blogpost.service;

import com.example.blogpost.model.Blog;
import com.example.blogpost.model.Tag;
import com.example.blogpost.model.User;
import com.example.blogpost.repository.BlogRepository;
import com.example.blogpost.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class BlogService {
    public final BlogRepository blogRepository;
    private final Utils utils;
    private final TagService tagService;

    BlogService(BlogRepository blogRepository, Utils utils, TagService tagService) {
        this.blogRepository = blogRepository;
        this.utils = utils;
        this.tagService = tagService;
    }

    private Blog getBlogByID(UUID id) throws ResponseStatusException{
        return this.blogRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found!")
        );
    }

    private void checkBlogPostBelongsToUser(Blog currentBlog) throws ResponseStatusException{
        User user = this.utils.getUserFromSecurityContext();
        if(!currentBlog.getAuthor().getId().equals(user.getId())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not allowed to perform this action!");
        }
    }

    private List<Tag> fetchTags(List<Tag> tags){
        List<UUID> tagIds = tags.stream().map(Tag::getId).toList();
        return this.tagService.fetchTagsByIds(tagIds);
    }

    public List<Blog> findAll(){
        return this.blogRepository.findAll();
    }

    public Blog findById(UUID id) throws ResponseStatusException{
        return this.getBlogByID(id);
    }

    public Blog create(Blog blog){
        blog.setAuthor(this.utils.getUserFromSecurityContext());
        List<Tag> fetchedTags = this.fetchTags(blog.getTags());
        blog.setTags(fetchedTags);
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

        String title = blog.getTitle();
        if(title != null && !title.isEmpty() && !title.equals(currentBlog.getTitle())){
            currentBlog.setTitle(title);
        }

        String content = blog.getContent();
        if(content != null && !content.isEmpty() && !content.equals(currentBlog.getContent())){
            currentBlog.setContent(content);
        }

        List<Tag> tags = blog.getTags();
        if(tags != null && !tags.isEmpty() && !tags.equals(currentBlog.getTags())){
            List<Tag> fetchedTags = this.fetchTags(tags);
            currentBlog.setTags(fetchedTags);
        }

        Blog resultantBlog = this.blogRepository.save(currentBlog);
        resultantBlog.getTags().size();
        return resultantBlog;
    }
}
