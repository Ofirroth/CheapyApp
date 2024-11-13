//package com.example.cheapy.viewModels;
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.ViewModel;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.example.cheapy.viewModels.CartViewModel;
//
//public class CartViewModelFactory implements ViewModelProvider.Factory {
//    private final String userToken;
//
//    public CartViewModelFactory(String userToken) {
//        this.userToken = userToken;
//    }
//
//    @NonNull
//    @Override
//    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
//        if (modelClass.isAssignableFrom(CartViewModel.class)) {
//            return (T) new CartViewModel(userToken);
//        }
//        throw new IllegalArgumentException("Unknown ViewModel class");
//    }
//}
