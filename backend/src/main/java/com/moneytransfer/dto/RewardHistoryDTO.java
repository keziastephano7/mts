package com.moneytransfer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for reward history entry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardHistoryDTO {
    private Long id;
    private String transactionId;
    private Long pointsGranted;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime grantedOn;
}
