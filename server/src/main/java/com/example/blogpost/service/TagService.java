package com.example.blogpost.service;

import com.example.blogpost.model.Tag;
import com.example.blogpost.model.User;
import com.example.blogpost.repository.TagRepository;
import com.example.blogpost.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class TagService {
    private final TagRepository tagRepository;
    private final Utils utils;

    public TagService(TagRepository tagRepository, Utils utils) {
        this.tagRepository = tagRepository;
        this.utils = utils;
    }

    private Tag getTagById(UUID tagId) throws ResponseStatusException{
        return tagRepository.findById(tagId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found!")
        );
    }

    private void checkTagBelongsToUser(Tag tag) throws ResponseStatusException{
       User currentUser = this.utils.getUserFromSecurityContext();
       if(!currentUser.getId().equals(tag.getCreator().getId())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not allowed to perform this action!");
       }
    }

    public List<Tag> findAll() {
        return this.tagRepository.findAll();
    }

    public Tag findById(UUID tagId) {
        return this.getTagById(tagId);
    }

    public Tag create(Tag tag) throws ResponseStatusException{
        tag.setCreator(this.utils.getUserFromSecurityContext());
        return this.tagRepository.save(tag);
    }

    public Tag update(UUID tagId, Tag tag) throws ResponseStatusException {
        Tag currentTag = this.getTagById(tagId);
        checkTagBelongsToUser(currentTag);

        String description = tag.getDescription();
        if(description != null && !description.isEmpty() && !description.equals(currentTag.getDescription())){
            currentTag.setDescription(description);
        }

        String tagName = tag.getTagName();
        if(tagName != null && !tagName.isEmpty() && !tagName.equals(currentTag.getTagName())){
            currentTag.setTagName(tagName);
        }

        return this.tagRepository.save(currentTag);
    }

    public void deleteById(UUID tagId) throws ResponseStatusException {
        Tag tag = this.getTagById(tagId);
        checkTagBelongsToUser(tag);
        this.tagRepository.deleteById(tagId);
    }

    public List<Tag> fetchTagsByIds(List<UUID> tagIds) {
        return this.tagRepository.findAllById(tagIds);
    }
}
