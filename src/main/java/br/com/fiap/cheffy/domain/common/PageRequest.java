package br.com.fiap.cheffy.domain.common;

public record PageRequest(
    int page,
    int size,
    String sortBy,
    SortDirection direction
) {
    
    public enum SortDirection {
        ASC, DESC
    }
    
    public static PageRequest of(int page, int size) {
        return new PageRequest(page, size, "id", SortDirection.ASC);
    }
    
    public static PageRequest of(int page, int size, String sortBy, SortDirection direction) {
        return new PageRequest(page, size, sortBy, direction);
    }
    

    public PageRequest {
        if (page < 0) {
            throw new IllegalArgumentException("Page must be >= 0");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be > 0");
        }
        if (sortBy == null || sortBy.isBlank()) {
            throw new IllegalArgumentException("SortBy cannot be empty");
        }
        if (direction == null) {
            throw new IllegalArgumentException("Direction cannot be null");
        }
    }
}