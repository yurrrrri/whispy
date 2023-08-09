package com.syr.whispy.comment.service;

import com.syr.whispy.base.exception.DataNotFoundException;
import com.syr.whispy.comment.dto.SubCommentCreateDto;
import com.syr.whispy.comment.dto.SubCommentUpdateDto;
import com.syr.whispy.comment.entity.SubComment;
import com.syr.whispy.comment.repository.SubCommentRepository;
import com.syr.whispy.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.syr.whispy.comment.code.CommentErrorCode.COMMENT_NOT_EXISTS;
import static com.syr.whispy.comment.code.SubCommentErrorCode.SUB_COMMENT_NOT_EXISTS;
import static com.syr.whispy.member.code.MemberErrorCode.MEMBER_NOT_EXISTS;

@RequiredArgsConstructor
@Service
public class SubCommentService {

    private final SubCommentRepository subCommentRepository;
    private final MemberService memberService;
    private final CommentService commentService;

    public Optional<SubComment> findById(String id) {
        return subCommentRepository.findById(id);
    }

    public SubComment findByIdAndGet(String id) {
        Optional<SubComment> opSubComment = findById(id);

        if (opSubComment.isEmpty()) {
            throw new DataNotFoundException(SUB_COMMENT_NOT_EXISTS);
        }

        return opSubComment.get();
    }

    public List<SubComment> findByCommentId(String commentId) {
        return subCommentRepository.findByComment(commentId);
    }

    public SubComment create(SubCommentCreateDto dto) {
        if (memberService.findById(dto.getWriter()).isEmpty()) {
            throw new DataNotFoundException(MEMBER_NOT_EXISTS);
        }

        if (commentService.findById(dto.getComment()).isEmpty()) {
            throw new DataNotFoundException(COMMENT_NOT_EXISTS);
        }

        return subCommentRepository.insert(SubComment.builder()
                .id(UUID.randomUUID().toString())
                .createDate(LocalDateTime.now())
                .writer(dto.getWriter())
                .comment(dto.getComment())
                .content(dto.getContent())
                .build()
        );
    }

    public SubComment update(SubCommentUpdateDto dto) {
        SubComment subComment = findByIdAndGet(dto.getSubComment());

        return subCommentRepository.save(subComment.toBuilder()
                .modifyDate(LocalDateTime.now())
                .content(dto.getContent())
                .build()
        );
    }

    public String softDelete(String id) {
        SubComment subComment = findByIdAndGet(id);
        String commentId = subComment.getComment();

        subCommentRepository.save(subComment.toBuilder()
                .deleteDate(LocalDateTime.now())
                .build());

        return commentId;
    }

    public String hardDelete(String id) {
        if (!subCommentRepository.existsById(id)) {
            throw new DataNotFoundException(SUB_COMMENT_NOT_EXISTS);
        }

        String commentId = findByIdAndGet(id).getComment();
        subCommentRepository.deleteById(id);

        return commentId;
    }

}
