package com.example.blogpost.controller;

import com.example.blogpost.model.Blog;
import com.example.blogpost.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        try {
            return new ResponseEntity<>(this.blogService.findById(blogId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/")
    public ResponseEntity<Blog> createBlog(@RequestBody Blog blog){
        try{
            return new ResponseEntity<>(this.blogService.create(blog), HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{blogId}")
    public ResponseEntity<Blog> updateBlog(@PathVariable UUID blogId, @RequestBody Blog blog){
        try{
            return new ResponseEntity<>(this.blogService.update(blogId, blog), HttpStatus.ACCEPTED);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{blogId}")
    public ResponseEntity<Blog> deleteBlog(@PathVariable UUID blogId){
        try{
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
