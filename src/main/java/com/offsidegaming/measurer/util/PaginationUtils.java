package com.offsidegaming.measurer.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@UtilityClass
public class PaginationUtils {

    public static <T> HttpHeaders generatePaginationHttpHeaders(Page<T> page, String baseUrl) {
        page.getSort();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", Long.toString(page.getTotalElements()));
        String link = "";
        if (page.getNumber() + 1 < page.getTotalPages()) {
            String uri = generateUri(baseUrl, page.getNumber() + 1, page.getSize(), page.getSort());
            link = "<" + uri + ">; rel=\"next\",";
        }

        if (page.getNumber() > 0) {
            link = link + "<" + generateUri(baseUrl, page.getNumber() - 1, page.getSize(), page.getSort()) + ">; rel=\"prev\",";
        }

        int lastPage = 0;
        if (page.getTotalPages() > 0) {
            lastPage = page.getTotalPages() - 1;
        }

        link = link + "<" + generateUri(baseUrl, lastPage, page.getSize(), page.getSort()) + ">; rel=\"last\",";
        link = link + "<" + generateUri(baseUrl, 0, page.getSize(), page.getSort()) + ">; rel=\"first\"";
        headers.add("Link", link);
        return headers;
    }

    public static UriComponentsBuilder addPageableQueryParams(UriComponentsBuilder builder, int page, int size, Sort sort) {
        if (!sort.isUnsorted()) {
            List<String> sorts = sort.get().map(ord -> ord.getProperty() + "," + ord.getDirection()).toList();
            builder.queryParam("sort", sorts);
        }
        return builder.queryParam("page", page)
            .queryParam("size", size);
    }

    private static String generateUri(String baseUrl, int page, int size, Sort sort) {
        return addPageableQueryParams(UriComponentsBuilder.fromPath(baseUrl), page, size, sort)
            .build().toString();
    }


}