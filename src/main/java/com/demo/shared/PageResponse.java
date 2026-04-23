package com.demo.shared;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse <T>(
    List<T> content,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean hasNext,            // Frontend flag for next button
    boolean hasPrevious,        // Frontend flag for previous button
    boolean last,               //Frontend flag for last page
    boolean first               //Frontend flag for first page
) {
    public static <T> PageResponse<T> fromPage(Page<T> page){
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious(),
                page.isLast(),
                page.isFirst()
        );
    }
}

