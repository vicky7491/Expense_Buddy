package dev.nitish.expensebuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import dev.nitish.expensebuddy.databinding.ActivityDashboardBinding;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity implements OnItemsClick{
    ActivityDashboardBinding binding;  // Declare the binding variable
    private ExpensesAdapter expensesAdapter;
    private long income = 0, expense = 0;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        expensesAdapter = new ExpensesAdapter(this,this);
        binding.recycler.setAdapter(expensesAdapter);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
         intent = new Intent(Dashboard.this,ExpenseActivity.class);

    binding.addIncome.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            intent.putExtra("type", "Income");
            startActivity(intent);
        }
    });binding.addExpense.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            intent.putExtra("type", "Expense");
            startActivity(intent);
        }
    });
    }

    @Override
    protected void onResume() {
        super.onResume();
        income=0;
        expense=0;
        getData();
    }

    private void getData() {
        FirebaseFirestore
                .getInstance()
                .collection("expenses")
                .whereEqualTo("uid", FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        expensesAdapter.clear();

                      List<DocumentSnapshot> dsList= queryDocumentSnapshots.getDocuments();
                      for(DocumentSnapshot ds:dsList){
                          ExpenseModel expenseModel = ds.toObject(ExpenseModel.class);
                          if(expenseModel.getType().equals("Income")){
                              income+= expenseModel.getAmount();
                          }else{
                              expense+= expenseModel.getAmount();
                          }
                          expensesAdapter.add(expenseModel);
                      }
                      setUpGraph();
                    }
                });
    }

    private void setUpGraph() {
        List<PieEntry> pieEntryList = new ArrayList<>();
        List<Integer> colorsList = new ArrayList<>();
        if (income!=0){
            pieEntryList.add(new PieEntry(income, "Income"));
            colorsList.add(getResources().getColor(R.color.green));
        }
        if (expense!=0){
            pieEntryList.add(new PieEntry(expense, "Expense"));
            colorsList.add(getResources().getColor(R.color.red));
        }
        PieDataSet pieDataSet = new PieDataSet(pieEntryList, String.valueOf(income-expense));
        pieDataSet.setColors(colorsList);
        pieDataSet.setValueTextColor(getResources().getColor(R.color.white));
        PieData pieData = new PieData(pieDataSet);

        binding.pieChart.setData(pieData);
        binding.pieChart.invalidate();

    }

    @Override
    public void onClick(ExpenseModel expenseModel) {
        intent.putExtra("model", expenseModel);
        startActivity(intent);
    }
}