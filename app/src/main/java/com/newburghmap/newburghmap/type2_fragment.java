package com.newburghmap.newburghmap;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class type2_fragment extends Fragment {


    public type2_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_type2_fragment, container, false);
    }
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        Activity act = getActivity();
        act = (MapsActivity) act;
        ((MapsActivity) act).locations("fin_lit");
    }

//    public ArrayList<String> locations(String subtype){
//        InputStream credentialsJSON = getResources().openRawResource(getResources().getIdentifier("service_account_credentials", "raw", getPackageName()));
//        try {
//            credential = GoogleCredential
//                    .fromStream(credentialsJSON, transport, jsonFactory)
//                    .createScoped(Collections.singleton(FusiontablesScopes.FUSIONTABLES_READONLY));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        fclient = new Fusiontables.Builder(
//                transport, jsonFactory, credential).setApplicationName("TestMap/1.0")
//                .build();
//        ArrayList<String> locations = new ArrayList<String>();
//        try {
//
//            Sqlresponse result = null;
//            String name, address;
//            String q = "SELECT subtype, subtypeES, name, address FROM "+tableId;
//
//            result = query(tableId,q);
//            List<List<Object>> rows = result.getRows();
//
//            for (List<Object> poi : rows) {
//                if(!spanish){
//                    String check = (String) poi.get(0);
//                    if(subtype.equals(check)){
//                        name = (String) poi.get(3);
//                        address = (String) poi.get(4);
//                        if(!locations.contains(name)){
//                            locations.add("Name: " + name + "/n Address: " + address);
//                        }
//                    }
//                }
//                else{
//                    String check = (String) poi.get(1);
//                    if(subtype.equals(check)){
//                        name = (String) poi.get(3);
//                        address = (String) poi.get(4);
//                        if(!locations.contains(name)){
//                            locations.add("Name: " + name + "/n Address: " + address);
//                        }
//                    }
//                }
//            }
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return locations;
//    }

}
