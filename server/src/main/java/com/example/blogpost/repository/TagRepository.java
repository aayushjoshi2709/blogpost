package com.example.blogpost.repository;

import com.example.blogpost.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {
}
