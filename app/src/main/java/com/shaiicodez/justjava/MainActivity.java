package com.shaiicodez.justjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    int quantity;
    boolean hasWhippedCream, hasChocolate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {

        //User name
        EditText nameEditText = findViewById(R.id.et_name);
        String username = nameEditText.getText().toString();
        Log.v("MainActivity", "The username is " + username);

        // Topping: Whipped Cream
        CheckBox whippedCreamCheckBox = findViewById(R.id.cb_whipped_cream);
        boolean creamChecked = whippedCreamCheckBox.isChecked();
        hasWhippedCream = creamChecked;
        Log.v("MainActivity", "Toppings: WhippedCream:  " + hasWhippedCream);

        // Topping: Chocolate
        CheckBox chocolateCheckBox = findViewById(R.id.cb_chocolate);
        boolean chocolateChecked = chocolateCheckBox.isChecked();
        hasChocolate = chocolateChecked;
        Log.v("MainActivity", "Toppings: Chocolate:  " + hasChocolate);

        //price
        int price = calculatePrice(hasWhippedCream, hasChocolate);
        Log.v("MainActivity", "The price is " + price);

        // print order summary
        String orderSummary = createOrderSummary(username, price, hasWhippedCream, hasChocolate);
        String subject = "My Just Java Order for " + username;
        composeEmail(subject, orderSummary);

    }

    public void composeEmail(String subject, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        //check if any app in the device can handle this intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Calculates the price of the order based on the current quantity.
     *
     * @param hasWhippedCream
     * @param hasChocolate
     * @return the price
     */
    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {
        // Price of 1 cup of coffee
        int base_price = 14;

        // Add SR. 2 if user wants whipped cream
        if (hasWhippedCream) {
            base_price = base_price + 2;
        }

        // Add SR. 4 if user wants chocolate
        if (hasChocolate) {
            base_price = base_price + 4;
        }

        return base_price * quantity;
    }


    // javadoc comment

    /**
     * This method creates summary of the order.
     *
     * @param username
     * @param price           of the order
     * @param hasChocolate
     * @param hasWhippedCream
     * @return text summary
     */
    public String createOrderSummary(String username, int price, boolean hasWhippedCream, boolean hasChocolate) {
        String priceMessage = "" + getString(R.string.email_name, username);
        priceMessage += "\n" + getString(R.string.email_add_whipped_cream, hasWhippedCream);
        priceMessage += "\n" + getString(R.string.email_add_chocolate, hasChocolate);
        priceMessage += "\n" + getString(R.string.email_quantity, quantity);
        priceMessage += "\n" + getString(R.string.email_total_tag, NumberFormat.getCurrencyInstance().format(price));
        priceMessage += "\n" + getString(R.string.email_thank_you);
        return priceMessage;
    }


    // ======================== QUANTITY BUTTONS =================================

    /**
     * This method increases the coffee quantity.
     */
    public void increment(View view) {
        if (quantity == 100) {
            // Show an error message as a toast
            Toast.makeText(this, "You cannot have more than 100 coffees", Toast.LENGTH_SHORT).show();
            // Exit this method early because there's nothing left to do
            return;
        }

        quantity = quantity + 1;
        displayQuantity(quantity);
    }

    /**
     * This method decreases the coffee quantity.
     */
    public void decrement(View view) {
        if (quantity == 1) {
            // Show an error message as a toast
            Toast.makeText(this, "You cannot have less than 1 coffee", Toast.LENGTH_SHORT).show();
            // Exit this method early because there's nothing left to do
            return;
        }

        quantity = quantity - 1;
        displayQuantity(quantity);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.tv_quantity);
        quantityTextView.setText("" + number);
    }


}//end class