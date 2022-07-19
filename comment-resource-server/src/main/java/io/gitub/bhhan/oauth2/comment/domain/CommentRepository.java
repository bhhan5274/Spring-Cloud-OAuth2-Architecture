package io.gitub.bhhan.oauth2.comment.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentsByArticleId(Long articleId);
}
