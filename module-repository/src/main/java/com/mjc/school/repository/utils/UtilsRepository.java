package com.mjc.school.repository.utils;

import com.mjc.school.repository.model.CommentModel;
import com.mjc.school.repository.model.NewsModel;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UtilsRepository {

    public static String parseSortValue(String sortValue) {
        String[] parts = sortValue.split("\\:");

        if (parts.length == 1) {
            if (sortValue.toLowerCase().equals("authorid")) {
                sortValue="authorModel";
            }
            if (sortValue.toLowerCase().equals("newsid")) {
                sortValue="newsModel";
            }
            if (sortValue.toLowerCase().equals("createdate")) {
                sortValue="createDate";
            }
            if (sortValue.toLowerCase().equals("lastupdatedate")) {
                sortValue="lastUpdateDate";
            }
        } else if (parts.length == 2) {
            String sortField = parts[0];
            String sortOrder = parts[1];

            if (sortField.toLowerCase().equals("authorid")) {
                sortField="authorModel";
            }
            if (sortField.toLowerCase().equals("newsid")) {
                sortField="newsModel";
            }
            if (sortField.toLowerCase().equals("createdate")) {
                sortField="createDate";
            }
            if (sortField.toLowerCase().equals("lastupdatedate")) {
                sortField="lastUpdateDate";
            }
            sortValue = sortField + " " + sortOrder;
        }
        return sortValue;
    }

    public static void sortNewsByDate(List<NewsModel> newsModelList, String sortField, String sortOrder) {

        if (sortField==null || sortField.isEmpty()) {
            sortField = "createdate";
        }
        if (sortOrder==null || sortOrder.isEmpty()) {
            sortOrder = "desc";
        }

        String finalSortField = sortField;
        String finalSortOrder = sortOrder;
        Comparator<NewsModel> comparator = (n1, n2) -> {
            LocalDateTime date1 = null;
            LocalDateTime date2 = null;

            if (finalSortField.equals("createdate")) {
                date1 = n1.getCreateDate();
                date2 = n2.getCreateDate();
            } else if (finalSortField.equals("lastupdatedate")) {
                date1 = n1.getLastUpdateDate();
                date2 = n2.getLastUpdateDate();
            }

            if (finalSortOrder.equals("asc")) {
                return date1.compareTo(date2);
            } else if (finalSortOrder.equals("desc")) {
                return date2.compareTo(date1);
            }

            return 0;
        };

        Collections.sort(newsModelList, comparator);
    }

    public static void sortCommentsByDate(List<CommentModel> commentModelList, String sortField, String sortOrder) {

        if (sortField==null || sortField.isEmpty()) {
            sortField = "createdate";
        }
        if (sortOrder==null || sortOrder.isEmpty()) {
            sortOrder = "desc";
        }

        String finalSortField = sortField;
        String finalSortOrder = sortOrder;
        Comparator<CommentModel> comparator = (n1, n2) -> {
            LocalDateTime date1 = null;
            LocalDateTime date2 = null;

            if (finalSortField.equals("createdate")) {
                date1 = n1.getCreateDate();
                date2 = n2.getCreateDate();
            } else if (finalSortField.equals("lastupdatedate")) {
                date1 = n1.getLastUpdateDate();
                date2 = n2.getLastUpdateDate();
            }

            if (finalSortOrder.equals("asc")) {
                return date1.compareTo(date2);
            } else if (finalSortOrder.equals("desc")) {
                return date2.compareTo(date1);
            }

            return 0;
        };

        Collections.sort(commentModelList, comparator);
    }
}