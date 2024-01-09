package com.cello.controller;

import com.cello.payload.CommentDto;
import com.cello.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/comments")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    @PostMapping
    public ResponseEntity<CommentDto> createComment(@RequestParam long postId, @RequestBody CommentDto commentDto){
        CommentDto dto = commentService.createComment(postId, commentDto);
    return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
    @DeleteMapping
    public ResponseEntity<String> deleteCommentById(@RequestParam long postId,@RequestParam long commentId){
        commentService.deleteCommentById(postId, commentId);
        return new ResponseEntity<>("comment is deleted",HttpStatus.OK);
    }
    @GetMapping
    public List<CommentDto> getCommentsByPostId(@RequestParam long postId){
        List<CommentDto> commentDtos = commentService.getCommentsPostById(postId);
        return commentDtos;
    }
    @PutMapping
    public ResponseEntity<CommentDto> updateComment(@RequestParam long commentId,@RequestBody CommentDto commentDto){
        CommentDto dto = commentService.updateComment(commentId, commentDto);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }
}
