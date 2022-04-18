package com.integratedappsforlife.tracking.findmyphone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FindMyphone extends AppCompatActivity {
    ListView listv;
    public ArrayList<ListItem> list ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_myphone);
        listv =(ListView) findViewById (R.id.listViewFindMyPhone) ;
        SettingSaved settingSaved = new SettingSaved(this);
        listv.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("NwTry","Iam here");
                if (position!=listv.getCount()-1){
                Intent newpage;
                    Log.d("NwTry","Iam here2");
                    ListItem temp=list.get(position); // case select the item
                    if(settingSaved.loadPhoneNumber()!=null){
                        Log.d("NwTry","Iam here3");
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + temp.Detals));
                    intent.putExtra("sms_body", getString(R.string.sentLocationHead)+" "+getString(R.string.requestLocationCode)+" ("+settingSaved.loadPhoneNumber()+") "+getString(R.string.sentLocationTail));
                    startActivity(intent);}
            }
            }
        });
    }
    @Override
    protected void onResume() {
        UpdetListContact();
        super.onResume();

    }
    void     UpdetListContact(){
        listv.setAdapter(new VivzAdapter(this));
    }
    public void MessageSend(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_find_myphone, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.goback) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    Boolean IsDisplayMessage=false;
    class VivzAdapter extends BaseAdapter
    {
        Context context;
        VivzAdapter (Context context)
        { this.context=context;
            list=new ArrayList<ListItem>();
            if ( Build.VERSION.SDK_INT >= 23){
                if (ActivityCompat.checkSelfPermission((Activity)context, android.Manifest.permission.READ_CONTACTS) !=
                        PackageManager.PERMISSION_GRANTED ){
                    requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS,
                    },7);
                    return;
                }
            }

            getContacts();
            // get all contact to list
            /*ArrayList<ListItem> list_contact=new ArrayList<ListItem>() ;
            Cursor cursor = getContentResolver().query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,null, null);
            while (cursor.moveToNext()) {
                String name =cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
               list_contact.add(new ListItem(name, ManagmentOperations.FormatPhoneNumber(phoneNumber), R.drawable.user));


            }
            String IsFound=null;
            for (ListItem cs : list_contact) {

                    IsFound=//SettingSaved.WhoIFindIN.get(cs.Detals);  // for case who i could find list
                    ManagmentOperations.IsPhoneIn(cs.Detals,SettingSaved.UserTrackedNumber);

                if (IsFound!=null)
                    list.add(new ListItem(cs.Title, cs.Detals, R.drawable.dmap));

            }*/
            // add new one
            //list.add(new ListItem("Add$New", "no_desc", R.drawable.dmap));


//ask for add users to track you
if (list.size()==1  && IsDisplayMessage==false) {
    IsDisplayMessage=true;
    final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
    builder.setMessage(getResources().getString(R.string.settingPage))
            .setCancelable(false)
            .setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                    SettingSaved.WhoFindMeTag=1;
                    Intent newpage=new Intent(  getApplicationContext(),ContactList.class);
                    startActivity(newpage);
                    //finish();
                }
            })
            .setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                    dialog.cancel();
                }
            });
    final AlertDialog alert = builder.create();
    alert.show();
}

        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater inflater =( LayoutInflater ) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ListItem temp=list.get(i);

            if (temp.Title.equals("Add$New")){

                View row = inflater.inflate(R.layout.single_row_contact_add, viewGroup, false);
                //View row = inflater.inflate(android.R.layout.simple_list_item_2, viewGroup, false);
                Button buaddnew = (Button) row.findViewById(R.id.buaddnew);
                buaddnew.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SettingSaved.WhoFindMeTag=1;
                        Intent newpage=new Intent(  getApplicationContext(),ContactList.class);
                        startActivity(newpage);
                    //  finish();
                    }
                });

                return row;

            }
            else {
                View row = inflater.inflate(R.layout.single_row_conact, viewGroup, false);

                TextView title = (TextView) row.findViewById(R.id.uniqu1);
                TextView detals = (TextView) row.findViewById(R.id.uniqu2);
                ImageView image = (ImageView) row.findViewById(R.id.imageView);


                title.setText(temp.Title);
                detals.setText(temp.Detals);// it updated
                image.setImageResource(temp.ImageURL);

                return row;


            }



        }



    }


    //access to permsions

    //get acces to location permsion
   /* final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
*/


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 7:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    UpdetListContact();// init the contact list
                } else {
                    // Permission Denied
                    Toast.makeText( this,getResources().getString(R.string.DenailMessage) , Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    HashMap<String, String> contacts;
    private void getContacts() {
       /* contacts = new Contacts(this).getContactList();
        String IsFound=null;
        for (HashMap.Entry<String, String> entry : contacts.entrySet()) {
            Log.d("Heyaa1",entry.getKey());
            IsFound= ManagmentOperations.IsPhoneIn(entry.getKey(),SettingSaved.UserTrackedNumber);
            Log.d("anahena",IsFound + "y");
            if (IsFound!=null)
                list.add(new ListItem(entry.getValue(), entry.getKey(), R.drawable.dmap));
            //list_contact.add(new ListItem(entry.getValue(), ManagmentOperations.FormatPhoneNumber(entry.getKey())    ,R.drawable.user));*/
        /*}*/
        for(Map.Entry<String, String> entry: SettingSaved.WhoIFindIN.entrySet()){
            list.add(new ListItem(entry.getValue(), entry.getKey(), R.drawable.dmap));

        }
        list.add(new ListItem("Add$New","",0));

        Log.d("anahena", list.size()+ "y");
        Log.d("anahena", SettingSaved.UserTrackedNumber+" o%tah");
        // Log.d("Howaa",);
    }
}
