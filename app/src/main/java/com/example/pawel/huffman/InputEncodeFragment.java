package com.example.pawel.huffman;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InputEncodeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InputEncodeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class InputEncodeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    static final boolean readFromFile = false;
    static final boolean newTextBasedOnOldOne = false;

    static PriorityQueue<Node> nodes = new PriorityQueue<>((o1, o2) -> (o1.value < o2.value) ? -1 : 1);
    static TreeMap<Character, String> codes = new TreeMap<>();
    static String text = "";
    static String encoded = "";
    static String decoded = "";

    static Map<String,HashMap> charsData;


    static int ASCII[] = new int[100000];

    public InputEncodeFragment() {
        // Required empty public constructor
    }




    public static InputEncodeFragment newInstance(String param1, String param2) {
        InputEncodeFragment fragment = new InputEncodeFragment();
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

        View view = inflater.inflate(R.layout.fragment_input_encode, container, false);
        Button encodeButton =(Button) view.findViewById(R.id.encodeButton);
        EditText textField = view.findViewById(R.id.textToEncode);
        TextView rawText = view.findViewById(R.id.inputText);
        TextView encodedText = view.findViewById(R.id.encodedText);
        TextView entropia = view.findViewById(R.id.entropia);
        TextView wordLengthText = view.findViewById(R.id.wordLength);
        TextView inBit = view.findViewById(R.id.inputBits);
        TextView inByte = view.findViewById(R.id.inputBytes);
        TextView outBit = view.findViewById(R.id.outputBits);
        TextView outByte = view.findViewById(R.id.outputBytes);
        TextView bitCompress = view.findViewById(R.id.bitsCompression);
        TextView byteCompress = view.findViewById(R.id.bytesCompression);

        charsData = new HashMap<String,HashMap>();
        Bundle bundle = getArguments();
        if(bundle.getString("text")!=null) {
            //charsData = (HashMap) bundle.getSerializable("data");
            textField.setText(bundle.getString("text"));
            rawText.setText(bundle.getString("text"));
            encodedText.setText(bundle.getString("encoded"));
            entropia.setText(bundle.getString("entropy"));
            wordLengthText.setText(bundle.getString("avgWord"));
            inByte.setText(Integer.toString(bundle.getString("text").length()));
            inBit.setText(Integer.toString(bundle.getString("text").length()*8));
            outBit.setText(Integer.toString(bundle.getString("encoded").length()));
            Integer outByteValue = bundle.getString("encoded").length()/8;
            if(bundle.getString("encoded").length()%8!=0)
                outByteValue+=1;
            outByte.setText(Integer.toString(outByteValue));
            Double bitCom = 1 - Double.valueOf(bundle.getString("encoded").length()) / Double.valueOf(bundle.getString("text").length()*8);
            Double byteCom = 1 - Double.valueOf(outByteValue)/Double.valueOf(bundle.getString("text").length());
            bitCompress.setText(Double.toString(bitCom*100) + "%");
            byteCompress.setText(Double.toString(byteCom*100) + "%");


        }
        encodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textNew = textField.getText().toString();
                handleNewText(textNew);
                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);

                Double IntervalSum = 0.0;
                Double avgWordLength = 0.0;
//                for(int i=0; i<text.length();i++){
//                    Double val = Double.parseDouble((charsData.get("" + text.charAt(i))).get("interval").toString());
//                    IntervalSum += val * Math.log(1/val)/Math.log(2);
//                }
                for ( String key : charsData.keySet() ) {
                    Double val = Double.parseDouble((charsData.get(key)).get("interval").toString());
                    Integer wordLength = ((charsData.get(key)).get("code").toString()).length();
                    IntervalSum += val * Math.log(1/val)/Math.log(2);
                    avgWordLength += val * wordLength;
                }

                intent.putExtra("data", (Serializable) charsData);
                intent.putExtra("text", text);
                intent.putExtra("encoded", encoded);
                intent.putExtra("entropy", IntervalSum.toString());
                intent.putExtra("avgWord", avgWordLength.toString());
                startActivity(intent);
                getActivity().finish();
            }
        });

        // Inflate the layout for this fragment
        return view;
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
            //Toast.makeText(context, "Wprowadzanie", Toast.LENGTH_SHORT).show();
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

    private static boolean handleNewText(String textt) {
        System.out.println("Enter the text:");
        text = textt;

        ASCII = new int[100000];
        nodes.clear();
        codes.clear();
        encoded = "";
        decoded = "";
        System.out.println("Text: " + text);
        calculateCharIntervals(nodes, true);
        buildTree(nodes);
        generateCodes(nodes.peek(), "");

        printCodes();
        System.out.println("-- Encoding/Decoding --");
        encodeText();
        decodeText();
        return false;



    }

    private static boolean IsSameCharacterSet() {
        boolean flag = true;
        for (int i = 0; i < text.length(); i++)
            if (ASCII[text.charAt(i)] == 0) {
                flag = false;
                break;
            }
        return flag;
    }

    private static void decodeText() {
        decoded = "";
        Node node = nodes.peek();
        for (int i = 0; i < encoded.length(); ) {
            Node tmpNode = node;
            while (tmpNode.left != null && tmpNode.right != null && i < encoded.length()) {
                if (encoded.charAt(i) == '1')
                    tmpNode = tmpNode.right;
                else tmpNode = tmpNode.left;
                i++;
            }
            if (tmpNode != null)
                if (tmpNode.character.length() == 1)
                    decoded += tmpNode.character;
                else
                    System.out.println("Input not Valid");

        }
        System.out.println("Decoded Text: " + decoded);
    }

    private static void encodeText() {
        encoded = "";
        for (int i = 0; i < text.length(); i++)
            encoded += codes.get(text.charAt(i));
        System.out.println("Encoded Text: " + encoded);
    }

    private static void buildTree(PriorityQueue<Node> vector) {
        while (vector.size() > 1)
            vector.add(new Node(vector.poll(), vector.poll()));
    }

    private static void printCodes() {
        System.out.println("--- Printing Codes ---");
        codes.forEach((k, v) -> {
            System.out.println("'" + k + "' : " + v);
            HashMap<String,String> data = charsData.get("" + k);
            data.put("code" , v);
            charsData.put("" + k,data);
        });
    }

    private static void calculateCharIntervals(PriorityQueue<Node> vector, boolean printIntervals) {
        if (printIntervals) System.out.println("-- intervals --");

        for (int i = 0; i < text.length(); i++)
            ASCII[text.charAt(i)]++;

        for (int i = 0; i < ASCII.length; i++)
            if (ASCII[i] > 0) {
                vector.add(new Node(ASCII[i] / (text.length() * 1.0), ((char) i) + ""));
                if (printIntervals) {
                    System.out.println("'" + ((char) i) + "' : " + ASCII[i] / (text.length() * 1.0));
                    HashMap data = new HashMap();
                    data.put("interval" , Double.toString(ASCII[i] / (text.length() * 1.0)));
                    data.put("ascii", Integer.toString((int) i));
                    data.put("count", ASCII[i]);
                    charsData.put("" +((char) i), data);
                }
            }
    }

    private static void generateCodes(Node node, String s) {
        if (node != null) {
            if (node.right != null)
                generateCodes(node.right, s + "1");

            if (node.left != null)
                generateCodes(node.left, s + "0");

            if (node.left == null && node.right == null) {
                codes.put(node.character.charAt(0), s);
            }
        }
    }
}

class Node {
    Node left, right;
    double value;
    String character;

    public Node(double value, String character) {
        this.value = value;
        this.character = character;
        left = null;
        right = null;
    }

    public Node(Node left, Node right) {
        this.value = left.value + right.value;
        character = left.character + right.character;
        if (left.value < right.value) {
            this.right = right;
            this.left = left;
        } else {
            this.right = left;
            this.left = right;
        }
    }
}
