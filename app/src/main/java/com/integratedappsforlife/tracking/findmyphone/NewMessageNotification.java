package com.integratedappsforlife.tracking.findmyphone;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.core.app.NotificationCompat;

/**
 * Helper class for showing and canceling new message
 * notifications.
 * <p/>
 * This class makes heavy use of the {@link NotificationCompat.Builder} helper
 * class to create notifications in a backward-compatible way.
 */
public class NewMessageNotification {
    /**
     * The unique identifier for this type of notification.
     */
    private static final String NOTIFICATION_TAG = "NewMessage";
    public static String addAction1 = "ic_action_stat_share";
    public static String addAction2 = null;
    public static String text1;
    public static String text2;
    /**
     * Shows the notification, or updates a previously shown notification of
     * this type, with the given parameters.
     * <p/>
     * TODO: Customize this method's arguments to present relevant content in
     * the notification.
     * <p/>
     * TODO: Customize the contents of this method to tweak the behavior and
     * presentation of new message notifications. Make
     * sure to follow the
     * <a href="https://developer.android.com/design/patterns/notifications.html">
     * Notification design guidelines</a> when doing so.
     *
     * @see #cancel(Context)
     */
    public static void notify(final Context context,
                              final String exampleString, String DetailsMessge, final int number, final String showedMessage,final String name,final String phoneNu) {
        final Resources res = context.getResources();

        // This image is used as the notification's large icon (thumbnail).
        // TODO: Remove this if your notification has no relevant thumbnail.
        final Bitmap picture = BitmapFactory.decodeResource(res, R.drawable.dmapsmall);
       // ContactList cl= new ContactList();
//String USerName=cl.GetNAmeByphone(exampleString);

        final String ticker =getContactName(context, exampleString);
        final String title = res.getString(
                R.string.new_message_notification_title_template, exampleString);
        final String text = showedMessage;
        Intent imap;
        Bundle extras = new Bundle();
         extras.putString(Intent.EXTRA_TEXT, DetailsMessge);
         text1 = "";
         text2 = "";
         addAction1 = "ic_action_stat_share";
         addAction2 = "Deny";
         imap = new Intent(context, MapDisplay.class);
         imap.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         Resources resources = context.getResources();
         final int resourceId1 = resources.getIdentifier(addAction1, "drawable",
                context.getPackageName());
        final int resourceId2 = resources.getIdentifier(addAction2, "drawable",
                context.getPackageName());
        //Drawable fin123  = resources.getDrawable(resourceId1);
        //Drawable fin12 = resources.getDrawable(resourceId2);
       // context.startActivity(imap);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)

                // Set appropriate defaults for the notification light, sound,
                // and vibration.
                .setDefaults(Notification.DEFAULT_ALL)

                        // Set required fields, including the small icon, the
                        // notification title, and text.
                .setSmallIcon(R.drawable.findmyphonep)
                .setContentTitle(title)
                .setContentText(text)

                        // All fields below this line are optional.

                        // Use a default priority (recognized on devices running Android
                        // 4.1 or later)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                        // Provide a large icon, shown with the notification in the
                        // notification drawer on devices running Android 3.0 or later.
                .setLargeIcon(picture)

                        // Set ticker text (preview) information for this notification.
                .setTicker(ticker)

                        // Show a number. This is useful when stacking notifications of
                        // a single type.
                .setNumber(number)

                        // If this notification relates to a past or upcoming event, you
                        // should set the relevant time information using the setWhen
                        // method below. If this call is omitted, the notification's
                        // timestamp will by set to the time at which it was shown.
                        // TODO: Call setWhen if this notification relates to a past or
                        // upcoming event. The sole argument to this method should be
                        // the notification timestamp in milliseconds.
                        //.setWhen(...)

                        // Set the pending intent to be initiated when the user touches
                        // the notification.
                .setContentIntent(
                        PendingIntent.getActivity(
                                context,
                                0,
                                imap,
                                PendingIntent.FLAG_UPDATE_CURRENT))

                        // Show expanded text content on devices running Android 4.1 or
                        // later.
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(text)
                        .setBigContentTitle(title)
                        .setSummaryText("--"))

                        // Example additional actions for this notification. These will
                        // only show on devices running Android 4.1 or later, so you
                        // should ensure that the activity in this notification's
                        // content intent provides access to the same actions in
                        // another way.
                /*Wrong 1st argument type. Found: 'android.graphics.drawable.Drawable', required: 'int' less...

addAction() in Builder cannot be applied to:
Expected Parameters:
Actual Arguments:

icon:
int
fin123  (android.graphics.drawable.Drawable)
title:
CharSequence
text1  
intent:
PendingIntent
PendingIntent...FLAG_UPDATE_CURRENT) */


                .addAction(resourceId1
                        , text1,
                       /* PendingIntent.getActivity(
                                context,
                                0,
                                Intent.createChooser(new Intent(Intent.ACTION_SEND)
                                        .setType("text/plain")
                                        .putExtras(extras), "Select"),
                                PendingIntent.FLAG_UPDATE_CURRENT)*/null)
                .addAction(resourceId2,
                        text2,null
                )


                        // Automatically dismiss the notification when it is touched.
                .setAutoCancel(true);

        notify(context, builder.build());
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 0, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
    }
// get phone from number
public static String getContactName(Context context, String phoneNumber) {

    String contactName = phoneNumber;
    Cursor cursor =context. getContentResolver().query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,null, null);
    while (cursor.moveToNext()) {

        String phoneNumberLis = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        if((phoneNumber.contains(phoneNumberLis) )||(phoneNumberLis.contains(phoneNumber) ))
        {   contactName =cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
break;

    }}

    return contactName;
}
    /**
     * Cancels any notifications of this type previously shown using
     * {@link #notify(Context, String, int)}.
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, 0);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }
    }
}