package br.com.fiap.cheffy.infrastructure.persistence.pagination;


import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageMapper {

    public static Pageable toSpringPageable(PageRequest pageRequest) {
        Sort sort = pageRequest.direction() == PageRequest.SortDirection.DESC
            ? Sort.by(pageRequest.sortBy()).descending()
            : Sort.by(pageRequest.sortBy()).ascending();
        
        return org.springframework.data.domain.PageRequest.of(
            pageRequest.page(), 
            pageRequest.size(), 
            sort
        );
    }
    

    public static <T> PageResult<T> toDomainPageResult(Page<T> springPage) {
        return PageResult.of(
            springPage.getContent(),
            springPage.getNumber(),
            springPage.getSize(),
            springPage.getTotalElements()
        );
    }
}