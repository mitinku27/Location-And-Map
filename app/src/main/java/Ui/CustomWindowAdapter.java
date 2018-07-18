package Ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.tinku.locationandmap.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by TINKU on 1/23/2018.
 */

public class CustomWindowAdapter implements GoogleMap.InfoWindowAdapter{
    Context context;
    View view;
    LayoutInflater layinflater;

    public CustomWindowAdapter(Context context) {
        this.context = context;
        layinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view= layinflater.inflate(R.layout.custoinmwindow,null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        TextView title= view.findViewById(R.id.titlewin);
        TextView mag= view.findViewById(R.id.magnitudewin);
        title.setText(marker.getTitle());
        mag.setText(marker.getSnippet());
        return view;
    }
}
