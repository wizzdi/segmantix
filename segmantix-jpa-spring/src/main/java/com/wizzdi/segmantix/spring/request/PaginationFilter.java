package com.wizzdi.segmantix.spring.request;

import com.wizzdi.segmantix.store.jpa.request.BaseclassFilter;
import jakarta.validation.constraints.Min;

public class PaginationFilter {
    @Min(value = 1)
    private Integer pageSize;
    @Min(value = 0)
    private Integer currentPage;

    public Integer getPageSize() {
        return pageSize;
    }

    public <T extends PaginationFilter> T setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return (T) this;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public <T extends PaginationFilter> T setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
        return (T) this;
    }

    protected void forService(com.wizzdi.segmantix.store.jpa.request.PaginationFilter paginationFilter) {
        paginationFilter.setCurrentPage(this.currentPage)
                .setPageSize(this.pageSize);

    }
}
