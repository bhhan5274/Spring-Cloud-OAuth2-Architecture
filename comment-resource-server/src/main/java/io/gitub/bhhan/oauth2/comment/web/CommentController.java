package io.gitub.bhhan.oauth2.comment.web;

import io.gitub.bhhan.oauth2.comment.service.CommentDto;
import io.gitub.bhhan.oauth2.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/v1/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PreAuthorize("hasAuthority('SCOPE_write')")
    @GetMapping("/{articleId}")
    public List<CommentDto.Response> getCommentsByArticleId(@PathVariable Long articleId){
        return commentService.getCommentsByArticleId(articleId);
    }

    @PreAuthorize("hasAuthority('SCOPE_read')")
    @GetMapping("/scope")
    public String scopeRead() {
        return "Hello World";
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    public CommentDto.Response addComment(@RequestBody CommentDto.Request request) {
        return commentService.addComment(request);
    }

    @DeleteMapping("/{commentId}")
    public void deleteCommentById(@PathVariable Long commentId, @AuthenticationPrincipal Jwt jwt) {
        CommentDto.Response commentById = commentService.getCommentById(commentId);

        if(scopeContains(jwt, "delete")){
            commentService.deleteCommentById(commentId);
        }else {
            if(Objects.isNull(jwt.getClaims().get("id"))){
                throw new IllegalArgumentException();
            }

            if(!commentById.getUserId().equals(jwt.getClaims().get("id"))){
                throw new IllegalArgumentException();
            }
            commentService.deleteCommentById(commentId);
        }
    }

    @SuppressWarnings("unchecked")
    private boolean scopeContains(Jwt jwt, String... args){
        List<String> scopes = (List<String>) jwt.getClaims().get("scope");
        for (String arg : args) {
            if(scopes.contains(arg)){
                return true;
            }
        }
        return false;
    }
}
