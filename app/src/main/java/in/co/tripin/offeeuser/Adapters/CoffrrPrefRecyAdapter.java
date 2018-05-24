package in.co.tripin.offeeuser.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;


import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;

import java.util.List;
import java.util.logging.Logger;

import in.co.tripin.offeeuser.Communicators.OnCoffeeSelected;
import in.co.tripin.offeeuser.Model.CoffeeOrder;
import in.co.tripin.offeeuser.R;
import in.co.tripin.offeeuser.holders.PrefCoffeeHolder;

/**
 * Created by Tripin1 on 6/20/2017.
 */

public class CoffrrPrefRecyAdapter extends RecyclerView.Adapter<PrefCoffeeHolder> {

    private List<CoffeeOrder> list;
    private Context mContext;
    private OnCoffeeSelected onCoffeeSelected;
    private int lastPosition = -1;


    public CoffrrPrefRecyAdapter(Context context , List<CoffeeOrder>list, OnCoffeeSelected onCoffeeSelected){
        mContext = context;
        this.list = list;
        this.onCoffeeSelected = onCoffeeSelected;
    }

    @Override
    public PrefCoffeeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_coffee_pref, parent, false);

        return new PrefCoffeeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PrefCoffeeHolder holder, int position) {
        final CoffeeOrder c = list.get(position);
        holder.getmNameField().setText(c.getmCoffeeName());
        if(!c.getmItemPrice().equalsIgnoreCase("0")){
            holder.mPrice.setText("₹"+c.getmItemPrice());
        }else{
            holder.mPrice.setText("Free");
        }
        Log.i("Coffe OnBindViewHolder "," "+position);

        holder.imgThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c.getmCoffeeNumber().equalsIgnoreCase("0")){
                    c.setmCoffeeNumber("1");
                    holder.getmNumberField().setText(c.getmCoffeeNumber());
                    holder.llNumber.setVisibility(View.VISIBLE);
                    holder.imgThumb.setImageResource(R.drawable.ic_whatshot_black_24dp2);
                    c.setmOrderStatus("Ordered");
                    onCoffeeSelected.onCoffeeSelected(c);
                    holder.getmNumberField().setText(c.getmCoffeeNumber());
                    holder.mPrice.setAlpha(1);

                }else {
                    holder.llNumber.setVisibility(View.GONE);
                    c.setmCoffeeNumber("0");
                    holder.imgThumb.setImageResource(R.drawable.ic_whatshot_black_24dp);
                    onCoffeeSelected.onCoffeeSelected(c);
                    holder.mPrice.setAlpha((float) 0.5);


                }
            }
        });

        holder.getmNameField().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(holder.llNumber.getVisibility()==View.VISIBLE)){
                    holder.mTapInfo.setVisibility(View.VISIBLE);
                }
                ViewAnimator.animate(holder.imgThumb).tada().duration(500).start().onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        holder.mTapInfo.setVisibility(View.GONE);
                    }
                });
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(holder.llNumber.getVisibility()==View.VISIBLE)){
                    holder.mTapInfo.setVisibility(View.VISIBLE);
                }
                ViewAnimator.animate(holder.imgThumb).tada().duration(1000).start().onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        holder.mTapInfo.setVisibility(View.GONE);
                    }
                });
            }
        });

        holder.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = Integer.parseInt(c.getmCoffeeNumber());
                Log.d("debug","n value "+n);

                if(n<5){
                    n++;
                    c.setmCoffeeNumber(String.valueOf(n));
                    c.setmOrderStatus("Ordered");
                    onCoffeeSelected.onCoffeeSelected(c);
                    holder.getmNumberField().setText(c.getmCoffeeNumber());
                    int p = Integer.parseInt(c.getmItemPrice());
                    holder.mPrice.setText("₹"+p*n);

                }else {
                    Toast.makeText(mContext,"Limit!",Toast.LENGTH_SHORT).show();
                }


            }
        });

        holder.fabMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = Integer.parseInt(c.getmCoffeeNumber());
                if(n>1){
                    n--;
                    c.setmCoffeeNumber(String.valueOf(n));
                    c.setmOrderStatus("Ordered");
                    onCoffeeSelected.onCoffeeSelected(c);
                    holder.getmNumberField().setText(c.getmCoffeeNumber());
                    int p = Integer.parseInt(c.getmItemPrice());
                    holder.mPrice.setText("₹"+p*n);

                }else {
                    Toast.makeText(mContext,"Dude!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Here you apply the animation when the view is bound
        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
