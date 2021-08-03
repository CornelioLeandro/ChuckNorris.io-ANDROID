package com.leandro.chocknorrisio.presentation;

import android.os.Handler;

import com.leandro.chocknorrisio.MainActivity;
import com.leandro.chocknorrisio.model.CategoryItem;

import java.util.ArrayList;
import java.util.List;

public class CategoryPresenter {

    private static List<CategoryItem> fakeResponse = new ArrayList<>();
    private final MainActivity view;

    static {
        fakeResponse.add(new CategoryItem("Cat1", 0xF500FFFF));
        fakeResponse.add(new CategoryItem("Cat2", 0xAC04F4FF));
        fakeResponse.add(new CategoryItem("Cat3", 0x0F00bABF));
        fakeResponse.add(new CategoryItem("Cat4", 0x0F23F2FF));
        fakeResponse.add(new CategoryItem("Cat5", 0xF300904F));
        fakeResponse.add(new CategoryItem("Cat6", 0xFF00F6FF));
        fakeResponse.add(new CategoryItem("Cat7", 0xFF0087FF));
        fakeResponse.add(new CategoryItem("Cat8", 0xFF0034FF));
    }

    public CategoryPresenter(MainActivity mainActivity) {
        this.view = mainActivity;
    }

    ;

    //solicita os dados da view
    public void requestAll() {
        // chamar o metodo da view mainActivity
        this.view.showProgressBar();

        //chamar um http do servidor!!!!
        this.request();
    }

    public void onSuccess(List<CategoryItem> items) {
        //retorna os para a view
        view.showCategories(items);
    }

    public void onComplete() {
        this.view.hideProgressBar();
    }

    public void onError(String message) {
        this.view.showFailure(message);
    }

    private void request() {
        // busca os dados do model
        new Handler().postDelayed(() -> {
            try {
                onSuccess(fakeResponse);
                //throw new Exception("Falha ao buscar Categoraia");
            } catch (Exception e) {
                e.printStackTrace();
                onError(e.getMessage());
            } finally {
                onComplete();
            }
        }, 5000);
    }
}
