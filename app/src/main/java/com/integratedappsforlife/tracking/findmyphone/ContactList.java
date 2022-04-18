package com.integratedappsforlife.tracking.findmyphone;

import android.Manifest;
import android.app.AlertDialog;
import android.app.SearchManager;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.integratedappsforlife.tracking.findmyphone.util.Contacts;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactList extends AppCompatActivity {
    ListView listv;
    public ArrayList<ListItem> list;
    private VivzAdapter adpter;
    String SearchText = "%";
    Context x  = this;
    public String phone1;
    public String message1;
    public String phone2;
    public String message2;
    ArrayList<ListItem> list_contact;
    HashMap<String, String> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_list);
        list = new ArrayList<ListItem>();
//ask for permsion
        //getlocation();


        listv = (ListView) findViewById(R.id.listView);
        adpter = new VivzAdapter(this, list);
        UpdetListContact();
        listv.setAdapter(adpter);

        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                ListItem temp = list.get(position); // case select the item

                if (temp.ImageURL == R.drawable.gpson)  // case
                {  // chaneg to off we delect it
                    if (SettingSaved.WhoFindMeTag == 0) // case who find me
                        SettingSaved.WhoFindMeIN.remove(ManagmentOperations.IsPhoneIn(temp.Detals,SettingSaved.UserPhoneNumber));
                    else
                        SettingSaved.WhoIFindIN.remove(ManagmentOperations.IsPhoneIn(temp.Detals,SettingSaved.UserTrackedNumber));
                } else  /// in case add item to list
                {

                    if (SettingSaved.WhoFindMeTag == 0) // case who find me
                    {
                        SettingSaved.WhoFindMeIN.put(temp.Detals, temp.Title);
                        MsgSent(temp.Detals, "Hi, I Added you as person who could find me , you could find me use app "+getResources().getString(R.string.appShortLink));
                    } else {
                        SettingSaved.WhoIFindIN.put(temp.Detals, temp.Title);
                        MsgSent1(temp.Detals, "Hi, I Added you as person who could I find, please allow me as person who could find you in app, "+getResources().getString(R.string.appShortLink));
                    }
                }
                SaveData();  // save change

            }
        });
    }


    private void SaveData() {
        SettingSaved settingSaved = new SettingSaved(this);
        settingSaved.SaveData();
        settingSaved.LoadData(); // load data
        UpdetListContact();// init the contact list
        // adpter.notifyDataSetChanged();
    }

    SearchView searchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_list, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //final Context co=this;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0)
                    SearchText = "%" + newText + "%";
                else
                    SearchText = "%";

                UpdetListContact();

                return false;
            }
        });
        //   searchView.setOnCloseListener(this);
        return true;
    }

    void MsgSent(final String Phone, final String Message) {
        phone1 = Phone;
        message1 = Message;
       final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setMessage(getResources().getString(R.string.smsSend))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + Phone));
                        intent.putExtra("sms_body", Message);
                        startActivity(intent);

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
    void MsgSent1(final String Phone, final String Message) {
        phone2 = Phone;
        message2 = Message;

            final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setMessage(getResources().getString(R.string.smsSend))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + Phone));
                        intent.putExtra("sms_body", Message);
                        startActivity(intent);
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

    void messageSend(String msg) {

       Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.goback) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    class VivzAdapter extends BaseAdapter
    {


        Context context;
        ArrayList<ListItem> list;
        VivzAdapter (Context c,ArrayList<ListItem> list)
        { context=c;
            this.list=list;
        }


        @Override
        public int getCount() {
            return this.list.size();
        }

        @Override
        public Object getItem(int i) {
            return this.list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater inflater =( LayoutInflater ) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row= inflater.inflate( R.layout.single_row_conact,viewGroup,false );
            TextView title=(TextView) row.findViewById(R.id.uniqu1);
            TextView detals=(TextView) row.findViewById(R.id.uniqu2);
            ImageView image =(ImageView) row.findViewById(R.id.imageView);
            ListItem temp=this.list.get(i);

            Log.d("NewTry",temp.Title+"Iam here");
            title.setText(temp.Title);
            detals.setText(temp.Detals);// it updated
            image.setImageResource(temp.ImageURL);

            return row;






        }



    }

    void UpdetListContact( ){
        try{
          if ( Build.VERSION.SDK_INT >= 23){
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) !=
                        PackageManager.PERMISSION_GRANTED ){
                            requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS,
                            },125);
                    return;

                }
            }
       // messageSend("in UpdetListContact everything is ok");
        list.clear();
        list_contact=new ArrayList<ListItem>() ;
         getContacts();
        // get all contact to list
        /*String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" like'" + SearchText +"'";
        ArrayList<ListItem> list_contact=new ArrayList<ListItem>() ;
        Cursor cursor = getContentResolver().query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, selection,null,  "upper("+ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC");
        while (cursor.moveToNext()) {
            String name =cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            boolean foundIt = false;
            for (ListItem ir : list_contact) {
                if (ManagmentOperations.FormatPhoneNumber(phoneNumber).equals(ir.getPhoneNumber())) {
                    foundIt = true;
                    break;
                }
            }
            if (foundIt == false) {
                System.out.println("Video found!");
                list_contact.add(new ListItem(name, ManagmentOperations.FormatPhoneNumber(phoneNumber)    ,R.drawable.user));
            }

                //something here



            }*/
            //messageSend("in last line in try UpdetListContact everything is ok");



        //ArrayList<ListItem>newList = new ArrayList<ListItem>(new LinkedHashSet<ListItem>(list_contact));
// if the name is save chane his text
        // case who find me
        String IsFound=null;
        for (ListItem cs : list_contact) {
            if ( SettingSaved.WhoFindMeTag==0)
                IsFound=/*SettingSaved.WhoFindMeIN.get(cs.Detals); // for case who could find me list*/ManagmentOperations.IsPhoneIn(cs.Detals,SettingSaved.UserPhoneNumber);
            else
                IsFound= ManagmentOperations.IsPhoneIn(cs.Detals,SettingSaved.UserTrackedNumber); //SettingSaved.WhoIFindIN.get(cs.Detals);  // for case who i could find list

            if (IsFound!=null)
                list.add(new ListItem(cs.Title, cs.Detals, R.drawable.added));
            else
                list.add(new ListItem(cs.Title, cs.Detals, R.drawable.user));

        }

        }
        catch (Exception ex){}
        adpter.notifyDataSetChanged();
    }
    private void getContacts() {
        Log.d("Heyaa","First ya");
        Log.d("Heyaa","Second ya");
        contacts = new Contacts(this).getContactList();
        //HashMap<String,String> c = (HashMap<String, String>) contacts.getSerializableExtra(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        Log.d("Hey1","uouba");

        Log.d("Heyaa",contacts.toString()+" yala bena");
        for (HashMap.Entry<String, String> entry : contacts.entrySet()) {
            Log.d("Heyaa",entry.getValue());
            list_contact.add(new ListItem(entry.getValue(),entry.getKey()    ,R.drawable.user));

            //adapter.addContact(entry.getValue(), entry.getKey() );
        }

//        progress.visibility = View.GONE
    }
    //access to permsions
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 7:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    MsgSent(phone1,message1);
                }else{
                    Toast.makeText(this, "You denied applicatin to send messages. application will not do it's function", Toast.LENGTH_LONG).show();
                }
            }


            case 8:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    MsgSent1(phone2,message2);
                }else{
                    Toast.makeText(this, "You denied applicatin to send messages. application will not do it's function", Toast.LENGTH_LONG).show();
                }
            }

            return;
            case 125: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(this,"you are good",Toast.LENGTH_LONG).show();
                    Intent y = new Intent(this,ContactList.class);
                    startActivity(y);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                   // UpdetListContact();

                } else {
                    Toast.makeText(this, "You denied applicatin to read your contacts. application will not do it's function", Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            }

            // other 'case' lines to check for other
            // permissions this app might request
        }

    //get acces to location permsion





}
