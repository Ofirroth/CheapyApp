//package com.example.cheapy;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextWatcher;
//import android.widget.GridView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.cheapy.Cart.CartActivity;
//import com.example.cheapy.Dao.AppDB;
//import com.example.cheapy.Dao.CategoryDao;
//import com.example.cheapy.Dao.ItemDao;
//import com.example.cheapy.Home_page.HomePageActivity;
//import com.example.cheapy.adapters.CategoryAdapter;
//import com.example.cheapy.adapters.ItemAdapter;
//import com.example.cheapy.databinding.ActivityCategoriesBinding;
//import com.example.cheapy.databinding.CategoryItemsBinding;
//import com.example.cheapy.databinding.HomePageBinding;
//import com.example.cheapy.entities.Category;
//import com.example.cheapy.entities.Item;
//import com.example.cheapy.viewModels.CategoryViewModel;
//import com.example.cheapy.viewModels.ItemViewModel;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class CategoryItemsActivity extends AppCompatActivity implements TextWatcher {
//    String activeUserName;
//    private CategoryItemsBinding binding;
//
//    String userToken;
//
//    private Boolean isNightMode = null;
//    private AppDB db;
//    private CategoryDao categoryDao;
//    private CategoryAdapter categoryAdapter;
//
//    private CategoryViewModel categoryViewModel;
//    private List<Category> categories;
//    private ItemDao itemDao;
//    private ItemAdapter itemAdapter;
//
//    private ItemViewModel itemViewModel;
//    private List<Item> items;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = CategoryItemsBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        Intent intent = getIntent();
//
//        if (intent != null) {
//            activeUserName = getIntent().getStringExtra("activeUserName");
//            userToken = getIntent().getStringExtra("token");
//        }
//        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;
//        bottomNavigationView.setOnItemSelectedListener(item -> {
//            int itemId = item.getItemId();
//
//            if (itemId == R.id.navigationMyProfile) {
//                Intent profileIntent = new Intent(CategoryItemsActivity.this, HomePageActivity.class);
//                startActivity(profileIntent);
//                return true;
//            } else if (itemId == R.id.navigationHome) {
//                return true;
//            } else if (itemId == R.id.navigationCheckout) {
//                Intent cartIntent = new Intent(CategoryItemsActivity.this, CartActivity.class);
//                startActivity(cartIntent);
//                return true;
//            } else {
//                return false;
//            }
//        });
//
//        this.db = DatabaseManager.getDatabase();
//        this.categoryDao = db.categoryDao();
//        this.itemDao = db.itemDao();
//        this.categoryViewModel = new CategoryViewModel(this.userToken);
//        this.itemViewModel = new ItemViewModel(this.userToken);
//        this.categories = new ArrayList<>();
//        this.items = new ArrayList<>();
//        binding.searchEditText.addTextChangedListener(this);
//        binding.searchEditText.addTextChangedListener(this);
//
////        GridView lvCategoriesItems = binding.gridViewCategoriesItems;
////        this.itemAdapter = new ItemAdapter(getApplicationContext(), this.items);
////        this.itemViewModel.().observe(this, adapter::setCategories);
////        lvCategories.setAdapter(this.adapter);
//    }
//}
//
