package in.co.tripin.offeeuser.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import in.co.tripin.offeeuser.R;


/**
 * Created by Tripin1 on 6/21/2017.
 */

public class OrderListHolder extends RecyclerView.ViewHolder {

    public RecyclerView recyclerView;

    public OrderListHolder(View itemView) {
        super(itemView);
        recyclerView = (RecyclerView)itemView.findViewById(R.id.rv_myorderlists);
    }

}