package com.example.blogpost.controller;

import com.example.blogpost.model.Blog;
import com.example.blogpost.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/blogs")
public class BlogController {
    BlogService blogService;
    public BlogController(BlogService blogService){
        this.blogService = blogService;
    }

    @GetMapping("")
    public ResponseEntity<List<Blog>> getBlogs(){
        return new ResponseEntity<>(this.blogService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<Blog> getBlogById(@PathVariable UUID blogId){
        return new ResponseEntity<>(this.blogService.findById(blogId), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Blog> createBlog(@RequestBody Blog blog){
        return new ResponseEntity<>(this.blogService.create(blog), HttpStatus.CREATED);
    }

    @PatchMapping("/{blogId}")
    public ResponseEntity<Blog> updateBlog(@PathVariable UUID blogId, @RequestBody Blog blog){
        return new ResponseEntity<>(this.blogService.update(blogId, blog), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{blogId}")
    public ResponseEntity<Map<String, String>> deleteBlog(@PathVariable UUID blogId){
        this.blogService.deleteById(blogId);
        Map<String,String> responseMap = new HashMap<>(){{
            put("message", "Blog deleted successfully!");
        }};
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }
}
