package com.mysampleapp.Waffle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.content.ContentDownloadPolicy;
import com.amazonaws.mobile.content.ContentItem;
import com.amazonaws.mobile.content.ContentListHandler;
import com.amazonaws.mobile.content.ContentManager;
import com.amazonaws.mobile.content.ContentProgressListener;
import com.amazonaws.mobile.util.ThreadUtils;
import com.mysampleapp.R;
import com.mysampleapp.demo.nosql.DemoNoSQLOperation;
import com.mysampleapp.demo.nosql.DemoNoSQLResult;
import com.mysampleapp.demo.nosql.DemoNoSQLTableWaffles.WaffleTestOperation;
import com.mysampleapp.demo.nosql.DemoNoSQLWafflesResult;
import com.mysampleapp.demo.nosql.DynamoDBUtils;
import com.mysampleapp.demo.nosql.WafflesDO;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WaffleShowFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WaffleShowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WaffleShowFragment extends Fragment {
    private static final String LOG_TAG = WaffleShowFragment.class.getSimpleName();
    /** The List View containing the NoSQL Operations that may be selected */
    private ListView operationsListView;
    private ContentManager contentManager;
    private WafflesDO currentWaffleObject;
    private WafflesDO oldWaffleObject;

    private ContentProgressListener progressListener1 = new ContentProgressListener() {
        @Override
        public void onSuccess(ContentItem contentItem) {
            showImage(contentItem, 1);
        }

        @Override
        public void onProgressUpdate(String filePath, boolean isWaiting, long bytesCurrent, long bytesTotal) {

        }

        @Override
        public void onError(String filePath, Exception ex) {

        }
    };

    private ContentProgressListener progressListener2 = new ContentProgressListener() {
        @Override
        public void onSuccess(ContentItem contentItem) {
            showImage(contentItem, 2);
        }

        @Override
        public void onProgressUpdate(String filePath, boolean isWaiting, long bytesCurrent, long bytesTotal) {

        }

        @Override
        public void onError(String filePath, Exception ex) {

        }
    };

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public WaffleShowFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WaffleShowFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WaffleShowFragment newInstance(String param1, String param2) {
        WaffleShowFragment fragment = new WaffleShowFragment();
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
        // Inflate the layout for this fragment
        getRandomWaffle(getActivity().getApplicationContext());
        return inflater.inflate(R.layout.fragment_waffle, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButton1Pressed() {
        cycleWaffle();
    }

    public void onButton2Pressed() {
        cycleWaffle();
    }

    private void cycleWaffle(){
        oldWaffleObject = currentWaffleObject;

        ((ImageButton)getActivity().findViewById(R.id.old_button_1)).setImageDrawable(((ImageButton)getActivity().findViewById(R.id.new_button_1)).getDrawable());
        ((ImageButton)getActivity().findViewById(R.id.old_button_2)).setImageDrawable(((ImageButton)getActivity().findViewById(R.id.new_button_2)).getDrawable());
        ((ImageButton)getActivity().findViewById(R.id.new_button_1)).setImageDrawable(null);
        ((ImageButton)getActivity().findViewById(R.id.new_button_2)).setImageDrawable(null);

        getRandomWaffle(getContext());
    }

    @Override
    public void onViewCreated(final View fragmentView, final Bundle savedInstanceState) {
        AWSMobileClient.defaultMobileClient().
            createDefaultContentManager(new ContentManager.BuilderResultHandler() {
                @Override
                public void onComplete(final ContentManager contentManager) {
                    if (!isAdded()) {
                        contentManager.destroy();
                        return;
                    }
                    final View fragmentView = getView();
                    WaffleShowFragment.this.contentManager = contentManager;
                }
            });
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void getRandomWaffle(final Context context){

        final WaffleTestOperation operation = new WaffleTestOperation(context);

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean foundResults = false;
                try {
                    foundResults = operation.executeOperation();
                } catch (final AmazonClientException ex) {
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e(LOG_TAG, String.format("Failed executing selected DynamoDB table (%s) operation (%s) : %s",
                                    operation.getTitle(), ex.getMessage()), ex);
                            DynamoDBUtils.showErrorDialogForServiceException(getActivity(),
                                    getString(R.string.nosql_dialog_title_failed_operation_text), ex);
                        }
                    });
                    return;
                }

                if (!foundResults) {
                    return;//handleNoResultsFound();
                } else {
                    currentWaffleObject = ((DemoNoSQLWafflesResult)operation.getNextResultGroup().get(0)).getResult();
                    System.out.println(currentWaffleObject.toString());
                    retrieveContent();
                }
            }
        }).start();
    }

    private void retrieveContent(){


        contentManager.listAvailableContent(currentWaffleObject.getOpt1(), new ContentListHandler() {
            @Override
            public boolean onContentReceived(int startIndex, List<ContentItem> partialResults, boolean hasMoreResults) {
                if(partialResults != null && partialResults.size() != 0){
                    try{
                        for(ContentItem item: partialResults) {
                            contentManager.getContent(item.getFilePath(), item.getSize(), ContentDownloadPolicy.DOWNLOAD_ALWAYS, false, progressListener1);
                        }

                    }catch(Exception e){
                        e.printStackTrace();
                        return false;
                    }
                }
                return false;
            }
            @Override
            public void onError(Exception ex) {

            }
        });

        contentManager.listAvailableContent(currentWaffleObject.getOpt2(), new ContentListHandler() {
            @Override
            public boolean onContentReceived(int startIndex, List<ContentItem> partialResults, boolean hasMoreResults) {
                if (partialResults != null && partialResults.size() != 0) {
                    try {
                        for (ContentItem item : partialResults) {
                            contentManager.getContent(item.getFilePath(), item.getSize(), ContentDownloadPolicy.DOWNLOAD_ALWAYS, false, progressListener2);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                return false;
            }

            @Override
            public void onError(Exception ex) {

            }
        });



    }

    private void showImage(ContentItem content, int imageNum){
        ImageButton button;
        if(imageNum == 1) {

            button = (ImageButton) getActivity().findViewById(R.id.new_button_1);
        }else {
            button = (ImageButton) getActivity().findViewById(R.id.new_button_2);
        } try {

            Drawable d = getAssetImage(getContext(), content.getFile());

            button.setImageDrawable(d);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void showResultsForOperation(final DemoNoSQLOperation operation) {
        List<DemoNoSQLResult> results = operation.getNextResultGroup();
        for(DemoNoSQLResult result: results){
            if(result instanceof DemoNoSQLWafflesResult){
                Log.d("Result Test : ", ((DemoNoSQLWafflesResult) result).getResult().toString());
//            System.out.println(result.toString());
            }

        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public static Drawable getAssetImage(Context context, File file) throws IOException {
        InputStream buffer = new FileInputStream(file);
        Bitmap bitmap = BitmapFactory.decodeStream(buffer);
        return new BitmapDrawable(context.getResources(), bitmap);
    }

}
