package com.mysampleapp.demo.nosql;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;

import java.util.Set;

public class DemoNoSQLWafflesResult implements DemoNoSQLResult {
    private static final int KEY_TEXT_COLOR = 0xFF333333;
    private final WafflesDO result;

    DemoNoSQLWafflesResult(final WafflesDO result) {
        this.result = result;
    }
    @Override
    public void updateItem() {
        final DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        final String originalValue = result.getOpt1();
        result.setOpt1(DemoSampleDataGenerator.getRandomSampleString("opt1"));
        try {
            mapper.save(result);
        } catch (final AmazonClientException ex) {
            // Restore original data if save fails, and re-throw.
            result.setOpt1(originalValue);
            throw ex;
        }
    }

    @Override
    public void deleteItem() {
        final DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        mapper.delete(result);
    }

    public WafflesDO getResult(){
        return result;
    }

    private void setKeyTextViewStyle(final TextView textView) {
        textView.setTextColor(KEY_TEXT_COLOR);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(dp(5), dp(2), dp(5), 0);
        textView.setLayoutParams(layoutParams);
    }

    /**
     * @param dp number of design pixels.
     * @return number of pixels corresponding to the desired design pixels.
     */
    private int dp(int dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
    private void setValueTextViewStyle(final TextView textView) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(dp(15), 0, dp(15), dp(2));
        textView.setLayoutParams(layoutParams);
    }

    private void setKeyAndValueTextViewStyles(final TextView keyTextView, final TextView valueTextView) {
        setKeyTextViewStyle(keyTextView);
        setValueTextViewStyle(valueTextView);
    }

    private static String bytesToHexString(byte[] bytes) {
        final StringBuilder builder = new StringBuilder();
        builder.append(String.format("%02X", bytes[0]));
        for(int index = 1; index < bytes.length; index++) {
            builder.append(String.format(" %02X", bytes[index]));
        }
        return builder.toString();
    }

    private static String byteSetsToHexStrings(Set<byte[]> bytesSet) {
        final StringBuilder builder = new StringBuilder();
        int index = 0;
        for (byte[] bytes : bytesSet) {
            builder.append(String.format("%d: ", ++index));
            builder.append(bytesToHexString(bytes));
            if (index < bytesSet.size()) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    @Override
    public View getView(final Context context, final View convertView, int position) {
        final LinearLayout layout;
        final TextView resultNumberTextView;
        final TextView qIdKeyTextView;
        final TextView qIdValueTextView;
        final TextView termKeyTextView;
        final TextView termValueTextView;
        final TextView opt1KeyTextView;
        final TextView opt1ValueTextView;
        final TextView opt2KeyTextView;
        final TextView opt2ValueTextView;
        final TextView ownerIdKeyTextView;
        final TextView ownerIdValueTextView;
        final TextView vote1KeyTextView;
        final TextView vote1ValueTextView;
        final TextView vote2KeyTextView;
        final TextView vote2ValueTextView;
        if (convertView == null) {
            layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            resultNumberTextView = new TextView(context);
            resultNumberTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            layout.addView(resultNumberTextView);


            qIdKeyTextView = new TextView(context);
            qIdValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(qIdKeyTextView, qIdValueTextView);
            layout.addView(qIdKeyTextView);
            layout.addView(qIdValueTextView);

            termKeyTextView = new TextView(context);
            termValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(termKeyTextView, termValueTextView);
            layout.addView(termKeyTextView);
            layout.addView(termValueTextView);

            opt1KeyTextView = new TextView(context);
            opt1ValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(opt1KeyTextView, opt1ValueTextView);
            layout.addView(opt1KeyTextView);
            layout.addView(opt1ValueTextView);

            opt2KeyTextView = new TextView(context);
            opt2ValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(opt2KeyTextView, opt2ValueTextView);
            layout.addView(opt2KeyTextView);
            layout.addView(opt2ValueTextView);

            ownerIdKeyTextView = new TextView(context);
            ownerIdValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(ownerIdKeyTextView, ownerIdValueTextView);
            layout.addView(ownerIdKeyTextView);
            layout.addView(ownerIdValueTextView);

            vote1KeyTextView = new TextView(context);
            vote1ValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(vote1KeyTextView, vote1ValueTextView);
            layout.addView(vote1KeyTextView);
            layout.addView(vote1ValueTextView);

            vote2KeyTextView = new TextView(context);
            vote2ValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(vote2KeyTextView, vote2ValueTextView);
            layout.addView(vote2KeyTextView);
            layout.addView(vote2ValueTextView);
        } else {
            layout = (LinearLayout) convertView;
            resultNumberTextView = (TextView) layout.getChildAt(0);

            qIdKeyTextView = (TextView) layout.getChildAt(1);
            qIdValueTextView = (TextView) layout.getChildAt(2);

            termKeyTextView = (TextView) layout.getChildAt(3);
            termValueTextView = (TextView) layout.getChildAt(4);

            opt1KeyTextView = (TextView) layout.getChildAt(5);
            opt1ValueTextView = (TextView) layout.getChildAt(6);

            opt2KeyTextView = (TextView) layout.getChildAt(7);
            opt2ValueTextView = (TextView) layout.getChildAt(8);

            ownerIdKeyTextView = (TextView) layout.getChildAt(9);
            ownerIdValueTextView = (TextView) layout.getChildAt(10);

            vote1KeyTextView = (TextView) layout.getChildAt(11);
            vote1ValueTextView = (TextView) layout.getChildAt(12);

            vote2KeyTextView = (TextView) layout.getChildAt(13);
            vote2ValueTextView = (TextView) layout.getChildAt(14);
        }

        resultNumberTextView.setText(String.format("#%d", + position+1));
        qIdKeyTextView.setText("qId");
        qIdValueTextView.setText("" + result.getQId().longValue());
        termKeyTextView.setText("term");
        termValueTextView.setText("" + result.getTerm().longValue());
        opt1KeyTextView.setText("opt1");
        opt1ValueTextView.setText(result.getOpt1());
        opt2KeyTextView.setText("opt2");
        opt2ValueTextView.setText(result.getOpt2());
        ownerIdKeyTextView.setText("ownerId");
        ownerIdValueTextView.setText(result.getOwnerId());
        vote1KeyTextView.setText("vote1");
        vote1ValueTextView.setText("" + result.getVote1().longValue());
        vote2KeyTextView.setText("vote2");
        vote2ValueTextView.setText("" + result.getVote2().longValue());
        return layout;
    }

    @Override
    public String toString(){
        String string = "{";


        return "";
    }
}
