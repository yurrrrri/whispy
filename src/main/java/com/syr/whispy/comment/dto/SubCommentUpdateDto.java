package com.syr.whispy.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SubCommentUpdateDto {

    private String subComment;

    @NotBlank
    private String content;

}
