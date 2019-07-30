package com.example.maruf.tourMateApplication.Activity;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;


import com.example.maruf.tourMateApplication.Adapter.EventPagerAdapter;
import com.example.maruf.tourMateApplication.R;



public class EventDetailsActivity extends AppCompatActivity {
    private Toolbar eToolbar;
    private TabLayout eTabLayout;
    private ViewPager eventViewPager;
    private TabItem expenseTab;
    private TabItem memorableTab;
    private EventPagerAdapter eventPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Intent intent = getIntent();
        String toolbarName = intent.getStringExtra("eventName");
        String eventId = intent.getStringExtra("eventId");
        Double budget = intent.getDoubleExtra("eventBudget",0);
        Bundle bundle = new Bundle();
        bundle.putString("id",eventId);
        bundle.putDouble("budgetId",budget);
        eToolbar = findViewById(R.id.eventToolbar);
        eToolbar.setTitle(toolbarName);
        setSupportActionBar(eToolbar);
        eTabLayout = findViewById(R.id.eventTablayout);
        expenseTab = findViewById(R.id.tabExpense);
        memorableTab = findViewById(R.id.tabMemorable);
        eventViewPager = findViewById(R.id.viewPagerEvent);
        eventPagerAdapter = new EventPagerAdapter(getSupportFragmentManager(),eTabLayout.getTabCount(),bundle);
        eventViewPager.setAdapter(eventPagerAdapter);

        eTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                eventViewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 1){
                    eToolbar.setBackgroundColor(ContextCompat.getColor(EventDetailsActivity.this,R.color.colorMemorablePrimary));
                    eTabLayout.setBackgroundColor(ContextCompat.getColor(EventDetailsActivity.this,R.color.colorMemorablePrimary));
                    getWindow().setStatusBarColor(ContextCompat.getColor(EventDetailsActivity.this,R.color.colorMemorableDark));
                }else{
                    eToolbar.setBackgroundColor(ContextCompat.getColor(EventDetailsActivity.this,R.color.colorPrimary));
                    eTabLayout.setBackgroundColor(ContextCompat.getColor(EventDetailsActivity.this,R.color.colorPrimary));
                    getWindow().setStatusBarColor(ContextCompat.getColor(EventDetailsActivity.this,R.color.colorPrimaryDark));

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        eventViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(eTabLayout));
    }


}
