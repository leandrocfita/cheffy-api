package br.com.fiap.cheffy.domain.common;

import java.util.List;

public record PageResult<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last,
        boolean empty
) {

    public static <T> PageResult<T> of(
            List<T> content,
            int page,
            int size,
            long totalElements) {

        int totalPages = (int) Math.ceil((double) totalElements / size);
        boolean first = page == 0;
        boolean last = page >= totalPages - 1;
        boolean empty = content.isEmpty();

        return new PageResult<>(
                content,
                page,
                size,
                totalElements,
                totalPages,
                first,
                last,
                empty
        );
    }

    public static <S, T> PageResult<T> from(
            PageResult<S> source,
            List<T> mappedContent) {

        return new PageResult<>(
                mappedContent,
                source.page(),
                source.size(),
                source.totalElements(),
                source.totalPages(),
                source.first(),
                source.last(),
                source.empty()
        );
    }


    public static <T> PageResult<T> emptyPage() {
        return new PageResult<>(
                List.of(),
                0,
                0,
                0,
                0,
                true,
                true,
                true
        );
    }


    public int numberOfElements() {
        return content.size();
    }
}