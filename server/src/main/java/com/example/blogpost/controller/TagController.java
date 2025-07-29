package com.example.blogpost.controller;

import com.example.blogpost.model.Tag;
import com.example.blogpost.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController()
@RequestMapping("/api/v1/tags")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService){
        this.tagService = tagService;
    }

    @GetMapping("")
    public ResponseEntity<List<Tag>> findAll(){
        return new ResponseEntity<>(this.tagService.findAll(),HttpStatus.OK);
    }

    @GetMapping("/{tagId}")
    public ResponseEntity<Tag> findById(@PathVariable UUID tagId){
        return new ResponseEntity<>(this.tagService.findById(tagId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag){
        return new ResponseEntity<>(this.tagService.create(tag), HttpStatus.CREATED);
    }

    @PatchMapping("/{tagId}")
    public ResponseEntity<Tag> updateTag(@PathVariable UUID tagId, @RequestBody Tag tag){
        return new ResponseEntity<>(this.tagService.update(tagId, tag), HttpStatus.OK);
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<Map<String, String>> deleteTag(@PathVariable UUID tagId){
        this.tagService.deleteById(tagId);
        Map<String,String> responseMap = new HashMap<>(){{
            put("message", "Tag deleted successfully!");
        }};
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }
}
