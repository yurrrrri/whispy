package com.syr.whispy.post.entity;

import com.syr.whispy.base.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Post extends BaseEntity implements Comparable<Post> {

    private String writer;
    private String content;
    private List<String> tags;
    private List<String> likeMembers;

    @Override
    public int compareTo(Post other) {
        return other.getCreateDate().compareTo(this.getCreateDate());
    }
}
