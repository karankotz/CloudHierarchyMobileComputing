package com.edge.http.servlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.edge.http.configuration.FilterMapping;
import com.edge.http.configuration.ServletMapping;

public class ServletContextHelper {

    //@Nullable
    public ServletMapping getResolvedServletMapping(ServletContext servletContext, String path) {
        Objects.requireNonNull(servletContext);
        for (ServletMapping servletMapping : servletContext.getServletMappings()) {
            String inContextPath = getPathInContext(servletContext, path);
            if (servletMapping.getUrlPattern().matcher(inContextPath).matches()) {
                return servletMapping;
            }
        }

        return null;
    }

    //@Nullable
    public ServletContextWrapper getResolvedContext(List<ServletContextWrapper> servletContexts, String path) {
        for (ServletContextWrapper servletContext : servletContexts) {
            if (path.startsWith(servletContext.getContextPath())) {
                return servletContext;
            }
        }
        return null;
    }

    /**
     * Returns a list of filters to be included for given path. It first checks whether the filter
     * is included, and then checks whether the filter is excluded for given path.
     * <p>
     * Filter included URL pattern must not be null. Filter excluded URL pattern can be null.
     *
     * @param servletContext
     * @param path
     * @return
     */
    public List<FilterMapping> getFilterMappingsForPath(ServletContext servletContext, String path) {
        Objects.requireNonNull(servletContext);
        String inContextPath = getPathInContext(servletContext, path);

        List<FilterMapping> filterMappings = new ArrayList<>();
        for (FilterMapping filterMapping : servletContext.getFilterMappings()) {
            if (filterMapping.getUrlPattern().matcher(inContextPath).matches()) {
                if (filterMapping.getUrlExcludePattern() != null) {
                    if (!filterMapping.getUrlExcludePattern().matcher(inContextPath).matches()) {
                        filterMappings.add(filterMapping);
                    }
                } else {
                    filterMappings.add(filterMapping);
                }
            }
        }

        return filterMappings;
    }

    private String getPathInContext(ServletContext servletContext, String path) {
        return path.substring(servletContext.getContextPath().length());
    }
}
