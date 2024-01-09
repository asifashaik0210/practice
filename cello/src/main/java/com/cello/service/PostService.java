package com.cello.service;

import com.cello.payload.PostDto;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);
    void deletePostById(long id);

    PostDto getPostById(long id);

    PostDto updatePost(long id, PostDto postDto);
    List<PostDto> getAllPosts(int pageNo, int pageSize);


}
