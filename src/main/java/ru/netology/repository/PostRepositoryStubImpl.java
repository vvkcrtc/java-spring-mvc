package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepositoryStubImpl implements PostRepository {
    private final ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();
    private static final AtomicLong lastPosts = new AtomicLong();

    public List<Post> all() {
        return new ArrayList<>(posts.values());
    }

    public void addPost(Post post) {
        post.setId(lastPosts.incrementAndGet());
        posts.putIfAbsent(post.getId(), post);
    }

    public Optional<Post> getById(long id) {

        return Optional.ofNullable(posts.get(id));

    }

    public Post save(Post post) {
        long postId = post.getId();
        if (postId == 0) {
            addPost(post);
        } else {
            if (posts.containsKey(postId)) {
                Post newPost = new Post();
                newPost.setId(postId);
                newPost.setContent(post.getContent());
                posts.replace(postId, newPost);
            } else {
                addPost(post);
            }
        }
        return post;
    }


    public void removeById(long id) {
        posts.remove(id);
    }

}
