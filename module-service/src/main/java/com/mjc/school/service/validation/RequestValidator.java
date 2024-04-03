package com.mjc.school.service.validation;

import com.mjc.school.service.errorsexceptions.ApiUriException;
import com.mjc.school.service.errorsexceptions.Errors;
import com.mjc.school.service.errorsexceptions.ValidatorException;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class RequestValidator {
    private final String NEWS_URI_PATTERN = "^/news-app/v1\\.0/news.*$";
    private final String AUTHORS_URI_PATTERN = "^/news-app/v1\\.0/authors.*$";
    private final String TAGS_URI_PATTERN = "^/news-app/v1\\.0/tags.*$";
    private final String COMMENTS_URI_PATTERN = "^/news-app/v1\\.0/comments.*$";
    private final String SWAGGER_UI_URI_PATTERN = "^/swagger-ui/.*$";
    private final String API_DOCS_URI_PATTERN = "^/v2/api-docs.*$";

    public void validateApiRootPath(String requestUri) {
        if (Pattern.matches(NEWS_URI_PATTERN, requestUri) ||
                Pattern.matches(AUTHORS_URI_PATTERN, requestUri) ||
                Pattern.matches(TAGS_URI_PATTERN, requestUri) ||
                Pattern.matches(COMMENTS_URI_PATTERN, requestUri) ||
                Pattern.matches(SWAGGER_UI_URI_PATTERN, requestUri) ||
                Pattern.matches(API_DOCS_URI_PATTERN, requestUri)) {
        } else {
            throw new ApiUriException(Errors.ERROR_WRONG_API_URI.getErrorData("", false));
        }
    }

    public Map<String, String> validateRequestParams(String params) {
        String cleanedParams = cleanRequestParams(params);
        Map<String, String> paramNameValues = new HashMap<>();

        // Split the cleanedParams string into individual parameter-value pairs
        String[] paramPairs = cleanedParams.split("&");

        for (String param : paramPairs) {
            // Split the parameter-value pair into parameter and value
            String[] parts = param.split("=");

            // Check the parameter name and perform validation accordingly
            if (parts.length == 2) {
                String paramName = parts[0].toLowerCase();
                String paramValue = parts[1];

                if (paramName.equals("page")) {
                    if (!validatePageSizeValue(paramValue)) {
                        throw new ValidatorException(Errors.ERROR_REQUEST_PARAM_PAGE
                                .getErrorData(paramValue, true));
                    }
                    paramNameValues.put(paramName, paramValue);
                } else if (paramName.equals("size")) {
                    if (!validatePageSizeValue(paramValue)) {
                        throw new ValidatorException(Errors.ERROR_REQUEST_PARAM_SIZE
                                .getErrorData(paramValue, true));
                    }
                    paramNameValues.put(paramName, paramValue);
                } else if (paramName.equals("sort")) {
                    if (!validateSortValue(paramValue)) {
                        throw new ValidatorException(Errors.ERROR_REQUEST_PARAM_SORT
                                .getErrorData(paramValue, true));
                    }
                    paramNameValues.put(paramName, paramValue);
                } else {
                    throw new ValidatorException(Errors.ERROR_REQUEST_PARAM_NAME.getErrorData(paramName, true));
                }
            } else {
                throw new ValidatorException(Errors.ERROR_REQUEST_PARAM_FORMAT.getErrorMessage());
            }
        }
        return paramNameValues;
    }

    private boolean validatePageSizeValue(String paramValue) {
        try {
            int page = Integer.parseInt(paramValue);
            return page > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean validateSortValue(String paramValue) {
        String[] parts = paramValue.split("\\:");
        if (parts.length == 1) {
            return true;
        } else if (parts.length == 2) {
            String sortField = parts[0];
            String sortOrder = parts[1];
            return Arrays.asList("asc", "desc").contains(sortOrder.toLowerCase());
        }
        return false;
    }

    public String cleanRequestParams(String params) {
        try {
            // Decode the input string to handle URL encoding
            String decodedInput = URLDecoder.decode(params, "UTF-8");
            String cleanedParams = decodedInput.replaceAll("\\s+", "").toLowerCase();

            return cleanedParams;
        } catch (Exception e) {
            e.getMessage();
        }
        return "";
    }

    public boolean isNumber(String id) {
        char[] chars = id.toCharArray();
        int counter=0;
        if (chars[0]=='-') {
            counter++;
        }
        for (int i=0; i<chars.length; i++) {
            if (Character.isDigit(chars[i])) {
                counter++;
            }
        }
        return counter==chars.length;
    }
}