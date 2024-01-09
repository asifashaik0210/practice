package com.cello.service;

import com.cello.entity.Comment;
import com.cello.entity.Post;
import com.cello.exception.ResourceNotFound;
import com.cello.payload.CommentDto;
import com.cello.repository.CommentRepository;
import com.cello.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService{
private PostRepository postRepo;
private CommentRepository commentRepo;
private ModelMapper modelMapper;


    public CommentServiceImpl(PostRepository postRepo, CommentRepository commentRepo,ModelMapper modelMapper) {
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
        this.modelMapper = modelMapper;
    }



    @Override
    public CommentDto createComment(long postId,CommentDto commentDto) {
        Post post = postRepo.findById(postId).orElseThrow(
                ()-> new ResourceNotFound("post not found with id:"+postId)

        );
        Comment comment = mapToEntity(commentDto);
        comment.setPost(post);
        Comment c = commentRepo.save(comment);

        return mapToDto(c);
    }

    @Override
    public void deleteCommentById(long postId, long commentId) {
        Post post = postRepo.findById(postId).orElseThrow(
                ()-> new ResourceNotFound("post not found with id:"+postId)

        );
        commentRepo.deleteById(commentId);
    }

    @Override
    public List<CommentDto> getCommentsPostById(long postId) {
        List<Comment> comments = commentRepo.findByPostId(postId);
        List<CommentDto> dtos = comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
        return dtos;
    }

    @Override
    public CommentDto updateComment(long commentId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);
        comment.setId(commentId);
        Comment savedComment = commentRepo.save(comment);
        CommentDto dto = mapToDto(savedComment);
        return dto;
    }

    Comment mapToEntity(CommentDto dto){
        Comment comment = modelMapper.map(dto,Comment.class);
//        comment.setName(dto.getName());
//        comment.setEmail(dto.getEmail());
//        comment.setBody(dto.getBody());
        return comment;
    }
    CommentDto mapToDto(Comment comment){
        CommentDto dto = modelMapper.map(comment,CommentDto.class);
//        dto.setName(comment.getName());
//        dto.setEmail(comment.getEmail());
//        dto.setBody(comment.getBody());
        return dto;
    }
}
