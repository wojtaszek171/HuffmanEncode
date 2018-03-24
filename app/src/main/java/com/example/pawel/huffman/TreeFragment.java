package com.example.pawel.huffman;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TreeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TreeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TreeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    int longest;
    int widthMin;
    private OnFragmentInteractionListener mListener;
    static Map<String,HashMap> charsData;

    public TreeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TreeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TreeFragment newInstance(String param1, String param2) {
        TreeFragment fragment = new TreeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MyView customView;
        ScrollView scroll_view;
        HorizontalScrollView h_scroll_view;
        scroll_view = new ScrollView(getActivity());
        h_scroll_view = new HorizontalScrollView(getActivity());
        customView = new MyView(getActivity());

        scroll_view.addView(customView);
        h_scroll_view.addView(scroll_view);


        View view = inflater.inflate(R.layout.fragment_tree, container, false);
        charsData = new HashMap<String,HashMap>();
        Bundle bundle = getArguments();
        if(bundle!=null) {
            charsData = (HashMap) bundle.getSerializable("data");
            int currentLength = 0;
            for ( String key : charsData.keySet() ) {
                currentLength=(charsData.get(key)).get("code").toString().length();
                if(currentLength>longest){
                    longest=currentLength;
                }
            }
            widthMin=0;
            for (int i=0; i<longest;i++)
                widthMin+=(30*Math.pow(2,longest-i));
            widthMin*=2;
        }
        return h_scroll_view;
        // Inflate the layout for this fragment
        //return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //Toast.makeText(context, "Drzewo", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public class MyView extends View
    {
        Paint paint = null;
        public MyView(Context context)
        {
            super(context);
            paint = new Paint();
        }
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
        {
            int width = 4500;
            int height = 1500;
            if(longest!=0)
                width= widthMin+100;
            setMeasuredDimension(width, height);
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);
            int x = getWidth();
            int y = getHeight();
            int radius;
            radius = 30;
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);
            // Use Color.parseColor to define HTML colors
            paint.setColor(Color.parseColor("#CD5C5C"));
            paint.setTextSize(40);
            canvas.drawCircle(x/2, 100, radius, paint);

            for ( String key : charsData.keySet() ) {
                x=getWidth()/2;
                y=100;
                String val = (charsData.get(key)).get("code").toString();
                for(int i=0;i<val.length();i++) {
                    int oldX =x;
                    int oldY =y;
                    if (val.charAt(i) == '1'){
                        y+=70;

                            x+= 30*Math.pow(2,longest-i);



                            canvas.drawLine(oldX, oldY, x, y, paint);
                            canvas.drawCircle(x, y, radius, paint);
                            radius = 30;
                    }else
                        if(val.charAt(i) == '0'){
                            y+=70;

                                x-= 30*Math.pow(2,longest-i);


                            canvas.drawLine(oldX, oldY, x, y, paint);
                            canvas.drawCircle(x, y, radius, paint);
                            radius = 30;
                        }
                }

            }
            for ( String key : charsData.keySet() ) {
                x=getWidth()/2;
                y=100;
                String val = (charsData.get(key)).get("code").toString();
                for(int i=0;i<val.length();i++) {
                    if (val.charAt(i) == '1'){
                        y+=70;
                        x += 30*Math.pow(2,longest-i);
                    }else
                    if(val.charAt(i) == '0'){
                        y+=70;
                        x -= 30*Math.pow(2,longest-i);
                    }

                    if(i==val.length()-1){

                        paint.setColor(Color.parseColor("#ffffff"));
                        if(val.charAt(i) == '0')
                            canvas.drawText(key, x-10, y+10, paint);
                        else
                            canvas.drawText(key, x-10, y+10, paint);
                        paint.setColor(Color.parseColor("#CD5C5C"));
                    }

                }

            }
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            getView().scrollTo(widthMin/2 -displayMetrics.widthPixels/2,0);
        }
    }
}
