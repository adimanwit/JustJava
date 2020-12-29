package com.example.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static java.text.NumberFormat.getCurrencyInstance;

public class MainActivity extends AppCompatActivity {

    int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
/*
*  Decrement quantity of coffees
* */
    public void decrement(View view) {
        if (quantity == 1) {
            return;
        }
        quantity = quantity - 1;
        displayQuantity(quantity);
    }

//  Increment quantity of coffees
    public void increment(View view) {
        if (quantity == 100) {
//              Stops at 100 coffees and shows Toast message
            Toast.makeText(this,
                    "You cannot have more than 100 coffees",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity + 1;
        displayQuantity(quantity);
//         Price changes from int to String
    }

    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        int basePrice = 5;

        if (addWhippedCream) { // Adds whipped cream to basePrice
            basePrice = basePrice + 1;
        }

        if (addChocolate) { // Adds chocolate to basePrice
            basePrice = basePrice + 2;
        }

        return quantity * basePrice;
    }


    public void submitOrder(View view) {

        // Figure out if user wants Whipped coffee
        CheckBox whippedCreamCheckbox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckbox.isChecked();

        // Figure out if user wants Chocolate
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        // Find out user`s name
        EditText yourNameEdittext = (EditText) findViewById(R.id.your_name_edittext);
        String yourName = yourNameEdittext.getText().toString();

        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String priceMessage = createOrderSummary(price, hasWhippedCream, hasChocolate, yourName);

        // Activate app selector for email
        Intent selectorIntent = new Intent(Intent.ACTION_SENDTO);
        selectorIntent.setData(Uri.parse("mailto:"));
        //Sends email with summary
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + " " + getString(R.string.order_for ) + yourName);
        emailIntent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        emailIntent.setSelector( selectorIntent );

        //Checks if you have email app
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            // Executes intend and Sends email
            startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email_using) + ":"));
        }
        else {
            // Shows toast if it cannot find an app
            Toast.makeText(this, "You don't have any email apps to contact us.", Toast.LENGTH_SHORT).show();
        }
    }

    private String createOrderSummary(int price, boolean addWhippedCream, boolean addChocolate, String yourName) {
        String priceMessage = getString(R.string.order_summary_name) + ": " + yourName;
        // += Will copy last line and add new text
        priceMessage += "\n" + getString(R.string.quantity) + ": " + quantity;
        priceMessage += "\n" + getString(R.string.add_whipped_cream) + "? " + addWhippedCream;
        priceMessage += "\n" + getString(R.string.add_chocolate) + "? " + addChocolate;
        priceMessage += "\n" + getString(R.string.total) + ": " + getCurrencyInstance().format(price);
        priceMessage += "\n" + getString(R.string.thank_you);
        return priceMessage;
    }

    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        // Changes quantity from int to String and displays quantity
        quantityTextView.setText(String.valueOf(number));
    }
}