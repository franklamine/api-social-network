package com.frank.apisocialnetwork.dto;

import java.time.Instant;

public record OutPutCommentDTO(
        int id,
        int PublicationId,
        String message,
        UserSummaryDTO author,
        Instant createdAt
) {
}
