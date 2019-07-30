package com.example.maruf.tourMateApplication.Fragments;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.maruf.tourMateApplication.Adapter.ExpenseListAdapter;
import com.example.maruf.tourMateApplication.OtherClass.DatabaseRef;
import com.example.maruf.tourMateApplication.ProjoPackage.Expenses;
import com.example.maruf.tourMateApplication.R;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
public class ExpensesFragment extends Fragment {


public ExpensesFragment() {
    // Required empty public constructor
}

private FirebaseAuth firebaseAuth;
private String userId;
private BottomSheetDialog bottomSheetDialog;
private FloatingActionButton openBottomsheetExpenseBtn;
private EditText details,amount;
private  Button expenseBtn;
private String eventId;
double budget;
private RecyclerView recyclerView;
private ProgressDialog progressDialog;
private List<Expenses> expensesList;

private ExpenseListAdapter expenseAdapter;


@Override
public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                         Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_expenses, container, false);
    expensesList = new ArrayList<>();
    Bundle bundle = getArguments();
    eventId = bundle.getString("id");
    budget = bundle.getDouble("budgetId");
    firebaseAuth = FirebaseAuth.getInstance();
    userId = firebaseAuth.getCurrentUser().getUid();
    recyclerView = view.findViewById(R.id.expensesRecyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    progressDialog = new ProgressDialog(getActivity(),R.style.AppTheme_Dark_Dialog);
    progressDialog.setIndeterminate(true);
    progressDialog.setMessage("Loading");
    progressDialog.setCanceledOnTouchOutside(false);
    progressDialog.show();
    loadEventListFromDatabase();

    openBottomsheetExpenseBtn = view.findViewById(R.id.openBottomSheetExpense);
    openBottomsheetExpenseBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            bottomSheetDialog = new BottomSheetDialog(getActivity());
            View sheetview = getLayoutInflater().inflate(R.layout.bottom_sheet_expense,null);
            bottomSheetDialog.setContentView(sheetview);
            bottomSheetDialog.show();
            details = sheetview.findViewById(R.id.expenseDetailET);
            amount = sheetview.findViewById(R.id.expenseAmountET);
            expenseBtn = sheetview.findViewById(R.id.addExpense);
            expenseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validateAndSend();
                }
            });
        }
    });
    return view;

}

    private void loadEventListFromDatabase() {
    DatabaseRef.userRef.child(userId).child("Events").child(eventId).child("Expense").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            expensesList.clear();
            if(!dataSnapshot.exists()){
                progressDialog.dismiss();
            }else{
                for (DataSnapshot d : dataSnapshot.getChildren()){
                    Expenses expenses = d.getValue(Expenses.class);
                    expensesList.add(expenses);
                    progressDialog.dismiss();


                }
                expenseAdapter = new ExpenseListAdapter(getActivity(),expensesList);
                recyclerView.setAdapter(expenseAdapter);
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
    }

    private void validateAndSend() {
    String expenseDetails = details.getText().toString();
    String ammount = amount.getText().toString();
    try {
        double expenseAmount = Double.parseDouble(ammount);
        double balance = budget;
        if(expenseAmount <= balance){
            final double rest = balance-expenseAmount;
            DatabaseRef.userRef.child(userId).child("Events").child(eventId).child("estimatedBudget").setValue(rest).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        bottomSheetDialog.dismiss();
                        Toasty.info(getActivity(),"Expense added balance:"+rest+" BDT",Toast.LENGTH_SHORT,false).show();
                    }else {
                        bottomSheetDialog.dismiss();
                        Toasty.error(getActivity(),task.getException().getMessage(),Toast.LENGTH_SHORT,false).show();

                    }

                }
            });
            Expenses expenses = new Expenses(expenseDetails,expenseAmount);
            DatabaseRef.userRef.child(userId).child("Events").child(eventId).child("Expense").push().setValue(expenses).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                    }else {
                        Toasty.warning(getActivity(),task.getException().getMessage(),Toast.LENGTH_SHORT,false).show();
                    }
                }
            });
        }else {
            Toasty.warning(getActivity(),"insufficient Balance",Toast.LENGTH_SHORT,false).show();
        }

    }catch (Exception e){
        Toasty.error(getActivity(),e.getMessage(),Toast.LENGTH_SHORT,false).show();

    }



}


}
