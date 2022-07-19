package io.gitub.bhhan.oauth2.comment.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long articleId;
    private Long userId;
    private String description;

    public Comment(Long id, Long articleId, Long userId, String description) {
        this.id = id;
        this.articleId = articleId;
        this.userId = userId;
        this.description = description;
    }
}
