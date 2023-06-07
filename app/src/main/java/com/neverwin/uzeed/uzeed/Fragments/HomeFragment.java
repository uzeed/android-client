package com.neverwin.uzeed.uzeed.Fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neverwin.uzeed.uzeed.Activities.CityActivity;
import com.neverwin.uzeed.uzeed.Activities.EstablecimientoActivity;
import com.neverwin.uzeed.uzeed.Activities.MainActivity;
import com.neverwin.uzeed.uzeed.Activities.SearchEstablecimientoActivity;
import com.neverwin.uzeed.uzeed.Activities.SeekActivity;
import com.neverwin.uzeed.uzeed.Activities.SeekEstablecimientoActivity;
import com.neverwin.uzeed.uzeed.Model.UserCategoria;
import com.neverwin.uzeed.uzeed.R;
import com.neverwin.uzeed.uzeed.Utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LinearLayout btStripers,btMasajista,btNightClub,btSexShop, btCines, btDespedidas;
    private TextView tvBuscando;
    private Button btnStripper, btnClubs, btnDespedida, btnShops, btnMasajes, btnCines;
    private UserCategoria userCategoria;
    private Typeface robo, titillium;
    final String fuenteRobo="fuentes/Roboto-Regular.ttf";
    final String fuenteTitillium="fuentes/TitilliumWeb-Regular.ttf";
    public HomeFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    private void setViews(View view) {
        btStripers = (LinearLayout) view.findViewById(R.id.bt_stripers);
        btMasajista=(LinearLayout)view.findViewById(R.id.bt_Masajes);
        btNightClub = (LinearLayout)view.findViewById(R.id.bt_NightClub);
        btSexShop=(LinearLayout)view.findViewById(R.id.bt_SexShop);
        btCines=(LinearLayout) view.findViewById(R.id.bt_Cines);
        btDespedidas = view.findViewById(R.id.bt_Despedidas);
        tvBuscando=view.findViewById(R.id.tv_queestasbuscando);
        btnStripper =view.findViewById(R.id.home_stripers);
        btnClubs =view.findViewById(R.id.home_club);
        btnDespedida=view.findViewById(R.id.home_despedida);
        btnShops =view.findViewById(R.id.home_shops);
        btnMasajes=view.findViewById(R.id.home_masajista);
        btnCines=view.findViewById(R.id.home_cine);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setViews(view);
        setListeners();
        robo=Typeface.createFromAsset(getActivity().getAssets(),fuenteRobo);
        titillium=Typeface.createFromAsset(getActivity().getAssets(),fuenteTitillium);
        userCategoria = new UserCategoria();
        tvBuscando.setTypeface(titillium);
        btnStripper.setTypeface(titillium);
        btnClubs.setTypeface(titillium);
        btnDespedida.setTypeface(titillium);
        btnShops.setTypeface(titillium);
        btnMasajes.setTypeface(titillium);
        btnCines.setTypeface(titillium);
        return view;
    }

    private void setListeners() {

        btStripers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                intent = new Intent(getActivity(), SeekActivity.class);
                Bundle bundle = new Bundle();
                userCategoria.setIdUsuario(Utils.getUsuario().getId());
                userCategoria.setIdCategoria(3);
                bundle.putSerializable("userCategoria", userCategoria);
                intent.putExtras(bundle);
                btStripers.getContext().startActivity(intent);
            }
        });
        btMasajista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                intent = new Intent(getActivity(), SeekActivity.class);
                Bundle bundle = new Bundle();
                userCategoria.setIdUsuario(Utils.getUsuario().getId());
                userCategoria.setIdCategoria(1);
                bundle.putSerializable("userCategoria", userCategoria);
                intent.putExtras(bundle);
                btMasajista.getContext().startActivity(intent);
            }
        });
        btDespedidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                //intent = new Intent(getActivity(), SeekActivity.class);
                intent = new Intent(getActivity(), SearchEstablecimientoActivity.class);
                Bundle bundle = new Bundle();
                userCategoria.setIdUsuario(Utils.getUsuario().getId());
                userCategoria.setIdCategoria(7);
                userCategoria.setDescripcionCategoria("Despedidas de solteros/as");
                bundle.putSerializable("userCategoria", userCategoria);
                intent.putExtras(bundle);
                btDespedidas.getContext().startActivity(intent);
            }
        });

        btNightClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                intent = new Intent(getActivity(), SearchEstablecimientoActivity.class);
                Bundle bundle = new Bundle();
                userCategoria.setIdUsuario(Utils.getUsuario().getId());
                userCategoria.setIdCategoria(4);
                userCategoria.setDescripcionCategoria("Night Clubs");
                bundle.putSerializable("userCategoria", userCategoria);
                intent.putExtras(bundle);
                btNightClub.getContext().startActivity(intent);
            }
        });

        btSexShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                intent = new Intent(getActivity(), SearchEstablecimientoActivity.class);
                Bundle bundle = new Bundle();
                userCategoria.setIdUsuario(Utils.getUsuario().getId());
                userCategoria.setIdCategoria(5);
                userCategoria.setDescripcionCategoria("Sex-shops");
                bundle.putSerializable("userCategoria", userCategoria);
                intent.putExtras(bundle);
                btSexShop.getContext().startActivity(intent);
            }
        });
        btCines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                intent = new Intent(getActivity(), SearchEstablecimientoActivity.class);
                Bundle bundle = new Bundle();
                userCategoria.setIdUsuario(Utils.getUsuario().getId());
                userCategoria.setIdCategoria(6);
                userCategoria.setDescripcionCategoria("Moteles");
                bundle.putSerializable("userCategoria", userCategoria);
                intent.putExtras(bundle);
                btCines.getContext().startActivity(intent);
            }
        });

    }

}
