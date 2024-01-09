package com.cello.service;

import com.cello.payload.CommentDto;

import java.util.List;

public interface CommentService {
   CommentDto createComment(long postId,CommentDto commentDto);
   public void deleteCommentById(long postId,long commentId);
   List<CommentDto> getCommentsPostById(long postId);
   CommentDto updateComment(long commentId,CommentDto commentDto);
}
