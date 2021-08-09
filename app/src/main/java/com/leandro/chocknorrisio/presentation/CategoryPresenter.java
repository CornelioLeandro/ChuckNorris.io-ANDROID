package com.leandro.chocknorrisio.presentation;

import com.leandro.chocknorrisio.Colors;
import com.leandro.chocknorrisio.MainActivity;
import com.leandro.chocknorrisio.datasource.CategoryRemoteDataSource;
import com.leandro.chocknorrisio.model.CategoryItem;

import java.util.ArrayList;
import java.util.List;

public class CategoryPresenter implements CategoryRemoteDataSource.ListCategoriesCallback {

    private final MainActivity view;
    private final CategoryRemoteDataSource datasource;

    public CategoryPresenter(MainActivity mainActivity, CategoryRemoteDataSource dataSource) {
        this.view = mainActivity;
        this.datasource = dataSource;
    }

    public void requestAll() {
        this.view.showProgressBar();
        this.datasource.findall(this);
    }

    @Override
    public void onSuccess(List<String> response) {
     List<CategoryItem> categoryItems = new ArrayList<>();
             for(String val : response){
                categoryItems.add(new CategoryItem(val, Colors.randomColor()));
             }
             view.showCategories(categoryItems);
    }

    @Override
    public void onComplete() {
        this.view.hideProgressBar();
    }

    @Override
    public void onError(String message) {
        this.view.showFailure(message);
    }
}
