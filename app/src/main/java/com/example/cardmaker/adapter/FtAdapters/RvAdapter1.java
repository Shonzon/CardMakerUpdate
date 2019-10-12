package com.example.cardmaker.adapter.FtAdapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cardmaker.MyCardEdit.McEdit1;
import com.example.cardmaker.R;
import com.example.cardmaker.models.RvModel1;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class RvAdapter1 extends RecyclerView.Adapter<RvAdapter1.MyHolder>{

    List<RvModel1> listdata;
    Context mContext;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;

    public RvAdapter1(Context context, List<RvModel1> listdata) {
        this.mContext = context;
        this.listdata = listdata;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.test_card1,viewGroup,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        holder.pname.setText(listdata.get(position).getName());
        holder.designation.setText(listdata.get(position).getDesignation());
        holder.email.setText(listdata.get(position).getEmail());
        holder.pphone.setText(String.valueOf(listdata.get(position).getPhone()));

        holder.pdec1.setText(listdata.get(position).getDec1());
        holder.pdec2.setText(listdata.get(position).getDec2());
        holder.pdec3.setText(listdata.get(position).getDec3());

        //holder.tempID.setText(listdata.get(position).getTempID());

        Glide.with(mContext).load(listdata.get(position).getLogo()).into(holder.logo_image);
    }

    @Override
    public int getItemCount() {
        //return listdata.size();
        int arr = 0;
        try{
            if(listdata.size()==0){
                arr = 0;
            }
            else{
                arr=listdata.size();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return arr;
    }

    class MyHolder extends RecyclerView.ViewHolder{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        TextView pname,email,pdec1,pdec2,pdec3,pphone,designation,optionMenu;
        ImageView logo_image;

        public MyHolder(final View itemView) {
            super(itemView);

            //optionMenu = itemView.findViewById(R.id.optionMenu);

            pname = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.address);
            pphone = itemView.findViewById(R.id.phone);
            designation = itemView.findViewById(R.id.designation);
            pdec1 = itemView.findViewById(R.id.dsc1);
            pdec2 = itemView.findViewById(R.id.dsc2);
            pdec3 = itemView.findViewById(R.id.dsc3);
            logo_image = itemView.findViewById(R.id.logo);
            //tempID = itemView.findViewById(R.id.tempID);


            /*optionMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    //will show popup menu here
                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(mContext,optionMenu);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.options_menu);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.add:
                                    Log.d("Click","Item 1");

                                    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                    FirebaseUser me = mAuth.getCurrentUser();
                                    DatabaseReference myRef = mDatabase.getReference().child("usertemplate").child(mAuth.getCurrentUser().getUid());

                                    String userID = mAuth.getCurrentUser().getUid();
                                    String imageUri = "drawable://" + R.drawable.constant;


                                    RvModel1 data = new RvModel1(pname.getText().toString()
                                            ,designation.getText().toString()
                                            ,pphone.getText().toString(),email.getText().toString()
                                            ,pdec1.getText().toString()
                                            ,pdec2.getText().toString()
                                            ,pdec3.getText().toString()
                                            ,imageUri);
                                    myRef.child("two").setValue(data);
                                    Toast.makeText(v.getContext(),"Added to mycard",Toast.LENGTH_SHORT).show();

                                    break;
                                case R.id.edit:
                                    Log.d("Click","Item 2");
                                    break;
                                case R.id.close:
                                    Log.d("Click","Item 3");
                                    break;
                            }
                            return false;
                        }
                    });
                    //displaying the popup
                    popup.show();
                }
            });*/

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri path = Uri.parse("https://firebasestorage.googleapis.com/v0/b/card-maker-a6a72.appspot.com/o/templatelogo%2F65395388_435718940614838_5223498045248765952_n.jpg?alt=media&token=646bdded-d5f7-48e9-8a1c-3be419117ae7");
                    String imgPath = path.toString();
                    Intent edit1 = new Intent(mContext, McEdit1.class);
                    edit1.putExtra("logo",imgPath);

                    firebaseDatabase = FirebaseDatabase.getInstance();
                    myRef = firebaseDatabase.getReference("usertemplate").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("two");

                    HashMap<String, Object> result1 = new HashMap<>();
                    result1.put("name", "Name");
                    myRef.updateChildren(result1);

                    HashMap<String, Object> result2 = new HashMap<>();
                    result2.put("designation", "Designation");
                    myRef.updateChildren(result2);

                    HashMap<String, Object> result3 = new HashMap<>();
                    result3.put("email", "Address");
                    myRef.updateChildren(result3);

                    HashMap<String, Object> result4 = new HashMap<>();
                    result4.put("phone", "Contact No");
                    myRef.updateChildren(result4);

                    HashMap<String, Object> result5 = new HashMap<>();
                    result5.put("dec1", "Description 1");
                    myRef.updateChildren(result5);

                    HashMap<String, Object> result6 = new HashMap<>();
                    result6.put("dec2", "Description 2");
                    myRef.updateChildren(result6);

                    HashMap<String, Object> result7 = new HashMap<>();
                    result7.put("dec3", "Description 3");
                    myRef.updateChildren(result7);

                    HashMap<String, Object> result8 = new HashMap<>();
                    result8.put("logo", imgPath);
                    myRef.updateChildren(result8);

                    mContext.startActivity(edit1);
                }
            });
        }
    }

}
