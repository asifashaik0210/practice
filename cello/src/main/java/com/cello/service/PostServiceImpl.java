package com.cello.service;

import com.cello.entity.Post;
import com.cello.exception.ResourceNotFound;
import com.cello.payload.PostDto;
import com.cello.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService{
    private PostRepository postRepo;
    private ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepo,ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.postRepo = postRepo;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        Post post = mapToEntity(postDto);
        Post savedPost = postRepo.save(post);
        PostDto dto = mapToDto(savedPost);
        return dto;
    }

    @Override
    public void deletePostById(long id) {
        Post post = postRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFound("post not found with id:"+id)

        );
        postRepo.deleteById(id);
    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFound("post not found with id:"+id)
        );
        return mapToDto(post);
    }

    @Override
    public PostDto updatePost(long id, PostDto postDto) {
        Post post = postRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFound("post not found with id:"+id)
        );
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        Post savedPost = postRepo.save(post);
        PostDto dto = mapToDto(savedPost);
        return dto;
    }

    @Override
    public List<PostDto> getAllPosts(int pageNo, int pageSize) {

       Pageable pageable =  PageRequest.of(pageNo,pageSize);
       Page<Post> pagePostsObjects = postRepo.findAll(pageable);
       List<Post> posts = pagePostsObjects.getContent();
        List<PostDto> dtos = posts.stream().map(post->mapToDto(post)).collect(Collectors.toList());

        return dtos;
    }

    PostDto mapToDto(Post savedPost){
        PostDto postDto = modelMapper.map(savedPost,PostDto.class);
//        postDto.setId(savedPost.getId());
//        postDto.setTitle(savedPost.getTitle());
//        postDto.setDescription(savedPost.getDescription());
//        postDto.setContent(savedPost.getContent());
        return postDto;
    }
    Post mapToEntity(PostDto postDto){
        Post post = modelMapper.map(postDto,Post.class);
//        Post post = new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        return post;
    }
}
