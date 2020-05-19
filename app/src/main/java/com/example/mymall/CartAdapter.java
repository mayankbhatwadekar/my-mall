package com.example.mymall;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter {

    private List<CartItemModel> cartItemModelList;
    private int lastPosition = -1;
    private TextView cartTotalAmount;
    private boolean showDeleteBtn;

    public CartAdapter(List<CartItemModel> cartItemModelList,TextView cartTotalAmount, boolean showDeletBtn) {
        this.cartItemModelList = cartItemModelList;
        this.cartTotalAmount = cartTotalAmount;
        this.showDeleteBtn = showDeletBtn;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemModelList.get(position).getType()) {
            case 0:
                return CartItemModel.CART_ITEM;
            case 1:
                return CartItemModel.TOTAL_AMOUNT;
                default:
                    return -1;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType){
            case CartItemModel.CART_ITEM:
                        View cartItemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item_layout,viewGroup,false);
                        return new CartItemViewHolder(cartItemView);
            case CartItemModel.TOTAL_AMOUNT:
                        View cartTotalView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_total_amount,viewGroup,false);
                return new CartTotalAmountViewHolder(cartTotalView);

            default:return null;
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch(cartItemModelList.get(position).getType()){
            case CartItemModel.CART_ITEM:
                    String productID = cartItemModelList.get(position).getProductID();
                    String resource = cartItemModelList.get(position).getProductImage();
                    String title = cartItemModelList.get(position).getProductTitle();
                    Long freeCoupons = cartItemModelList.get(position).getFreeCoupens();
                    String productPrice = cartItemModelList.get(position).getProductPrice();
                    String cuttedPrice = cartItemModelList.get(position).getCuttedPrice();
                    Long offerApplied = cartItemModelList.get(position).getOffersAppllied();
                    boolean inStock  = cartItemModelList.get(position).isInStock();

                    ((CartItemViewHolder)viewHolder).setItemDetails(productID,resource,title,freeCoupons,productPrice,cuttedPrice,offerApplied,position,inStock);
                break;
            case CartItemModel.TOTAL_AMOUNT:
                    int totalItems=0;
                    int totalItemPrice=0;
                    String deliveryPrice;
                    int totalAmount;
                    int savedAmount=0;
                for(int x=0;x<cartItemModelList.size();x++){
                    if(cartItemModelList.get(x).getType()==CartItemModel.CART_ITEM && cartItemModelList.get(x).isInStock()){
                        totalItems++;
                        totalItemPrice = Integer.parseInt(totalItemPrice+cartItemModelList.get(x).getProductPrice());
                    }
                }
                if(totalItemPrice>500){
                    deliveryPrice = "FREE";
                    totalAmount = totalItemPrice;
                }
                else{
                    deliveryPrice = "60";
                    totalAmount = totalItemPrice+60;
                }

                ((CartTotalAmountViewHolder)viewHolder).setTotalAmount(totalItems,totalItemPrice,deliveryPrice,totalAmount,savedAmount);
                break;
                default:
                    return;

        }
        if(lastPosition<position) {
            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.fade_in);
            viewHolder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }

    public class CartItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView productImage,freeCouponIcon;
        private TextView productTitle,freeCoupons,productPrice,cuttedPrice,offersApplied, couponsApplied,productQuantity;
        private LinearLayout deleteBtn,couponRedemptionLayout;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            freeCoupons = itemView.findViewById(R.id.tv_free_coupon);
            freeCouponIcon = itemView.findViewById(R.id.free_coupon_icon);
            productPrice = itemView.findViewById(R.id.product_price);
            cuttedPrice = itemView.findViewById(R.id.cutted_price);
            offersApplied = itemView.findViewById(R.id.offers_applied);
            couponsApplied = itemView.findViewById(R.id.coupons_applied);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            deleteBtn = itemView.findViewById(R.id.remove_item_btn);
            couponRedemptionLayout = itemView.findViewById(R.id.coupon_redemption_layout);
        }

        private void setItemDetails(String productID, String resource, String title, Long freeCouponsNo, String productPriceText, String cuttedPriceText, Long offersAppliedNo, final int position,boolean inStock){

            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.mipmap.image_not_found)).into(productImage);
            productTitle.setText(title);

                if(inStock){
                    if(freeCouponsNo>0){
                        freeCouponIcon.setVisibility(View.VISIBLE);
                        freeCoupons.setVisibility(View.VISIBLE);
                        freeCoupons.setText("free "+freeCouponsNo+" Coupons");
                    }else {
                        freeCouponIcon.setVisibility(View.INVISIBLE);
                        freeCoupons.setVisibility(View.INVISIBLE);
                    }

                    productPrice.setText("Rs. "+productPriceText+"/-");
                    productPrice.setTextColor(Color.parseColor("#000000"));
                    cuttedPrice.setText("Rs. "+cuttedPriceText+"/-");
                    couponRedemptionLayout.setVisibility(View.VISIBLE);
                    freeCoupons.setVisibility(View.INVISIBLE);

                    productQuantity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog quantityDialog = new Dialog(itemView.getContext());
                            quantityDialog.setContentView(R.layout.qauntity_dialog);
                            quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            quantityDialog.setCancelable(false);

                            final EditText quantityNo = quantityDialog.findViewById(R.id.quantity_number);
                            Button cancelButton = quantityDialog.findViewById(R.id.cancel_btn);
                            Button okButton = quantityDialog.findViewById(R.id.ok_btn);

                            cancelButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    quantityDialog.dismiss();
                                }
                            });

                            okButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    productQuantity.setText("Qty: "+quantityNo.getText());
                                    quantityDialog.dismiss();
                                }
                            });

                            quantityDialog.show();
                        }
                    });
                    if(offersAppliedNo>0){
                        offersApplied.setVisibility(View.VISIBLE);
                        offersApplied.setText(offersAppliedNo+" Offers Applied");
                    }
                    else{
                        offersApplied.setVisibility(View.INVISIBLE);
                    }
                }else{
                    productPrice.setText("Out of Stock");
                    productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                    cuttedPrice.setText("");
                    couponRedemptionLayout.setVisibility(View.GONE);
                    couponsApplied.setVisibility(View.GONE);
                   productQuantity.setVisibility(View.INVISIBLE);
                   offersApplied.setVisibility(View.GONE);
                   freeCoupons.setVisibility(View.INVISIBLE);
                   freeCouponIcon.setVisibility(View.INVISIBLE);
                }


            if(showDeleteBtn){
                deleteBtn.setVisibility(View.VISIBLE);
            }
            else{
                deleteBtn.setVisibility(View.GONE);
            }
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!ProductDetailsActivity.running_cart_query){
                        ProductDetailsActivity.running_cart_query=true;
                        DbQueries.removeFromCart(position,itemView.getContext(),cartTotalAmount);
                    }
                }
            });
        }

    }

    class CartTotalAmountViewHolder extends RecyclerView.ViewHolder{
            private TextView totalItems,totalItemsPrice,deliveryPrice,totalAmount,savedAmount;
        public CartTotalAmountViewHolder(@NonNull View itemView) {
            super(itemView);

            totalItems = itemView.findViewById(R.id.total_items);
            totalItemsPrice = itemView.findViewById(R.id.total_items_price);
            deliveryPrice = itemView.findViewById(R.id.delivery_price);
            totalAmount = itemView.findViewById(R.id.total_price);
            savedAmount = itemView.findViewById(R.id.saved_amount);

        }
        private void setTotalAmount(int totalItemText,int totalItemPriceText,String deliveryPriceText,int totalAmountText,int savedAmountText){
            totalItems.setText("Price ("+totalItemText+" items)");
            totalItemsPrice.setText("Rs."+totalItemPriceText+"/-");
            if(deliveryPriceText.equalsIgnoreCase("FREE")){
            deliveryPrice.setText(deliveryPriceText);
            }else{
                deliveryPrice.setText("Rs."+deliveryPriceText+"/-");
            }
            totalAmount.setText("Rs."+totalAmountText);
            cartTotalAmount.setText("Rs."+totalAmountText);
            savedAmount.setText("You saved Rs."+savedAmountText+"/- on this order.");

            LinearLayout parent=(LinearLayout) cartTotalAmount.getParent().getParent();
            if(totalItemPriceText==0){
                DbQueries.cartItemModelList.remove(DbQueries.cartItemModelList.size()-1);
                parent.setVisibility(View.GONE);
            }
            else{
                parent.setVisibility(View.VISIBLE);
            }
        }
    }
}
