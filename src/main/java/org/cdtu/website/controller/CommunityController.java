package org.cdtu.website.controller;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.cdtu.website.entity.Comments;
import org.cdtu.website.entity.HttpResult;
import org.cdtu.website.entity.Posts;
import org.cdtu.website.entity.table.CommentsTableDef;
import org.cdtu.website.service.CommentsService;
import org.cdtu.website.service.PostsService;
import org.cdtu.website.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/community")
public class CommunityController {
    PostsService postsService;

    @Autowired
    public void setPostsService(PostsService postsService) {
        this.postsService = postsService;
    }

    UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    CommentsService commentsService;

    @Autowired
    public void setCommentsService(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    @GetMapping("/postsPage")
    public HttpResult<Object> postPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        Page<Posts> page = Page.of(pageNum, pageSize);
        postsService.page(page);
        return HttpResult.success(page);
    }

    @PostMapping("/addPost")
    public HttpResult<Object> addPost(@RequestBody Posts posts) {
        posts.setCreateUser(userService.getCurrentUserId());
        postsService.save(posts);
        return HttpResult.success(null);
    }

    @DeleteMapping("/deletePost/{id}")
    public HttpResult<Object> deletePost(@PathVariable Long id) {
        postsService.removeById(id);
        return HttpResult.success(null);
    }

    @PutMapping("/updatePost")
    public HttpResult<Object> updatePost(@RequestBody Posts posts) {
        posts.setUpdateUser(userService.getCurrentUserId());
        postsService.updateById(posts);
        return HttpResult.success(null);
    }

    @GetMapping("/getPost/{id}")
    public HttpResult<Object> getPost(@PathVariable Long id) {
        return HttpResult.success(postsService.getById(id));
    }

    @PostMapping("/addComment")
    public HttpResult<Object> addComment(@RequestBody Comments comment) {
        comment.setCreateUser(userService.getCurrentUserId());
        commentsService.save(comment);
        return HttpResult.success(null);
    }

    @DeleteMapping("/deleteComment/{id}")
    public HttpResult<Object> deleteComment(@PathVariable Long id) {
        commentsService.removeById(id);
        return HttpResult.success(null);
    }

    @PutMapping("/updateComment")
    public HttpResult<Object> updateComment(@RequestBody Comments comment) {
        comment.setUpdateUser(userService.getCurrentUserId());
        commentsService.updateById(comment);
        return HttpResult.success(null);
    }

    @GetMapping("/getCommentPage/{postId}")
    public HttpResult<Object> getComment(@PathVariable Long postId, @RequestParam Integer pageNum, @RequestParam Integer pageSize){
        Page<Comments> page = Page.of(pageNum, pageSize);
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select()
                .where(CommentsTableDef.COMMENTS.POST_ID.eq(postId))
                .orderBy(CommentsTableDef.COMMENTS.CREATE_TIME.desc());
        return HttpResult.success(commentsService.page(page,queryWrapper));
    }
}
