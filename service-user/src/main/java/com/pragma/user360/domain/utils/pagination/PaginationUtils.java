package com.pragma.user360.domain.utils.pagination;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationUtils {
    
    private PaginationUtils() {
        // Constructor privado para evitar instanciación
    }

    public static Pageable createPageable(Integer page, Integer size, String sortField, Sort.Direction direction) {
        Sort sort = Sort.by(direction != null ? direction : Sort.Direction.ASC, 
                          sortField != null && !sortField.isBlank() ? sortField : "id");
        
        return PageRequest.of(
            page != null ? page : 0,
            size != null ? size : 10,
            sort
        );
    }

    public static <T> PagedResult<T> toPagedResult(Page<T> page) {
        return new PagedResult<>(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }
} 