
package com.example.blogpost.model;

import com.example.blogpost.model.common.CommonModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Tag extends CommonModel {
    private String tagName;
    private String description;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User creator;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private List<Blog> blogs;
}
