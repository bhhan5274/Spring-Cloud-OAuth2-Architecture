package io.gitub.bhhan.oauth2.comment.service;

import io.gitub.bhhan.oauth2.comment.domain.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class CommentDto {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Request {
        private Long articleId;
        private Long userId;
        private String description;

        public Comment toEntity(){
            return new Comment(null, articleId, userId, description);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response {
        private Long id;
        private Long articleId;
        private Long userId;
        private String description;

        public Response(Comment comment) {
            this.id = comment.getId();
            this.articleId = comment.getArticleId();
            this.userId = comment.getUserId();
            this.description = comment.getDescription();
        }
    }
}
