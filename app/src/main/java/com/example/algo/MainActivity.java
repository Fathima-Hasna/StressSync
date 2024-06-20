package com.example.algo;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;



public class MainActivity extends AppCompatActivity {
    private EditText taskname;
    private RatingBar rate1, rate2;
    private TextView rate1text, rate2text, currentdate, duedate, result,msg;
    private Button save, cal1, cal2, clear, next;
    private float givenDays, priority,leftDays;
    private ListView listView, prioritylist;
    ArrayList<String> items = new ArrayList<>();
    ArrayList<String> items2 = new ArrayList<>();
    ArrayList<TaskItem> taskItems = new ArrayList<>();
    ArrayAdapter<String> listadapter;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // calling all of the ID of the layout that are required in the calculations
        taskname = findViewById(R.id.Task_Name);
        rate1 = findViewById(R.id.E_ratingBar);
        rate2 = findViewById(R.id.I_ratingBar);
        rate1text = findViewById(R.id.Easy_rate);
        rate2text = findViewById(R.id.Imp_rate);
        currentdate = findViewById(R.id.Current_date_txt); //the date when task is given to the user
        duedate = findViewById(R.id.Due_date_txt);
        result = findViewById(R.id.Result);
        save = findViewById(R.id.Save);
        cal1 = findViewById(R.id.Cal_Btn);
        cal2 = findViewById(R.id.Cal_Btn2);
        msg = findViewById(R.id.msg);
        clear = findViewById(R.id.clr_btn);
        next = findViewById(R.id.stress);

        //for the list view
        listView = findViewById(R.id.listview);
        prioritylist = findViewById(R.id.priority_view);

       listadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(listadapter);
        prioritylist.setAdapter(listadapter);


        // button to open the calender
        cal1.setOnClickListener(v -> opencal());
        cal2.setOnClickListener(v -> opencal2());

       //setting the text for the rating bar
        float currentRating = rate1.getRating();

        rate1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rate1text.setText("Rating: " + rating);

            }
        });
        float currentRating2 = rate2.getRating();

        rate2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rate2text.setText("Rating: " + rating);
            }
        });
       // save but that will call the methods to calculate the priority and day left and display the result and add the task to the list view
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (areDatesValid()) { // Check if dates have been selected
                    totaldays();
                    dayleft();
                    calculate();
                    result.setText("Priority: " + priority);
                } else {
                    // Handle the case where dates are not valid (e.g., show a message)
                    result.setText("Please select both dates.");
                }
                //add the task to the list view
                String task = taskname.getText().toString();
                String priority1 = String.valueOf(priority);

                //check if the task is empty
                if (task.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a task name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(items.contains(task)) {
                    Toast.makeText(MainActivity.this, "Task already exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                items.add(task);
                items2.add(priority1);
                // Create TaskItem and add to list
                TaskItem newItem = new TaskItem(task, priority);
                taskItems.add(newItem);
                // Create and set adapter (do this only once, outside the onClick, ideally in onCreate)
                TaskItemAdapter taskItemAdapter = new TaskItemAdapter(MainActivity.this, taskItems);
                listView.setAdapter(taskItemAdapter);

                // Notify adapter of data change
                taskItemAdapter.notifyDataSetChanged();
                taskname.setText("");
                 /*  TaskItemAdapter taskItemAdapter = new TaskItemAdapter(MainActivity.this, taskItems);
                listView.setAdapter(taskItemAdapter);
                TaskItem newItem = new TaskItem(task, priority);*/
                /*    taskItems.add(newItem);*/

            }
        });
        //to go to the stress factor page
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,stressfactor.class);
                startActivity(intent);
            }
        });
        //clear button that will clear all the text fields
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskname.setText("");
                rate1.setRating(0);
                rate2.setRating(0);
                currentdate.setText("");
                duedate.setText("");
                result.setText("");
                msg.setText("");
            }

    });
        }
    private void opencal () {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        // for the date open date picker dialog
        DatePickerDialog cal = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                currentdate.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(month) + "/" + String.valueOf(year));
                //selectedDate = getDateFromDatePicker(year, month, dayOfMonth);

            }
        }, year, month, dayOfMonth);
        cal.show();
    }
    private void opencal2 () {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        // for the date open date picker dialog
        DatePickerDialog cal = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                duedate.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(month) + "/" + String.valueOf(year));
                //selectedDate = getDateFromDatePicker(year, month, dayOfMonth);

            }
        }, year, month, dayOfMonth);
        cal.show();
    }
    private void calculate () {
        priority = rate1.getRating() * rate2.getRating() * (givenDays / leftDays);
        if (priority < 5) {
            result.setTextColor(getResources().getColor(R.color.red));
            msg.setText("This is a high priority task. Please complete it at your earliest convenience.");
        }
        else if (priority < 10) {
            result.setTextColor(getResources().getColor(R.color.yellow));
            msg.setText("complete the task, This is a medium priority task");
        }
        else {
            result.setTextColor(getResources().getColor(R.color.green));
            msg.setText("complete the task,This is a low priority task");
        }


    }
    private boolean areDatesValid() {
        String d1 = currentdate.getText().toString();
        String d2 = duedate.getText().toString();
        return !d1.isEmpty() && !d2.isEmpty();
    }
    private void dayleft () {
        String d3 = duedate.getText().toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy"); // Adjusted format
        LocalDate d4 = LocalDate.now();
        Period period = Period.between(d4, LocalDate.parse(d3, formatter));
        leftDays = period.getDays();
    }
    private void totaldays() {
        String d1 = currentdate.getText().toString();
        String d2 = duedate.getText().toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy"); // Adjusted format
        LocalDate currentDate = LocalDate.parse(d1, formatter);
        LocalDate dueDate = LocalDate.parse(d2, formatter);
        givenDays = ChronoUnit.DAYS.between(dueDate,currentDate);
    }

   class TaskItem {
        String taskName;
        float priority;

        public TaskItem(String taskName, float priority) {
            this.taskName = taskName;
            this.priority = priority;
        }
    }
  class TaskItemAdapter extends ArrayAdapter<TaskItem> {
      public TaskItemAdapter(Context context, ArrayList<TaskItem> items) {
          super(context, 0, items);
      }

      @Override
      public View getView(int position, View convertView, ViewGroup parent) {
          TaskItem item = getItem(position);

          if (convertView == null) {
              // Use a built-in layout for simplicity (you can customize this)
              convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
          }

          TextView taskNameTextView = convertView.findViewById(android.R.id.text1);
          TextView priorityTextView = convertView.findViewById(android.R.id.text2);

          taskNameTextView.setText(item.taskName);
          priorityTextView.setText("Priority: " + item.priority);


          return convertView;
        }
    }
}