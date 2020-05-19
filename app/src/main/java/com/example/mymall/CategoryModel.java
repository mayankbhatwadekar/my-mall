package com.example.mymall;

public class CategoryModel {
    public String categoryIconLink,categoryName;

    public CategoryModel(String categoryIconLink, String categoryName) {
        this.categoryIconLink = categoryIconLink;
        this.categoryName = categoryName;
    }

    public String getCategoryIconlink() {
        return categoryIconLink;
    }

    public void setCategoryIconlink(String categoryIconlink) {
        this.categoryIconLink = categoryIconlink;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
