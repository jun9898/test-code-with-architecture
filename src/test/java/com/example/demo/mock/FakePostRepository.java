package com.example.demo.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.example.demo.post.domain.Post;
import com.example.demo.post.service.port.PostRepository;

public class FakePostRepository implements PostRepository {

	private Long autoGenerateId = 0L;
	private final List<Post> data = new ArrayList<>();

	@Override
	public Optional<Post> findById(long id) {
		return data.stream()
			.filter(item -> item.getId().equals(id))
			.findAny();
	}

	@Override
	public Post save(Post post) {
		if(post.getId() == null || post.getId() == 0 ) {
			Post newPost = Post.builder()
				.id(autoGenerateId++)
				.writer(post.getWriter())
				.content(post.getContent())
				.createdAt(post.getCreatedAt())
				.modifiedAt(post.getModifiedAt())
				.build();
			data.add(newPost);
			return newPost;
		} else {
			data.removeIf(item -> Objects.equals(item.getId(), post.getId()));
			data.add(post);
			return post;
		}
	}
}
