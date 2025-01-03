package com.wizzdi.segmantix.spring.request;

import java.time.OffsetDateTime;

public class DateFilter {

    private OffsetDateTime start;
    private OffsetDateTime end;

    public OffsetDateTime getStart() {
        return start;
    }

    public <T extends DateFilter> T setStart(OffsetDateTime start) {
        this.start = start;
        return (T) this;
    }

    public OffsetDateTime getEnd() {
        return end;
    }

    public <T extends DateFilter> T setEnd(OffsetDateTime end) {
        this.end = end;
        return (T) this;
    }

    public com.wizzdi.segmantix.store.jpa.request.DateFilter forService() {
        return new com.wizzdi.segmantix.store.jpa.request.DateFilter()
                .setStart(start)
                .setEnd(end);
    }
}
